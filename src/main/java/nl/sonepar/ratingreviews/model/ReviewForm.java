package nl.sonepar.ratingreviews.model;

import javax.ws.rs.FormParam;
import java.util.Date;

/**
 * Created by Christian on 15-1-2015.
 */
public class ReviewForm
{
  @FormParam("ApiVersion")
  private String mApiVersion;
  @FormParam("Rating")
  private int mRating;
  @FormParam("Title")
  private String mTitle;
  @FormParam("ReviewText")
  private String mReviewText;
  @FormParam("UserNickname")
  private String mUserNickname;
  @FormParam("ProductId")
  private String mProductId;
  @FormParam("UserId")
  private String mUserId;


  public ReviewForm()
  {
    super();
  }

  public String getApiVersion()
  {
    return mApiVersion;
  }

  public Review getReview()
  {
    Review review;

    review = new Review();
    review.setTitle(mTitle);
    review.setRating(mRating);
    review.setReviewText(mReviewText);
    review.setUserNickname(mUserNickname);
    review.setProductId(mProductId);
    review.setAuthorId(mUserId);
    review.setSubmissionTime(new Date());
    review.setStatus();

    return review;
  }
}
