package nl.sonepar.ratingreviews.manager;

import nl.sonepar.ratingreviews.dao.ReviewDAO;
import nl.sonepar.ratingreviews.model.*;

import java.util.*;

import org.jboss.resteasy.logging.Logger;

/**
 * Review manager Created by Wilco on 08/01/2015.
 */
public class ReviewManager {

	private static final Logger log = Logger.getLogger(ReviewManager.class);
	private static final int MAX_RATING = 5;
	public static final String PARAM_PRODUCT_ID = "ProductId";
	public static final String PARAM_SINGLE_REVIEW = "ReviewId";
	public static final String PARAM_REVIEW_SEARCH = "ReviewSearch";
	public static final String PARAM_REVIEW_COMMENTS = "Comments";

	private ReviewDAO mReviewDAO;

	public ReviewManager() {
		mReviewDAO = new ReviewDAO();
	}

	/**
	 * Get the statistics for the products
	 * 
	 * @param reviewResponse
	 *            The object to place the response in
	 * @param filters
	 *            The filters to use to get the results
	 */
	public void statisticsForProducts(ReviewResponse reviewResponse,
			Map<String, List<String>> filters) {
		try {
			List<String> productIds = filters.get(PARAM_PRODUCT_ID);

			Map<String, Product> results = mReviewDAO
					.findCountsForProductList(productIds);
			Includes includes = reviewResponse.getOrCreateIncludes();

			includes.setProducts(results);

			for (String productId : includes.getProducts().keySet()) {
				Product product = includes.getProducts().get(productId);
				if (product.getTotalReviewCount() > 0) {
					addMissingRatingsAndAverage(product);
				}
			}
			reviewResponse.setLimit(results.size());
			reviewResponse.setTotalResults(results.size());
		} catch (Exception ignored) {
			ignored.printStackTrace();
			reviewResponse
					.getProblems()
					.add(new Problem(
							"Wrong format for products filter. Expected: Filter=ProductId:p1,p1,p3. Actual: Filter="
									+ filters, "ERROR_BAD_REQUEST"));
		}
	}

	/**
	 * Get the reviews for a product
	 * 
	 * @param reviewResponse
	 *            The object to place the response in
	 * @param pProductId
	 *            The filters to use to get the results
	 */
	public void getReviewsForProduct(ReviewResponse reviewResponse,
			String pProductId, int pMaxCount, boolean pApprovedOnly) {
		Map<String, List<String>> filters;
		List<Review> results = mReviewDAO.findAllProductReviews(pProductId,
				pMaxCount, pApprovedOnly);

		filters = new HashMap<>();
		List<String> products = new ArrayList<>();
		products.add(pProductId);
		filters.put(PARAM_PRODUCT_ID, products);
		statisticsForProducts(reviewResponse, filters);

		reviewResponse.setResults(results);
		reviewResponse.setLimit(results.size());
		reviewResponse.setTotalResults(results.size());

	}

	/**
	 * Get all unapproved reviews
	 * 
	 * @param reviewResponse
	 *            The object to place the response in
	 */
	public void getUnapprovedReviews(ReviewResponse reviewResponse,
			int pMaxCount) {
		List<Review> results = mReviewDAO.findAllUnapprovedReviews(pMaxCount);

		reviewResponse.setResults(results);
		reviewResponse.setLimit(results.size());
		reviewResponse.setTotalResults(results.size());
	}
	
	
	/**
	 * Get the statistics for the products
	 * 
	 * @param reviewResponse
	 *            The object to place the response in
	 * @param filters
	 *            The filters to use to get the results
	 */
	public void findReviews(ReviewResponse reviewResponse,
			Map<String, List<String>> filters, int pMaxResults) {
		try {
			List<String> states = filters.get(PARAM_REVIEW_SEARCH);			
			List<Review> results = new ArrayList<Review>();
			
			List<Review> tmpResults = mReviewDAO.findAllReviews(pMaxResults, ModerationStatus.valueOf(states.get(0)));
			
			List<String> comments = filters.get(PARAM_REVIEW_COMMENTS);
			if (tmpResults != null && tmpResults.size() > 0)
			{
				if (comments != null && comments.size() > 0)
				{
					Boolean include = Boolean.valueOf(comments.get(0));
					for (Review rvw : tmpResults)
					{
						if (rvw.getClientResponse() != null && (rvw.getClientResponse().size() > 0 == include))
						{
							results.add(rvw);
						}
					}
				}
				else
				{
					results.addAll(tmpResults);
				}
			}			
			
			reviewResponse.setResults(results);
			reviewResponse.setLimit(results.size());
			reviewResponse.setTotalResults(results.size());
		
		} catch (Exception e) {
			e.printStackTrace();
			reviewResponse
					.getProblems()
					.add(new Problem(
							e.getMessage()
									+ filters, "ERROR_BAD_REQUEST"));
		}
	}

