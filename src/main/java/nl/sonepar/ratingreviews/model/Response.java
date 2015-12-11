package nl.sonepar.ratingreviews.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Generic response type.
 * Created by Kamiel on 02/02/2015.
 */
public abstract class Response {

	List<Problem> mProblems = new ArrayList<>();
	String mLocale = "nl_NL";

	@JsonProperty("HasErrors")
	public boolean isHasErrors() {
		return !getProblems().isEmpty();
	}

	@JsonProperty("Errors")
	public List<Problem> getProblems() {
		return mProblems;
	}

	@JsonProperty("Locale")
	public String getLocale() {
		return mLocale;
	}

	public void setLocale(String pLocale) {
		mLocale = pLocale;
	}

	@Override
	public String toString() {
		String result;
		try {
			result = new ObjectMapper().writeValueAsString(this);
		} catch (IOException ignored) {
			result = "{ error: " + super.toString() + " }";
		}
		return result;
	}
}
