package cz.muni.fi.pv256.movio2.uco_422186.data.source.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "cz.muni.fi.pv256.movio2.uco_422186.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final String DATE_FORMAT = "yyyyMMddHHmm";

    public static String getDbDateString(Date date) {
        DateTime yodaDate = new DateTime(date);
        return yodaDate.toString(DATE_FORMAT);
    }

    public static Date getDateFromDb(String dateText) {
        return DateTime.parse(dateText, DateTimeFormat.forPattern(DATE_FORMAT).withOffsetParsed()).toDate();
    }

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_COVER_PATH = "cover_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
