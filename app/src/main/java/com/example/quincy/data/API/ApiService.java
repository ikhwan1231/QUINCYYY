package com.example.quincy.data.API;

import com.example.quincy.data.model.MangaResponse;
import com.example.quincy.data.model.ChapterResponse;
import com.example.quincy.data.model.TagResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Latest Manga e
    @GET("manga")
    Call<MangaResponse> getLatestManga(
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("includes[]") String includeCover,
            @Query("order[latestUploadedChapter]") String orderLatest
    );

    // Most Popular Manga e
    @GET("manga")
    Call<MangaResponse> getPopularManga(
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("includes[]") String includeCover,
            @Query("order[followedCount]") String orderPopular
    );

    // ngambil daftar chapter manga e
    @GET("manga/{id}/feed")
    Call<ChapterResponse> getMangaChapters(
            @Path("id") String mangaId,
            @Query("limit") int limit,
            @Query("translatedLanguage[]") String language,
            @Query("order[chapter]") String order
    );

    // ngambil rating manga e
    @GET("statistics/manga/{id}")
    Call<ResponseBody> getMangaStatistics(
            @Path("id") String mangaId
    );

    // Cari manga berdasarkan judul + filter genre dan status
    @GET("manga")
    Call<MangaResponse> searchManga(
            @Query("title") String title,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("includes[]") String includeCover,
            @Query("order[relevance]") String orderRelevance,
            @Query("includedTags[]") List<String> includedTags,
            @Query("includedTagsMode") String includedTagsMode,
            @Query("status[]") List<String> statuses
    );

    // daftar tag (genre, tema, dll)
    @GET("manga/tag")
    Call<TagResponse> getTags();
}
