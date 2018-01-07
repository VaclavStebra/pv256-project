package cz.muni.fi.pv256.movio2.uco_422186.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResult {
    @SerializedName("results")
    public List<MovieDTO> movies;
}
