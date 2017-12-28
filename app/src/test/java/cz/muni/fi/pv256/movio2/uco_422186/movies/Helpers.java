package cz.muni.fi.pv256.movio2.uco_422186.movies;

import org.joda.time.DateTime;

import cz.muni.fi.pv256.movio2.uco_422186.data.Movie;

public class Helpers {
    static Movie fakeMovie() {
        return new Movie(1, new DateTime().getMillis(), "", "", "", 1f ,"");
    }
}
