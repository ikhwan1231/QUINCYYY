package com.example.quincy.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quincy.data.API.ApiService;
import com.example.quincy.data.model.MangaModel;
import com.example.quincy.data.model.MangaResponse;
import com.example.quincy.data.model.ChapterResponse;
import com.example.quincy.data.model.TagModel;
import com.example.quincy.data.model.TagResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaRepository {

    private final ApiService apiService;

    public MangaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // Mengambil Latest Manga e
    public LiveData<List<MangaModel>> getLatestMangaList(int limit, int offset) {
        MutableLiveData<List<MangaModel>> liveData = new MutableLiveData<>();

        apiService.getLatestManga(limit, offset, "cover_art", "desc").enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().getData());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // Mengambil Most Popular Manga e
    public LiveData<List<MangaModel>> getPopularMangaList(int limit, int offset) {
        MutableLiveData<List<MangaModel>> liveData = new MutableLiveData<>();

        apiService.getPopularManga(limit, offset, "cover_art", "desc").enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().getData());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // Ngambil Nomor Chapter Dari Manga
    public LiveData<String> getLatestChapter(String mangaId) {
        MutableLiveData<String> liveData = new MutableLiveData<>();

        apiService.getMangaChapters(mangaId, 1, "en", "desc").enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChapterResponse> call, @NonNull Response<ChapterResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getData() != null
                        && !response.body().getData().isEmpty()
                        && response.body().getData().get(0).getAttributes() != null) {

                    String chapter = response.body().getData().get(0).getAttributes().getChapter();
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
            public void onFailure(@NonNull Call<ChapterResponse> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    //Responbody buat Rating Manga
    public LiveData<String> getMangaRating(String mangaId) {
        MutableLiveData<String> liveData = new MutableLiveData<>();

        apiService.getMangaStatistics(mangaId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        // Parsing  mengambil nilai bayesian rating
                        if (jsonString.contains(mangaId) && jsonString.contains("bayesian")) {
                            int bayesianIndex = jsonString.indexOf("bayesian");
                            int colonIndex = jsonString.indexOf(":", bayesianIndex);
                            int commaIndex = jsonString.indexOf(",", colonIndex);
                            if (commaIndex == -1) {
                                commaIndex = jsonString.indexOf("}", colonIndex);
                            }
                            String ratingVal = jsonString.substring(colonIndex + 1, commaIndex).trim();
                            double bayesian = Double.parseDouble(ratingVal);
                            liveData.setValue(String.format(java.util.Locale.US, "%.2f", bayesian));
                            return;
                        }
                        liveData.setValue("N/A");
                    } catch (Exception e) {
                        liveData.setValue("N/A");
                    }
                } else {
                    liveData.setValue("N/A");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                liveData.setValue("N/A");
            }
        });

        return liveData;
    }

    // Cari manga berdasarkan judul + filter genre dan status
    public LiveData<List<MangaModel>> searchManga(String title, List<String> genreIds,
                                                  List<String> statuses, int limit, int offset) {
        MutableLiveData<List<MangaModel>> liveData = new MutableLiveData<>();

        apiService.searchManga(title, limit, offset, "cover_art", "desc",
                genreIds, "AND", statuses).enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaResponse> call, @NonNull Response<MangaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().getData());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaResponse> call, @NonNull Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // Ambil genre dari API (group == "genre")
    public void fetchGenres(MutableLiveData<List<TagModel>> target) {
        apiService.getTags().enqueue(new Callback<TagResponse>() {
            @Override
            public void onResponse(@NonNull Call<TagResponse> call, @NonNull Response<TagResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getData() != null) {
                    List<TagModel> genres = new ArrayList<>();
                    for (TagModel tag : response.body().getData()) {
                        if ("genre".equals(tag.getGroup()) && tag.getName() != null) {
                            genres.add(tag);
                        }
                    }
                    target.setValue(genres);
                } else {
                    target.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TagResponse> call, @NonNull Throwable t) {
                target.setValue(null);
            }
        });
    }
}
