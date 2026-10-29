
package com.example.quincy.data.API;

import com.example.quincy.data.model.MangaResponse;
import com.example.quincy.data.model.ChapterResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Latest Manga
    @GET("manga")
    Call<MangaResponse> getLatestManga(
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("includes[]") String includeCover,
            @Query("order[latestUploadedChapter]") String orderLatest
    );

    // Most Popular Manga
    @GET("manga")
    Call<MangaResponse> getPopularManga(
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("includes[]") String includeCover,
            @Query("order[followedCount]") String orderPopular
    );

    // Mengambil daftar chapter manga
    @GET("manga/{id}/feed")
    Call<ChapterResponse> getMangaChapters(
            @Path("id") String mangaId,
            @Query("limit") int limit,
            @Query("translatedLanguage[]") String language,
            @Query("order[chapter]") String order
    );
}
