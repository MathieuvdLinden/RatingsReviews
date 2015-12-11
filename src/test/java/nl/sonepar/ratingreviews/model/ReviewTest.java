package nl.sonepar.ratingreviews.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.GregorianCalendar;

/**
 * Created by Christian on 19-1-2015.
 */
public class ReviewTest extends TestCase
{
    private static final String REVIEW_TEXT = "Review Text";
    private static final String TITLE = "Title";
    private static final ModerationStatus STATUS_APPROVED = ModerationStatus.APPROVED;

    @Test
    public void testProperties()
    {
        GregorianCalendar calendar = new GregorianCalendar(2015, 1, 18, 22, 10, 11);
        Review review = new Review();

        review.setModStatus(ModerationStatus.APPROVED);
        assertEquals(STATUS_APPROVED, review.getModStatus());
        review.setSubmissionTime(calendar.getTime());
        assertEquals("2015-02-18T22:10:11.000+01:00", review.getSubmissionTime());
        review.setTitle("Title");
        assertSame(TITLE, review.getTitle());
        review.setReviewText("Review Text");
        assertSame(REVIEW_TEXT, review.getReviewText());
    }

    @Test
    public void testSetStatus()
    {
        Review review = new Review();

        review.setTitle("Title");
        review.setStatus();
        assertEquals(ModerationStatus.PENDING_APPROVAL, review.getModStatus());

        review.setReviewText("Title");
        review.setStatus();
        assertEquals(ModerationStatus.PENDING_APPROVAL, review.getModStatus());

        review.setTitle("  ");
        review.setReviewText("  ");
        review.setStatus();
        assertEquals(ModerationStatus.APPROVED, review.getModStatus());
    }
}
