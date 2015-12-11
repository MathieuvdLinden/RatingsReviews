package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Represents errors in the request parameters with message (human readable) and code (for machine/developer)
 * Created by Wilco on 08/01/2015.
 */
public class Problem {
    private String mMessage;
    private String mCode;

    public Problem(String pMessage, String pCode) {
        this.mMessage = pMessage;
        this.mCode = pCode;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String pMessage) {
        mMessage = pMessage;
    }

    @JsonProperty("Code")
    public String getCode() {
        return mCode;
    }

    public void setCode(String pCode) {
        mCode = pCode;
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
