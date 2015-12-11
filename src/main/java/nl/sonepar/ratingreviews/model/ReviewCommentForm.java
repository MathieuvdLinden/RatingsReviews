package nl.sonepar.ratingreviews.model;

import javax.ws.rs.FormParam;

/**
 * Created by Kamiel on 28-01-2015.
 */
public class ReviewCommentForm
{
  @FormParam("apiversion")
  private String mApiVersion;
  @FormParam("passkey")
  private String mPasskey;
  @FormParam("name")
  private String mName;
  @FormParam("comment")
  private String mComment;
  @FormParam("reviewId")
  private String mReviewId;
  @FormParam("department")
  private String mDepartment;
  @FormParam("responseSource")
  private String mResponseSource;
  @FormParam("approved")
  private Boolean mApproved;


  public ReviewCommentForm()
  {
    super();
  }

  public String getApiVersion()
  {
    return mApiVersion;
  }
  
  public String getPassKey()
  {
	  return mPasskey;
  }
  
  public String getReviewId()
  {
      return mReviewId;
  }

  public ReviewComment getReviewComment()
  {
    ReviewComment reviewComment;

    reviewComment = new ReviewComment();
    reviewComment.setName(mName);
    reviewComment.setResponse(mComment);
    reviewComment.setReviewId(mReviewId);
    reviewComment.setDepartment(mDepartment);
    reviewComment.setResponseSource(mResponseSource);
    return reviewComment;
  }
  
  public Boolean isApproved()
  {
	  return mApproved;
  }
}
