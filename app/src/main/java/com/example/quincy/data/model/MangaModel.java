package com.example.quincy.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class MangaModel {

    @SerializedName("id")
    private String id;

    @SerializedName("attributes")
    private Attributes attributes;

    @SerializedName("relationships")
    private List<Relationship> relationships;

    public String getId() {
        return id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    // Mngambil nama file cover
    public String getCoverFileName() {
        if (relationships == null) {
            return null;
        }

        for (Relationship rel : relationships) {
            if ("cover_art".equals(rel.getType())
                    && rel.getAttributes() != null) {

                return rel.getAttributes().getFileName();
            }
        }

        return null;
    }

    //  URL cover MangaDex
    public String getCoverUrl() {
        String fileName = getCoverFileName();

        if (fileName != null && id != null) {
            return "https://uploads.mangadex.org/covers/"
                    + id + "/" + fileName + ".256.jpg";
        }

        return null;
    }


    public static class Attributes {

        @SerializedName("title")
        private Map<String, String> title;

        @SerializedName("description")
        private Map<String, String> description;

        @SerializedName("lastChapter")
        private String lastChapter;

        @SerializedName("status")
        private String status;

        private String rating = "N/A";

        public Map<String, String> getTitle() {
            return title;
        }

        public Map<String, String> getDescription() {
            return description;
        }

        public String getEnglishTitle() {
            if (title != null) {
                if (title.containsKey("en")
                        && title.get("en") != null
                        && !title.get("en").trim().isEmpty()) {

                    return title.get("en");
                }

                for (String value : title.values()) {
                    if (value != null && !value.trim().isEmpty()) {
                        return value;
                    }
                }
            }

            return "No Title";
        }

        public String getDescriptionText() {
            if (description != null) {
                String en = description.get("en");
                if (en != null && !en.trim().isEmpty()) {
                    return en.trim();
                }

                for (String value : description.values()) {
                    if (value != null && !value.trim().isEmpty()) {
                        return value.trim();
                    }
                }
            }

            return "";
        }

        public String getLastChapter() {
            return lastChapter;
        }

        public String getStatus() {
            return status != null ? status : "ongoing";
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }

    // Relasi manga, termasuk cover art e
    public static class Relationship {

        @SerializedName("id")
        private String id;

        @SerializedName("type")
        private String type;

        @SerializedName("attributes")
        private CoverAttributes attributes;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public CoverAttributes getAttributes() {
            return attributes;
        }
    }

    // atribut file cover
    public static class CoverAttributes {

        @SerializedName("fileName")
        private String fileName;

        public String getFileName() {
            return fileName;
        }
    }
}