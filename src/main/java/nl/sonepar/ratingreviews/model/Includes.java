package nl.sonepar.ratingreviews.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Includes holds the products with their statistics and other 'additional' information associated with reviews
 *
 * Created by Wilco on 08/01/2015.
 */
public class Includes {

    private Map<String, Product> mProducts = new LinkedHashMap<>();

    @JsonProperty("Products")
    public Map<String,Product> getProducts() {
        return mProducts;
    }

    public void setProducts(Map<String, Product> pProducts) {
        mProducts.clear();
        mProducts.putAll(pProducts);
    }

    @JsonProperty("ProductsOrder")
    public List<String> getProductsOrder() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, Product> entry: mProducts.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
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
