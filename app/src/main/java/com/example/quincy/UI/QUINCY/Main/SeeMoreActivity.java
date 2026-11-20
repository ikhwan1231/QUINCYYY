package com.example.quincy.UI.QUINCY.Main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quincy.R;
import com.example.quincy.UI.QUINCY.Adapter.MangaAdapter;
import com.example.quincy.data.model.MangaModel;
import com.example.quincy.viewmodel.MangaViewModel;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreActivity extends AppCompatActivity {
    private RecyclerView rvSeeMoreGrid;
    private TextView tvToolbarTitle;
    private ImageView btnBack;
    private MangaViewModel mangaViewModel;
    private MangaAdapter mangaAdapter;
    private GridLayoutManager gridLayoutManager;
    private static final int PAGE_SIZE = 20;
    private int offset = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        rvSeeMoreGrid = findViewById(R.id.rvSeeMoreGrid);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnBack = findViewById(R.id.btnBack);
        categoryType = getIntent().getStringExtra("CATEGORY_TYPE");
        btnBack.setOnClickListener(v -> finish());
        gridLayoutManager = new GridLayoutManager(this, 3);
        rvSeeMoreGrid.setLayoutManager(gridLayoutManager);
        mangaAdapter = new MangaAdapter(new ArrayList<>());
        rvSeeMoreGrid.setAdapter(mangaAdapter);

        mangaViewModel = new ViewModelProvider(this)
                .get(MangaViewModel.class);

        if ("LATEST".equals(categoryType)) {
            tvToolbarTitle.setText("Latest Manga");
        } else if ("POPULAR".equals(categoryType)) {
            tvToolbarTitle.setText("Most Popular");
        } else {
            tvToolbarTitle.setText("Manga");
        }

        // Load halaman pertama e
        loadNextPage();

        // Infinite Scroll Listener e
        rvSeeMoreGrid.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(
                            RecyclerView recyclerView,
                            int dx,
                            int dy) {

                        super.onScrolled(recyclerView, dx, dy);

                        if (dy <= 0 || isLoading || isLastPage) {
                            return;
                        }
                        int visibleItemCount =
                                gridLayoutManager.getChildCount();
                        int totalItemCount =
                                gridLayoutManager.getItemCount();
                        int firstVisibleItem =
                                gridLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + firstVisibleItem)
                                >= totalItemCount - 5) {
                            loadNextPage();
                        }
                    }
                });
    }

    private void loadNextPage() {

        if (isLoading || isLastPage) {
            return;
        }

        isLoading = true;

        if ("LATEST".equals(categoryType)) {

            mangaViewModel.getLatestMangaList(PAGE_SIZE, offset)
                    .observe(this, mangaList -> {
                        handleMangaResponse(mangaList);
                    });

        } else if ("POPULAR".equals(categoryType)) {

            mangaViewModel.getPopularMangaList(PAGE_SIZE, offset)
                    .observe(this, mangaList -> {
                        handleMangaResponse(mangaList);
                    });

        } else {
            isLoading = false;
            isLastPage = true;
        }
    }

    private void handleMangaResponse(List<MangaModel> mangaList) {
        isLoading = false;
        if (mangaList == null) {
            return;
        }
        if (mangaList.isEmpty()) {
            isLastPage = true;
            return;
        }

        // menambahkan data ke adapter
        mangaAdapter.addMangaList(mangaList);

        // Update offset sesuai jumlah data manga yg diterima
        offset += mangaList.size();

        // muat chapter manga e klo ada sg baru
        for (MangaModel manga : mangaList) {
            if (manga.getId() == null) {
                continue;
            }

            // Fetch Chapter e
            mangaViewModel.getLatestChapter(manga.getId())
                    .observe(this, chapter -> {
                        if (chapter != null
                                && !chapter.trim().isEmpty()) {
                            mangaAdapter.setChapterForManga(
                                    manga.getId(),
                                    chapter
                            );
                        }
                    });

            // fecth rating e
            mangaViewModel.getMangaRating(manga.getId())
                    .observe(this, rating -> {
                        if (rating != null
                                && !rating.trim().isEmpty()) {
                            mangaAdapter.setRatingForManga(
                                    manga.getId(),
                                    rating
                            );
                        }
                    });
        }
        if (mangaList.size() < PAGE_SIZE) {
            isLastPage = true;
        }
    }
}