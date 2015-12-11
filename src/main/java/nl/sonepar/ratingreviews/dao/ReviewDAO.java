package nl.sonepar.ratingreviews.dao;

import com.avaje.ebean.*;
import com.avaje.ebean.annotation.Transactional;
import nl.sonepar.ratingreviews.model.ModerationStatus;
import nl.sonepar.ratingreviews.model.Product;
import nl.sonepar.ratingreviews.model.RatingDistribution;
import nl.sonepar.ratingreviews.model.Review;
import nl.sonepar.ratingreviews.model.ReviewComment;
import nl.sonepar.ratingreviews.model.ReviewStatistics;
import org.jboss.resteasy.logging.Logger;

import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Review manager provides the business logic for manipulating reviews.
 *
 * Created by Wilco on 08/01/2015.
 */
public class ReviewDAO
{
  private static final Logger log = Logger.getLogger(ReviewDAO.class);

  /**
   * Find all reviews for the given product id
   * 
   * @param pProductId
   *          The list of products for which to find the reviews
   * @param pMaxRows
   *          The maximum number of results to retrieve.
   * @return The list of reviews found for the products
   */
  public List<Review> findAllProductReviews(String pProductId, int pMaxRows, boolean pApprovedOnly)
  {
	if (pApprovedOnly)
	{
	    return Ebean.find(Review.class).where().eq("product_id", pProductId).eq("mod_status", ModerationStatus.APPROVED)
	            .setMaxRows(pMaxRows).orderBy("submission_time desc").findList();		
	}
	else
	{
	    return Ebean.find(Review.class).where().eq("product_id", pProductId).setMaxRows(pMaxRows).orderBy("submission_time desc").findList();		
	}
  }

  /**
   * Find all unapproved reviews
   *
   * @param pMaxRows
   *          The maximum numbers of reviews to retrieve
   * @return The list of reviews that are not approved
   */
  @NotNull
  public List<Review> findAllUnapprovedReviews(int pMaxRows)
  {
    return Ebean.find(Review.class).where().eq("mod_status", ModerationStatus.PENDING_APPROVAL)
        .setMaxRows(pMaxRows).orderBy("submission_time asc").findList();
  }
  
  /**
   * Search for reviews
   *
   * @param pMaxRows
   *          The maximum numbers of reviews to retrieve
   * @return The list of reviews that are not approved
   */
  @NotNull
  public List<Review> findAllReviews(int pMaxRows, ModerationStatus pStatus)
  {
    return Ebean.find(Review.class).where().eq("mod_status", pStatus).isNotNull("title").isNotNull("review_text").setMaxRows(pMaxRows).orderBy("submission_time desc").findList();
  }
  
   
  
  

  /**
   * Check if the review exists.
   *
   * @param review
   * @return True if the review exists otherwise return false
   */
  public boolean existsReview(Review review)
  {
    return Ebean.find(Review.class).where().eq("product_id", review.getProductId())
        .eq("author_id", review.getAuthorId()).findUnique() != null;
  }

  /**
   * Get a specific review
   *
   * @param reviewId
   * @return A review
   */
  public Review getReview(String reviewId)
  {
    //return Ebean.find(Review.class).where().eq("id", reviewId).findUnique();
    return Ebean.find(Review.class, reviewId);
  }
  
  /**
   * Save the review
   * 
   * @param review
   *          The review to persist
   * @return True is the review is saved correctly
   */
  @Transactional
  public boolean saveReview(Review review)
  {
    boolean persisted = false;

    try
    {
      Ebean.save(review);
      persisted = true;
    }
    catch (PersistenceException e)
    {
      log.error("An error occured when saving review", e);
    }

    return persisted;
  }
  
  /**
   * Update the review
   * 
   * @param review
   *          The review to persist
   * @return True is the review is saved correctly
   */
  @Transactional
  public boolean updateReview(Review review)
  {
    boolean persisted = false;

    try
    {
      Ebean.update(review);
      persisted = true;
    }
    catch (PersistenceException e)
    {
      log.error("An error occured when saving review", e);
    }

    return persisted;
  }
  
  /**
   * Save the review comment
   * 
   * @param reviewComment
   *          The review comment to persist
   * @return True if the review comment is saved correctly
   */
  @Transactional
  public boolean saveReviewComment(ReviewComment reviewComment)
  {
    boolean persisted = false;

    try
    {
      Ebean.save(reviewComment);
      persisted = true;
    }
    catch (PersistenceException e)
    {
      log.error("An error occured when saving review comment", e);
    }

    return persisted;
  }
  
  /**
   * Delete all comments, NOT FUNCTIONAL YET!
   * 
   * @param reviewComments
   *          A list of comments on a review to delete
   * @return True if the review comment is saved correctly
   */
  @Transactional
  public boolean deleteAllReviewComments(Set<ReviewComment> reviewComments)
  {
    boolean persisted = false;

    try
    {
      Ebean.delete(reviewComments);
//      while (reviewComments.iterator().hasNext())
//      {
//    	 reviewComments.iterator().next()
//      }
      persisted = true;
    }
    catch (PersistenceException e)
    {
      log.error("An error occured at deleting review comments", e);
    }

    return persisted;
  }

  /**
   * Find all the rating and their counts for a specific product, to be used in
   * statistics
   *
   * @param pProducts
   *          The product id's for which stats should be fetched
   * @return A map of all available ratings to their count. Unavailable ratings
   *         are omitted from the map
   */
  public Map<String, Product> findCountsForProductList(List<String> pProducts)
  {
    String sql = "SELECT\n" + "  product_id                     AS productId,\n"
        + "  rating                         AS rating,\n" + "  count(*)                       AS count\n"
        + "FROM rvw_review\n" + "WHERE product_id IN (:prodList) AND mod_status = 0\n"
        + "GROUP BY (product_id, rating)";

    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
    sqlQuery.setParameter("prodList", pProducts);
    List<SqlRow> list = sqlQuery.findList();

    Map<String, Product> productStats = new HashMap<>();
    if (list != null)
    {
      for (SqlRow row : list)
      {
        String productId = row.getString("productid");
        Integer rating = row.getInteger("rating");
        Integer count = row.getInteger("count");

        Product product;
        if (!productStats.containsKey(productId))
        {
          product = new Product();
          product.setId(productId);
          product.setReviewStatistics(new ReviewStatistics());
          productStats.put(productId, product);
        }
        else
        {
          product = productStats.get(productId);
        }
        RatingDistribution distribution = new RatingDistribution();
        distribution.setCount(count);
        distribution.setRatingValue(rating);
        product.getReviewStatistics().getRatingDistribution().add(distribution);
        product.setTotalReviewCount(product.getTotalReviewCount() + count);
        product.getReviewStatistics().setTotalReviewCount(product.getTotalReviewCount());
      }
    }
    return productStats;
  }
}
