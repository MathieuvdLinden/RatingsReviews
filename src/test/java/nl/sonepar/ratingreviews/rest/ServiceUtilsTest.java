package nl.sonepar.ratingreviews.rest;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 19-1-2015.
 */
public class ServiceUtilsTest extends TestCase
{
    private static final String PRODUCT_ID_1 = "4384681";
    private static final String PRODUCT_ID_2 = "4384682";
    private static final String PROD_LABEL = "product";


    @Test
    public void testParseMultiValueParam_null()
    {
        assertEquals(0, ServiceUtils.parseMultiValueParam(null).size());
    }

    @Test
    public void testParseMultiValueParam_empty_string()
    {
        assertEquals(0, ServiceUtils.parseMultiValueParam("").size());
    }

    @Test
    public void testParseMultiValueParam_one_colon()
    {
        assertEquals(0, ServiceUtils.parseMultiValueParam("colon:").size());
    }

    @Test
    public void testParseMultiValueParam_three_colons()
    {
        assertEquals(1, ServiceUtils.parseMultiValueParam("colon:colon:").size());
    }

    @Test
    public void testParseMultiValueParam_object_creation()
    {
        assertNotNull(new ServiceUtils());
    }

    @Test
    public void testParseMultiValueParam()
    {
        Map<String, List<String>> result;

        result = ServiceUtils.parseMultiValueParam("product:4384681");
        assertEquals(1, result.size());
        assertEquals(1, result.get(PROD_LABEL).size());

        result = ServiceUtils.parseMultiValueParam("product:4384681,4384682");
        assertEquals(1, result.size());
        assertEquals(2, result.get(PROD_LABEL).size());
        assertEquals(PRODUCT_ID_1, result.get(PROD_LABEL).get(0));
        assertEquals(PRODUCT_ID_2, result.get(PROD_LABEL).get(1));
    }
}