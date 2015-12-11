package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * The product represent a single product (including any review statistics)
 * Created by gebruiker on 08/01/2015.
 */
public class Product
{

  private Integer mTotalReviewCount = 0;
  private String mId;
  private ReviewStatistics mReviewStatistics = new ReviewStatistics();


  @JsonProperty("Id")
  public String getId()
  {
    return mId;
  }

  public void setId(String pId)
  {
    mId = pId;
  }
  @JsonProperty("TotalReviewCount")
  public Integer getTotalReviewCount()
  {
    return mTotalReviewCount;
  }

  public void setTotalReviewCount(Integer pTotalReviewCount)
  {
    mTotalReviewCount = pTotalReviewCount;
  }

  @JsonProperty("ReviewStatistics")
  public ReviewStatistics getReviewStatistics()
  {
    return mReviewStatistics;
  }

  public void setReviewStatistics(ReviewStatistics pReviewStatistics)
  {
    mReviewStatistics = pReviewStatistics;
  }

  @Override
  public String toString()
  {
    String result;
    try
    {
      result = new ObjectMapper().writeValueAsString(this);
    }
    catch (IOException ignored)
    {
      result = "{ error: " + super.toString() + " }";
    }
    return result;
  }

}
