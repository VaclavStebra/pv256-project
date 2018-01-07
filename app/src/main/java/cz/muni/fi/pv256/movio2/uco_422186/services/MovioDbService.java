package cz.muni.fi.pv256.movio2.uco_422186.services;


import cz.muni.fi.pv256.movio2.uco_422186.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_422186.dto.APIResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovioDbService {

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY + "" +
            "&language=en-US" +
            "&sort_by=popularity.desc" +
            "&include_adult=false" +
            "&include_video=false" +
            "&with_release_type=2%7C3")
    Call<APIResult> theatreMovies(@Query("primary_release_date.gte") String startDate, @Query("primary_release_date.lte") String endDate);

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY + "" +
            "&language=en-US" +
            "&sort_by=popularity.desc" +
            "&include_adult=false" +
            "&include_video=false" +
            "&with_release_type=1")
    Call<APIResult> newMovies(@Query("primary_release_date.gte") String startDate, @Query("primary_release_date.lte") String endDate);
}
