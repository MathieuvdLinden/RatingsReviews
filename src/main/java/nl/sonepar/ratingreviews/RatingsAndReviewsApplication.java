package nl.sonepar.ratingreviews;

import nl.sonepar.ratingreviews.rest.RatingReviewService;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Application for ratings and reviews
 * Created by Wilco on 07/01/2015.
 */
public class RatingsAndReviewsApplication extends Application
{

  HashSet<Object> singletons = new HashSet<>();

  public RatingsAndReviewsApplication()
  {
    singletons.add(new RatingReviewService());
  }

  @Override
  public Set<Class<?>> getClasses()
  {
    return new HashSet<>();
  }

  @Override
  public Set<Object> getSingletons()
  {
    return singletons;
  }

}
