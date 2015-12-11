package nl.sonepar.ratingreviews.rest;

import nl.sonepar.ratingreviews.manager.ReviewManager;
import nl.sonepar.ratingreviews.model.*;

import org.jboss.resteasy.logging.Logger;
import org.jboss.resteasy.annotations.Form;

import javax.ws.rs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * APIs for ratings and reviews, modeled to mimic BazaarVoice Created by Wilco
 * on 07/01/2015.
 */
@Path("/")
public class RatingReviewService
{
  private static final String ADMINPASSKEY = "sfkljbnwre01344jkbhv65uh1";	

  Logger log = Logger.getLogger(RatingReviewService.class);

  ReviewManager mReviewManager = new ReviewManager();

  @GET
  @Produces("application/json")
  @Path("reviews.json")
  public ReviewResponse getReviews(@QueryParam("apiversion") String pApiVersion,
      @QueryParam("passkey") String pPassKey, @QueryParam("Filter") String pFilter,
      @QueryParam("Include") String pInclude, @QueryParam("Sort") String pSort, @QueryParam("Stats") String pStats,
      @QueryParam("Limit") int pLimit)
  {
    ReviewResponse reviewResponse = new ReviewResponse();
    checkApiVersion(reviewResponse, pApiVersion);

    if (pStats != null)
    {    	
      switch (pStats)
      {
      case "Reviews":
        Map<String, List<String>> filters = ServiceUtils.parseMultiValueParam(pFilter);
        mReviewManager.statisticsForProducts(reviewResponse, filters);
        break;
      default:
      }
    }
    else
    {
      Map<String, List<String>> filters = ServiceUtils.parseMultiValueParam(pFilter);
      if (filters.containsKey(ReviewManager.PARAM_PRODUCT_ID))
      {
        if (filters.size() == 1)
        {
          int maxResults = 25;
          if (pLimit != 0)
          {
            maxResults = pLimit;
          }
          boolean approvedOnly = true;
          if (pPassKey.equals(ADMINPASSKEY)) {
        	  //If the call is coming from the admin pages we want to return all states and not only the approved ones
        	  approvedOnly = false;
          }
          mReviewManager.getReviewsForProduct(reviewResponse, filters.get(ReviewManager.PARAM_PRODUCT_ID).get(0),
              maxResults, approvedOnly);
        }
        else
        {
          log.info("Invalid number of products in request: " + filters.size());
        }
      }
      else if (pFilter.equals("REVIEW_UNAPPROVED"))
      {
        int maxResults = 100;
        if (pLimit != 0)
        {
          maxResults = pLimit;
        }
        if (pPassKey.equals(ADMINPASSKEY)) {
        	mReviewManager.getUnapprovedReviews(reviewResponse, maxResults);        	
        } else {
	    	reviewResponse.getProblems().add(new Problem("400 - invalid passkey given.", "ERROR_ACCESS_DENIED"));
	    }        
      }
      else if (filters.containsKey(ReviewManager.PARAM_REVIEW_SEARCH))
      {
	    if (pPassKey.equals(ADMINPASSKEY))
	    {
	        int maxResults = 100;
	        if (pLimit != 0)
	        {
	          maxResults = pLimit;
	        }
	    	List<String> inclarg = new ArrayList<String>();
	    	if (pInclude != null) {
	    		if (pInclude.equalsIgnoreCase(ReviewManager.PARAM_REVIEW_COMMENTS)) {
		    		inclarg.add(Boolean.TRUE.toString());
		    	} else {
		    		inclarg.add(Boolean.FALSE.toString());
		    	}	    	
		    	filters.put(ReviewManager.PARAM_REVIEW_COMMENTS, inclarg);
	    	}
	    	mReviewManager.findReviews(reviewResponse, filters, maxResults);	    	
	    }
	    else
	    {
	    	reviewResponse.getProblems().add(new Problem("400 - invalid passkey given.", "ERROR_ACCESS_DENIED"));
	    }        
      }      
      else if (filters.containsKey(ReviewManager.PARAM_SINGLE_REVIEW))
      {
    	  mReviewManager.getSingleReview(reviewResponse, filters.get(ReviewManager.PARAM_SINGLE_REVIEW).get(0));
      }
      else
      {
        log.error("No products in request: " + pFilter);
        reviewResponse.getProblems().add(new Problem("400 - No products in request", "ERROR_UNKNOWN"));
      }
    }

    return reviewResponse;
  }

  @POST
  @Produces("application/json")
  @Path("submitreview.json")
  public ReviewSubmitResponse submitReview(@Form ReviewForm form)
  {
    ReviewSubmitResponse reviewSubmitResponse = new ReviewSubmitResponse();

    checkApiVersion(reviewSubmitResponse, form.getApiVersion());

    Review review = form.getReview();

    if (!mReviewManager.existsReview(review))
    {
      mReviewManager.saveReview(review);
    }
    else
    {
      reviewSubmitResponse.getProblems().add(
          new Problem("400 - Duplicate review for product: " + form.getReview().getProductId() + "by: "
              + form.getReview().getUserNickname(), "ERROR_DUPLICATE_SUBMISSION"));
    }

    return reviewSubmitResponse;
    }
  
  @POST
  @Produces("application/json")
  @Path("submitreviewcomment.json")
  public ReviewCommentResponse submitReviewComment(@Form ReviewCommentForm form)
  {
	ReviewCommentResponse reviewCommentResponse = new ReviewCommentResponse();

    ReviewComment reviewComment = form.getReviewComment();
    
    checkApiVersion(reviewCommentResponse, form.getApiVersion());

    if (!form.getPassKey().equals(ADMINPASSKEY))
    {
    	reviewCommentResponse.getProblems().add(new Problem("400 - invalid passkey given.", "ERROR_ACCESS_DENIED"));
    }
    else if (form.isApproved() == null)
    {
    	reviewCommentResponse.getProblems().add(new Problem("400 - Property missing: 'Approved'.", "ERROR_FORM_REQUIRED"));
    }
    else if (!mReviewManager.existsReview(reviewComment.getReviewId()))    
    {
    	reviewCommentResponse.getProblems().add(new Problem("400 - Review not found.", "ERROR_PARAM_INVALID_SUBJECT_ID"));
    }
    else if (!mReviewManager.approveReviewWithComment(reviewComment, form.isApproved()))
    {
    	reviewCommentResponse.getProblems().add(new Problem("500 - Saving review(comment) failed.", "ERROR_UNKNOWN"));
   	}

    return reviewCommentResponse;
  }
  
  private boolean checkApiVersion(Response response, String apiVersion)
  {
	  if(!"5.4".equals(apiVersion))
	  {
		  log.info("Expected version 5.4, got: " + apiVersion);
		  response.getProblems().add(new Problem("400 - Expected version 5.4, got: " + apiVersion, "WARNING_BAD_VERSION"));
		  return false;
	  }
	  return true;
  }
}
