package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import cz.muni.fi.pv256.movio2.uco_422186.BuildConfig;

public class FetchHelpers {
    public static String getBaseUrl() {
        return "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY + "" +
                "&language=en-US" +
                "&sort_by=popularity.desc" +
                "&include_adult=false" +
                "&include_video=false";
    }
}
