package nl.sonepar.ratingreviews.model;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * Created by Wilco on 07/01/2015. DTO to store a single review
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "rvw_review")
public class Review // extends BaseJSONDTO Avoid inheritance, it breaks eBean
{

  private int rating;
  private boolean isRatingOnly;

  @Version
  private Date lastModificationTime;

  private String productId;
  private String campaignId;
  private Date submissionTime;
  @Transient
  private int ratingRange;
  @Transient
  private int totalCommentCount;
  private String authorId;
  private String userNickname;
  private String title;
  @Column(length = 4000)
  private String reviewText;
  
  @OneToMany(cascade=CascadeType.ALL)
  private Set<ReviewComment> clientResponse;
  private ModerationStatus modStatus;
  private Date lastModeratedTime;

  @Id
  private String id;

  @JsonProperty("Rating")
  public int getRating()
  {
    return rating;
  }

  public void setRating(int pRating)
  {
    rating = pRating;
  }

  @JsonProperty("RatingOnly")
  public boolean isRatingOnly()
  {
    return isRatingOnly;
  }

  public void setRatingOnly(boolean pIsRatingOnly)
  {
    isRatingOnly = pIsRatingOnly;
  }

  @JsonProperty("LastModificationTime")
  public String getLastModificationTime()
  {
    return ModelUtils.safeDateFormat(lastModificationTime);
  }

  public void setLastModificationTime(Date pLastModificationTime)
  {
    lastModificationTime = pLastModificationTime;
  }

  @JsonProperty("ProductId")
  public String getProductId()
  {
    return productId;
  }

  public void setProductId(String pProductId)
  {
    productId = pProductId;
  }

  @JsonProperty("CampaignId")
  public String getCampaignId()
  {
    return campaignId;
  }

  public void setCampaignId(String pCampaignId)
  {
    campaignId = pCampaignId;
  }

  @JsonProperty("SubmissionTime")
  public String getSubmissionTime()
  {
    return ModelUtils.safeDateFormat(submissionTime);
  }

  public void setSubmissionTime(Date pSubmissionTime)
  {
    submissionTime = pSubmissionTime;
  }

  @JsonProperty("RatingRange")
  public int getRatingRange()
  {
    return ratingRange;
  }

  public void setRatingRange(int pRatingRange)
  {
    ratingRange = pRatingRange;
  }

  @JsonProperty("TotalCommentCount")
  public int getTotalCommentCount()
  {
    return totalCommentCount;
  }

  public void setTotalCommentCount(int pTotalCommentCount)
  {
    totalCommentCount = pTotalCommentCount;
  }

  @JsonProperty("AuthorId")
  public String getAuthorId()
  {
    return authorId;
  }

  public void setAuthorId(String pAuthorId)
  {
    authorId = pAuthorId;
  }

  @JsonProperty("Title")
  public String getTitle()
  {
    return title;
  }

  public void setTitle(String pTitle)
  {
    title = pTitle;
  }

  @JsonProperty("ReviewText")
  public String getReviewText()
  {
    return reviewText;
  }

  public void setReviewText(String pReviewText)
  {
    reviewText = pReviewText;
  }

  @JsonProperty("ModerationStatus")
  public ModerationStatus getModStatus()
  {
    return modStatus;
  }

  public void setModStatus(ModerationStatus pModStatus)
  {
    modStatus = pModStatus;
  }

  @JsonProperty("LastModeratedTime")
  public String getLastModeratedTime()
  {
    return ModelUtils.safeDateFormat(lastModeratedTime);
  }

  public void setLastModeratedTime(Date pLastModeratedTime)
  {
    lastModeratedTime = pLastModeratedTime;
  }

  @JsonProperty("Id")
  public String getId()
  {
    return id;
  }

  public void setId(String pId)
  {
    id = pId;
  }

  public Set<ReviewComment> getClientResponse()
  {
    return clientResponse;
  }

  public void setClientResponse(Set<ReviewComment> clientResponse)
  {
    this.clientResponse = clientResponse;
  }

  @JsonProperty("UserNickname")
  public String getUserNickname()
  {
    return userNickname;
  }

  public void setUserNickname(String userNickname)
  {
    this.userNickname = userNickname;
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

  /**
   * Set the status of the review.
   * If no title and review text is supplied then the status is 'APPROVED' else the
   * status is set to 'PENDING APPROVAL'
   */
  public void setStatus()
  {

    if (StringUtils.isBlank(title) && StringUtils.isBlank(reviewText))
    {
      setModStatus(ModerationStatus.APPROVED);
    }
    else
    {
      setModStatus(ModerationStatus.PENDING_APPROVAL);
    }
  }
}
