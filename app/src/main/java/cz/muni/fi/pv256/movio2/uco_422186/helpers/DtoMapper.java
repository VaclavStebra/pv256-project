package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422186.models.Movie;

public class DtoMapper {
    public static Movie mapDTOToMovie(MovieDTO movieDTO) {
        return new Movie(TimeHelpers.getCurrentTime().getTime(), movieDTO.posterPath,
                movieDTO.title, movieDTO.backdropPath, movieDTO.popularity / 20, movieDTO.overview);
    }
}
