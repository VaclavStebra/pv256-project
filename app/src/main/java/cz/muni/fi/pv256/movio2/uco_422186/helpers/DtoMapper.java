package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;
import cz.muni.fi.pv256.movio2.uco_422186.dto.MovieDTO;

public class DtoMapper {
    public static Movie mapDTOToMovie(MovieDTO movieDTO) {
        return new Movie(movieDTO.id, TimeHelpers.getReleaseDate(movieDTO.releaseDate), movieDTO.posterPath,
                movieDTO.title, movieDTO.backdropPath, movieDTO.popularity, movieDTO.overview);
    }
}
