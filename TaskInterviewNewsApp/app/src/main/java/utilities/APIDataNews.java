package utilities;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Bruce on 01/02/2019.
 */

public interface APIDataNews {

    @GET("top-headlines")
    retrofit2.Call<JsonObject> getNews(@Query("country") String country, @Query("category") String category
            , @Query("apiKey") String apiKey);

}
