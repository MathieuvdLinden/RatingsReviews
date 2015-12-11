package nl.sonepar.ratingreviews.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.GregorianCalendar;

/**
 * Created by gebruiker on 20-1-2015.
 */
public class ModelUtilsTest extends TestCase
{

  @Test
  public void testSafeDateFormat_null()
  {
    assertSame("", ModelUtils.safeDateFormat(null));
  }

    @Test
    public void testSafeDateFormat()
    {
        GregorianCalendar calendar = new GregorianCalendar(2015, 1, 28,23, 55, 12);
        assertEquals("2015-02-28T23:55:12.000+01:00", ModelUtils.safeDateFormat(calendar.getTime()));
    }
}
