package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Distribution of a single rating
 * Created by Wilco on 08/01/2015.
 */
public class RatingDistribution {
    private Integer mCount;
    private Integer mRatingValue;

    @JsonProperty("Count")
    public Integer getCount() {
        return mCount;
    }

    public void setCount(Integer pCount) {
        mCount = pCount;
    }

    @JsonProperty("RatingValue")
    public Integer getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(Integer pRatingValue) {
        mRatingValue = pRatingValue;
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
