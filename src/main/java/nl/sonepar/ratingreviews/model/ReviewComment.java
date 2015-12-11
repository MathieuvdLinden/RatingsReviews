package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Wilco on 07/01/2015. A response for a review by Technische Unie
 * @author Kamiel.
 */
@Entity
@Table(name = "rvw_response")
public class ReviewComment
{
  @Column(name="response_name")
  private String name;
  private String responseSource;
  
  @Transient
  private String[] responseMarkup = {};
  private String responseType;
  
  @Column(name="response_date")
  private Date date;
  private String response;
  private String department;
  
  @Transient
  private String reviewId;
  
  @Version
  private Date lastModificationTime;
  
  @Id
  private String id;
  
  @ManyToOne
  private Review review; 

  @JsonProperty("Name")
  public String getName()
  {
    return name;
  }

  public void setName(String pName)
  {
    name = pName;
  }

  @JsonProperty("ResponseSource")
  public String getResponseSource()
  {
    return responseSource;
  }

  public void setResponseSource(String pResponseSource)
  {
    responseSource = pResponseSource;
  }

  @JsonProperty("ResponseMarkup")
  public String[] getResponseMarkup()
  {
    return responseMarkup;
  }

  public void setResponseMarkup(String[] pResponseMarkup)
  {
    responseMarkup = pResponseMarkup;
  }

  @JsonProperty("ResponseType")
  public String getResponseType()
  {
    return responseType;
  }

  public void setResponseType(String pResponseType)
  {
    responseType = pResponseType;
  }

  @JsonProperty("Date")
  public String getDate()
  {
    return ModelUtils.safeDateFormat(date);
  }

  public void setDate(Date pDate)
  {
    date = pDate;
  }

  @JsonProperty("ReviewResponse")
  public String getResponse()
  {
    return response;
  }

  public void setResponse(String pResponse)
  {
    response = pResponse;
  }

  @JsonProperty("Department")
  public String getDepartment()
  {
    return department;
  }

  public void setDepartment(String pDepartment)
  {
    department = pDepartment;
  }

  public Date getLastModificationTime()
  {
    return lastModificationTime;
  }

  public void setLastModificationTime(Date lastModificationTime)
  {
    this.lastModificationTime = lastModificationTime;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getReviewId() 
  {
	return reviewId;
  }

  public void setReviewId(String reviewId) 
  {
	this.reviewId = reviewId;
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

  @JsonIgnore
  public Review getReview() 
  {
	return review;
  }
	
  public void setReview(Review review) 
  {
	this.review = review;
  }
}
