package nl.sonepar.ratingreviews.model;

/**
 * Enumeration of moderation status, which is the state in which a review / rating can be. Should only be modified by
 * Technische Unie (review) or system (rating)
 * TODO check values with BazaarVoice
 * Created by Wilco on 07/01/2015.
 */
public enum ModerationStatus
{
  APPROVED, PENDING_APPROVAL, REJECTED
}
