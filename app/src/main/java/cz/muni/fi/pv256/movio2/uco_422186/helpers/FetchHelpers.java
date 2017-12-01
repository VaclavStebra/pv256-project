package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cz.muni.fi.pv256.movio2.uco_422186.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;

public class FetchHelpers {
    public static String getBaseUrl() {
        return "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "" +
                "&language=en-US" +
                "&sort_by=popularity.desc" +
                "&include_adult=false" +
                "&include_video=false";
    }

    public static APIResult parseResponse(Response response) throws IOException, JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(response.body().string(), APIResult.class);
    }
}