	private void addMissingRatingsAndAverage(Product pProduct) {
		Double total = 0.0;
		for (int rating = 1; rating <= MAX_RATING; rating++) {
			boolean foundRating = false;
			for (RatingDistribution distribution : pProduct
					.getReviewStatistics().getRatingDistribution()) {
				if (distribution.getRatingValue() != null
						&& rating == distribution.getRatingValue()) {
					foundRating = true;
					total += distribution.getRatingValue()
							* distribution.getCount();
				}
			}
			if (!foundRating) {
				RatingDistribution missing = new RatingDistribution();
				missing.setRatingValue(rating);
				missing.setCount(0);
				pProduct.getReviewStatistics().getRatingDistribution()
						.add(missing);
			}
		}
		pProduct.getReviewStatistics().setAverageOverallRating(
				total / pProduct.getTotalReviewCount());
	}

	public boolean existsReview(Review review) {
		return mReviewDAO.existsReview(review);
	}
	
	public boolean existsReview(String reviewId) {
		Review review = mReviewDAO.getReview(reviewId);
		if(review != null)
			return true;
		else return false;
	}

	public boolean saveReview(Review review) {
		return mReviewDAO.saveReview(review);
	}

	public void setReviewDAO(ReviewDAO pReviewDAO) {
		this.mReviewDAO = pReviewDAO;
	}

	public boolean approveReviewWithComment(ReviewComment reviewComment,
			Boolean approved){
		if (reviewComment == null) return false;
		
		Review review = mReviewDAO.getReview(reviewComment.getReviewId());
		if (review == null) return false;
		
		if (approved)
			review.setModStatus(ModerationStatus.APPROVED);
		else
			review.setModStatus(ModerationStatus.REJECTED);
		reviewComment.setLastModificationTime(new Date());
		
		if (review.getClientResponse() != null) {
			if (review.getClientResponse().size() > 0) {
				ReviewComment oldComment = review.getClientResponse().iterator().next();
				if (oldComment != null) {
					// If the review has an existing comment we want to update
					// the comment or remove it if the new comment is empty
					if (reviewComment.getResponse() != null && !reviewComment.getResponse().trim().isEmpty()) {
						oldComment.setResponse(reviewComment.getResponse());
						oldComment.setName(reviewComment.getName());
					} else {
						review.getClientResponse().remove(oldComment);
					}
				}
			} else {
				//If no old comment we simply add the new one if any comment given
				if (reviewComment.getResponse() != null && !reviewComment.getResponse().trim().isEmpty()) {
					review.getClientResponse().add(reviewComment);
				}
			}
		}
		
		return mReviewDAO.updateReview(review);
	}
	
	public void removeAllComments(Review review)
	{
		mReviewDAO.deleteAllReviewComments(review.getClientResponse());
	}
	
	public void getSingleReview(ReviewResponse response, String reviewId)
	{
		response.getResults().add(mReviewDAO.getReview(reviewId));
	}
}
