package cz.muni.fi.pv256.movio2.uco_422186.dto;

import com.google.gson.annotations.SerializedName;

public class MovieDTO {
    @SerializedName("title")
    public String title;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("popularity")
    public float popularity;
    @SerializedName("overview")
    public String overview;
}
