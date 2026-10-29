package com.example.quincy.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MangaResponse {
    @SerializedName("result")
    private String result;

    @SerializedName("data")
    private List<MangaModel> data;

    public String getResult() {
        return result;
    }

    public List<MangaModel> getData() {
        return data;
    }
}