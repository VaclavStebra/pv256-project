package cz.muni.fi.pv256.movio2.uco_422186.services;

import android.content.Context;
import android.support.annotation.NonNull;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.helpers.DtoMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchMovieService extends FetchService {

    public FetchMovieService(Context context) {
        super(context);
    }

    public void fetch(final Movie originalMovie) {
        MovioDbService service = buildService();
        Call<MovieDTO> call = service.movie(originalMovie.getId());
        call.enqueue(new Callback<MovieDTO>() {

            @Override
            public void onResponse(@NonNull Call<MovieDTO> call, @NonNull Response<MovieDTO> response) {
                MovieDTO result = response.body();
                // For some reason Movie API returns popularity - 1 as for discover API
                result.popularity++;
                Movie movie = DtoMapper.mapDTOToMovie(result);
                if (isUpdated(originalMovie, movie)) {
                    notifyActivityOnMovieUpdate(movie);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDTO> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private boolean isUpdated(Movie originalMovie, Movie movie) {
        if (originalMovie.getReleaseDate() != movie.getReleaseDate()) {
            return true;
        }

        if (originalMovie.getCoverPath() != null &&
                !originalMovie.getCoverPath().equals(movie.getCoverPath())) {
            return true;
        }

        if (originalMovie.getTitle() != null &&
                !originalMovie.getTitle().equals(movie.getTitle())) {
            return true;
        }

        if (originalMovie.getBackdrop() != null &&
                !originalMovie.getBackdrop().equals(movie.getBackdrop())) {
            return true;
        }

        if (originalMovie.getPopularity() != movie.getPopularity()) {
            return true;
        }

        if (originalMovie.getOverview() != null) {
            return !originalMovie.getOverview().equals(movie.getOverview());
        }

        return movie.getOverview() != null;
    }

}
