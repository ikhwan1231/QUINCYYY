
package com.example.quincy.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChapterResponse {

    @SerializedName("data")
    private List<ChapterModel> data;

    public List<ChapterModel> getData() {
        return data;
    }

    public static class ChapterModel {

        @SerializedName("attributes")
        private ChapterAttributes attributes;

        public ChapterAttributes getAttributes() {
            return attributes;
        }
    }

    public static class ChapterAttributes {

        @SerializedName("chapter")
        private String chapter;

        public String getChapter() {
            return chapter;
        }
    }
}
