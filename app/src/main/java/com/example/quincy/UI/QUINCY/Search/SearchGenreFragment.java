package com.example.quincy.UI.QUINCY.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quincy.R;
import com.example.quincy.data.model.TagModel;
import com.example.quincy.viewmodel.MangaViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchGenreFragment extends Fragment {

    private MangaViewModel viewModel;
    private ChipGroup chipGroupGenre;
    private ChipGroup chipGroupStatus;
    private ProgressBar progressGenre;
    private View layoutRetry;

    public SearchGenreFragment() {
        super(R.layout.fragment_search_genre);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        chipGroupGenre = view.findViewById(R.id.chipGroupGenre);
        chipGroupStatus = view.findViewById(R.id.chipGroupStatus);
        progressGenre = view.findViewById(R.id.progressGenre);
        layoutRetry = view.findViewById(R.id.layoutRetry);

        viewModel = new ViewModelProvider(requireActivity()).get(MangaViewModel.class);

        for (int i = 0; i < chipGroupStatus.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupStatus.getChildAt(i);
            chip.setChecked(viewModel.getSelectedStatuses().contains((String) chip.getTag()));
        }

        view.findViewById(R.id.btnRetryGenre).setOnClickListener(v -> {
            showLoading();
            viewModel.reloadGenres();
        });

        showLoading();
        viewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            progressGenre.setVisibility(View.GONE);

            if (genres == null || genres.isEmpty()) {
                chipGroupGenre.setVisibility(View.GONE);
                layoutRetry.setVisibility(View.VISIBLE);
                return;
            }

            layoutRetry.setVisibility(View.GONE);
            chipGroupGenre.setVisibility(View.VISIBLE);
            buildChips(genres);
        });
    }

    private void showLoading() {
        progressGenre.setVisibility(View.VISIBLE);
        layoutRetry.setVisibility(View.GONE);
    }

    private void buildChips(List<TagModel> genres) {
        chipGroupGenre.removeAllViews();
        for (TagModel genre : genres) {
            Chip chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.chip_genre, chipGroupGenre, false);
            chip.setId(View.generateViewId());
            chip.setText(genre.getName());
            chip.setTag(genre.getId());
            chip.setChecked(viewModel.getSelectedGenreIds().contains(genre.getId()));
            chipGroupGenre.addView(chip);
        }
    }

    // simpan pilihan waktu halaman ini ditutup
    @Override
    public void onDestroyView() {
        // Status
        List<String> statuses = new ArrayList<>();
        for (int i = 0; i < chipGroupStatus.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupStatus.getChildAt(i);
            if (chip.isChecked()) statuses.add((String) chip.getTag());
        }
        viewModel.setSelectedStatuses(statuses);

        // Genre (kalau chip belum kebentuk, jangan timpa pilihan lama)
        if (chipGroupGenre.getChildCount() > 0) {
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < chipGroupGenre.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupGenre.getChildAt(i);
                if (chip.isChecked()) ids.add((String) chip.getTag());
            }
            viewModel.setSelectedGenreIds(ids);
        }
        super.onDestroyView();
    }
}