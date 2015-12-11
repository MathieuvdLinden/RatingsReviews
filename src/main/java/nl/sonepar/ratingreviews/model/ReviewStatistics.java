package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Holder for statistics of a single product's reviews
 * Created by Wilco on 08/01/2015.
 */
public class ReviewStatistics {

    private List<RatingDistribution> mRatingDistribution = new ArrayList<>();
    private Integer mOverallRatingRange = 5;
    private Double mAverageOverallRating;
    private Integer mTotalReviewCount;

    @JsonProperty("RatingDistribution")
    public List<RatingDistribution> getRatingDistribution() {
        return mRatingDistribution;
    }

    public void setRatingDistribution(List<RatingDistribution> pRatingDistribution) {
        mRatingDistribution = pRatingDistribution;
    }

    @JsonProperty("OverallRatingRange")
    public Integer getOverallRatingRange() {
        return mOverallRatingRange;
    }

    public void setOverallRatingRange(Integer pOverallRatingRange) {
        mOverallRatingRange = pOverallRatingRange;
    }

    @JsonProperty("AverageOverallRating")
    public Double getAverageOverallRating() {
        return mAverageOverallRating;
    }

    public void setAverageOverallRating(Double pAverageOverallRating) {
        mAverageOverallRating = pAverageOverallRating;
    }

    @JsonProperty("TotalReviewCount")
    public Integer getTotalReviewCount() {
        return mTotalReviewCount;
    }

    public void setTotalReviewCount(Integer pTotalReviewCount) {
        mTotalReviewCount = pTotalReviewCount;
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

