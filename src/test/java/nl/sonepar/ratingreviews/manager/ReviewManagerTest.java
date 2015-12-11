package nl.sonepar.ratingreviews.manager;

import junit.framework.TestCase;
import nl.sonepar.ratingreviews.dao.ReviewDAO;
import nl.sonepar.ratingreviews.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Created by Christian on 19-1-2015.
 */
public class ReviewManagerTest extends TestCase
{
    private static final String PRODUCT_ID = "4384681";
    private static final int MAX_RESULTS = 25;

    private ReviewManager reviewManager;

    private List<Review> reviews;
    private List<Review> unApprovedReviews;
    private Map<String, Product> productMap;
    private List<String> productList;
    private ReviewComment reviewComment;

    @Mock
    private ReviewDAO mReviewDAO;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        reviewManager = new ReviewManager();
        reviewManager.setReviewDAO(mReviewDAO);

        this.setupReviews();
    }

    private void setupReviews()
    {
        reviews = new ArrayList<>();
        unApprovedReviews = new ArrayList<>();
        Review review = new Review();
        Review unApprovedReview = new Review();
        ReviewStatistics stats = new ReviewStatistics();
        Product product = new Product();
        productMap = new HashMap<>();
        productList = new ArrayList<>();
        List<RatingDistribution> ratings = new ArrayList<>();
        RatingDistribution rating1 = new RatingDistribution();
        RatingDistribution rating2 = new RatingDistribution();
        reviewComment = new ReviewComment();

        review.setTitle("Title");
        review.setReviewText("Review Text");
        review.setModStatus(ModerationStatus.PENDING_APPROVAL);
        reviews.add(review);

        unApprovedReview.setTitle("Title");
        unApprovedReview.setReviewText("Review Text");
        unApprovedReview.setModStatus(ModerationStatus.PENDING_APPROVAL);
        unApprovedReview.setRatingOnly(false);
        unApprovedReview.setClientResponse(new HashSet<ReviewComment>());
        unApprovedReviews.add(unApprovedReview);

        rating1.setCount(1);
        rating1.setRatingValue(3);
        rating2.setCount(1);
        rating2.setRatingValue(5);
        ratings.add(rating1);
        ratings.add(rating2);
        
        reviewComment.setName("Jantje piet");
        reviewComment.setId(review.getId());
        reviewComment.setResponse("This is the comment itself");
        reviewComment.setResponseSource("Marketing");

        product.setTotalReviewCount(2);
        stats.setRatingDistribution(ratings);
        product.setReviewStatistics(stats);

        productMap.put("4384681", product);
        productList.add("4384681");
    }

    @Test
    public void testGetReviewsForProduct()
    {
        ReviewResponse response = new ReviewResponse();

        when(mReviewDAO.findAllProductReviews("4384681", 25, true)).thenReturn(reviews);
        when(mReviewDAO.findCountsForProductList(productList)).thenReturn(productMap);

        reviewManager.getReviewsForProduct(response, "4384681", MAX_RESULTS, true);
        assertEquals(1, response.getLimit());
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getIncludes().getProducts().size());
        assertEquals(2, response.getIncludes().getProducts().get(PRODUCT_ID).getTotalReviewCount().intValue());

        ReviewStatistics responseStats = response.getIncludes().getProducts().get(PRODUCT_ID).getReviewStatistics();
        assertEquals(5, responseStats.getRatingDistribution().size());
        assertEquals(5, responseStats.getOverallRatingRange().intValue());
        assertEquals(4.0, responseStats.getAverageOverallRating().doubleValue());
    }

    @Test
    public void testGetUnapprovedReviews()
    {
        ReviewResponse response = new ReviewResponse();

        when(mReviewDAO.findAllUnapprovedReviews(25)).thenReturn(unApprovedReviews);

        reviewManager.getUnapprovedReviews(response, MAX_RESULTS);

        assertEquals(1, response.getTotalResults());
    }
    
    @Test
    public void testApproveReviewAndComment1()
    {
    	when(mReviewDAO.getReview(reviewComment.getId())).thenReturn(unApprovedReviews.get(0));
    	when(mReviewDAO.updateReview(unApprovedReviews.get(0))).thenReturn(true);
    	
    	reviewManager.approveReviewWithComment(reviewComment, true);
    	Review resultingReview = mReviewDAO.getReview(reviewComment.getId());
    	
    	assertEquals(resultingReview.getModStatus(), ModerationStatus.APPROVED);
    	assertTrue(resultingReview.getClientResponse().contains(reviewComment));
    }
    
    @Test
    public void testApproveReviewAndComment2()
    {
    	when(mReviewDAO.getReview(reviewComment.getId())).thenReturn(unApprovedReviews.get(0));
    	when(mReviewDAO.updateReview(unApprovedReviews.get(0))).thenReturn(true);
    	
    	reviewManager.approveReviewWithComment(reviewComment, false);
    	Review resultingReview = mReviewDAO.getReview(reviewComment.getId());
    	
    	assertEquals(resultingReview.getModStatus(), ModerationStatus.REJECTED);
    	assertTrue(resultingReview.getClientResponse().contains(reviewComment));
    }
}
