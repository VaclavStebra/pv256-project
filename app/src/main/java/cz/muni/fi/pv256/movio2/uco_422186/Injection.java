package cz.muni.fi.pv256.movio2.uco_422186;

import android.content.Context;

import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepository;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.MoviesRepositoryImpl;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.local.MoviesManager;
import cz.muni.fi.pv256.movio2.uco_422186.data.source.remote.MoviesRemoteDataSource;

public class Injection {
    public static MoviesRepository provideMoviesRepository(Context context) {
        return MoviesRepositoryImpl.getInstance(MoviesRemoteDataSource.getInstance(context),
                new MoviesManager(context));
    }
}
