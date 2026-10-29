
package com.example.quincy.UI.QUINCY.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quincy.R;
import com.example.quincy.UI.QUINCY.Adapter.MangaAdapter;
import com.example.quincy.UI.QUINCY.Main.SeeMoreActivity;
import com.example.quincy.data.model.MangaModel;
import com.example.quincy.viewmodel.MangaViewModel;

import java.util.List;

public class FragmentHome extends Fragment {

    private RecyclerView rvLatestManga, rvMostPopular;
    private TextView tvSeeMoreLatest, tvSeeMorePopular;
    private MangaViewModel mangaViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        rvLatestManga = view.findViewById(R.id.rvLatestManga);
        rvMostPopular = view.findViewById(R.id.rvMostPopular);
        tvSeeMoreLatest = view.findViewById(R.id.tvSeeMoreLatest);
        tvSeeMorePopular = view.findViewById(R.id.tvSeeMorePopular);

        rvLatestManga.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));

        rvMostPopular.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));

        mangaViewModel = new ViewModelProvider(this)
                .get(MangaViewModel.class);

        // Latest Manga
        mangaViewModel.getLatestMangaList(10, 0)
                .observe(getViewLifecycleOwner(), mangaList -> {

                    if (mangaList != null) {
                        MangaAdapter adapter =
                                new MangaAdapter(mangaList);

                        rvLatestManga.setAdapter(adapter);

                        loadChapters(mangaList, adapter);
                    }
                });

        // Most Popular Manga
        mangaViewModel.getPopularMangaList(10, 0)
                .observe(getViewLifecycleOwner(), mangaList -> {

                    if (mangaList != null) {
                        MangaAdapter adapter =
                                new MangaAdapter(mangaList);

                        rvMostPopular.setAdapter(adapter);

                        loadChapters(mangaList, adapter);
                    }
                });

        // See More Latest
        tvSeeMoreLatest.setOnClickListener(v -> {
            Intent intent = new Intent(
                    requireContext(), SeeMoreActivity.class);

            intent.putExtra("CATEGORY_TYPE", "LATEST");
            startActivity(intent);
        });

        // See More Popular
        tvSeeMorePopular.setOnClickListener(v -> {
            Intent intent = new Intent(
                    requireContext(), SeeMoreActivity.class);

            intent.putExtra("CATEGORY_TYPE", "POPULAR");
            startActivity(intent);
        });
    }

    // Memuat nomor chapter untuk setiap manga
    private void loadChapters(
            List<MangaModel> mangaList,
            MangaAdapter adapter) {

        for (MangaModel manga : mangaList) {

            if (manga.getId() == null) {
                continue;
            }

            mangaViewModel.getLatestChapter(manga.getId())
                    .observe(getViewLifecycleOwner(), chapter -> {

                        if (chapter != null && !chapter.trim().isEmpty()) {
                            adapter.setChapterForManga(
                                    manga.getId(), chapter);
                        }
                    });
        }
    }
}
