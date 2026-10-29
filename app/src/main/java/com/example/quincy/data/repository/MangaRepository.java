

package com.example.quincy.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quincy.data.API.ApiService;
import com.example.quincy.data.model.MangaModel;
import com.example.quincy.data.model.MangaResponse;
import com.example.quincy.data.model.ChapterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaRepository {

    private final ApiService apiService;

    public MangaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // Mengambil Latest Manga
    public LiveData<List<MangaModel>> getLatestMangaList(
            int limit, int offset) {

        MutableLiveData<List<MangaModel>> liveData =
                new MutableLiveData<>();

        apiService.getLatestManga(
                limit, offset, "cover_art", "desc"
        ).enqueue(new Callback<MangaResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MangaResponse> call,
                    @NonNull Response<MangaResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {
                    liveData.setValue(response.body().getData());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MangaResponse> call,
                    @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // Mengambil Most Popular Manga
    public LiveData<List<MangaModel>> getPopularMangaList(
            int limit, int offset) {

        MutableLiveData<List<MangaModel>> liveData =
                new MutableLiveData<>();

        apiService.getPopularManga(
                limit, offset, "cover_art", "desc"
        ).enqueue(new Callback<MangaResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MangaResponse> call,
                    @NonNull Response<MangaResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {
                    liveData.setValue(response.body().getData());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MangaResponse> call,
                    @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // Mengambil nomor chapter dari endpoint MangaDex
    public LiveData<String> getLatestChapter(String mangaId) {

        MutableLiveData<String> liveData = new MutableLiveData<>();

        apiService.getMangaChapters(
                mangaId, 1, "en", "desc"
        ).enqueue(new Callback<ChapterResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<ChapterResponse> call,
                    @NonNull Response<ChapterResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getData() != null
                        && !response.body().getData().isEmpty()
                        && response.body().getData().get(0).getAttributes() != null) {

                    String chapter = response.body().getData()
                            .get(0).getAttributes().getChapter();

                    if (chapter != null && !chapter.trim().isEmpty()) {
                        liveData.setValue(chapter);
                    } else {
                        liveData.setValue(null);
                    }

                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ChapterResponse> call,
                    @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
