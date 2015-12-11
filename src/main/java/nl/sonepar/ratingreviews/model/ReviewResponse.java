package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * A representation of a full review response, which is a query for reviews (0 or more)
 * Created by Wilco on 07/01/2015.
 */
public class ReviewResponse extends Response
{
  int mOffset = 0;
  int mTotalResults = 0;
  List<Review> mResults = new ArrayList<>();
  int limit = 0;

  @Transient
  private Includes mIncludes;

  @JsonProperty("Offset")
  public int getOffset()
  {
    return mOffset;
  }

  public void setOffset(int pOffset)
  {
    mOffset = pOffset;
  }

  @JsonProperty("TotalResults")
  public int getTotalResults()
  {
    return mTotalResults;
  }

  public void setTotalResults(int pTotalResults)
  {
    mTotalResults = pTotalResults;
  }

  @JsonProperty("Results")
  public List<Review> getResults()
  {
    return mResults;
  }

  public void setResults(List<Review> pResults)
  {
    mResults = pResults;
  }

  @JsonProperty("Limit")
  public int getLimit()
  {
    return limit;
  }

  public void setLimit(int pLimit)
  {
    limit = pLimit;
  }


  @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
  @JsonProperty("Includes")
  public Includes getIncludes() {
    return mIncludes;
  }

  @JsonIgnore
  public Includes getOrCreateIncludes() {
    if (mIncludes == null) {
      mIncludes = new Includes();
    }
    return mIncludes;
  }
}
