package com.example.quincy.UI.QUINCY.Search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quincy.R;
import com.example.quincy.UI.QUINCY.Adapter.MangaAdapter;
import com.example.quincy.viewmodel.MangaViewModel;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    private static final long DEBOUNCE_MS = 500;
    private static final int PAGE_LIMIT = 20;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private int requestId = 0;

    private EditText etSearch;
    private TextView tvJumlahHasil;
    private MangaAdapter searchAdapter;
    private MangaViewModel mangaViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mangaViewModel = new ViewModelProvider(requireActivity()).get(MangaViewModel.class);

        etSearch = view.findViewById(R.id.etSearch);
        tvJumlahHasil = view.findViewById(R.id.tvJumlahHasil);
        ImageView btnClear = view.findViewById(R.id.btnClear);
        ImageView ivGenreLogo = view.findViewById(R.id.ivGenreLogo);
        RecyclerView rvSearch = view.findViewById(R.id.rvSearch);

        // Card horizontal
        searchAdapter = new MangaAdapter(new ArrayList<>(), true);
        rvSearch.setAdapter(searchAdapter);

        // Klik logo genre
        ivGenreLogo.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SearchGenreFragment())
                .addToBackStack(null)
                .commit());

        // Tombol clear
        btnClear.setOnClickListener(v -> etSearch.setText(""));

        // Search dengan debounce
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                String query = s.toString().trim();

                if (query.isEmpty() && !mangaViewModel.hasActiveFilter()) {
                    requestId++; // batalin hasil request yang masih jalan
                    searchAdapter.setMangaList(null);
                    tvJumlahHasil.setText(R.string.total_pencarian);
                    return;
                }

                searchRunnable = () -> doSearch(query);
                handler.postDelayed(searchRunnable, DEBOUNCE_MS);
            }
        });

        if (mangaViewModel.hasActiveFilter()) {
            doSearch(etSearch.getText().toString().trim());
        }
    }

    private void doSearch(String query) {
        final int currentId = ++requestId;

        // judul kosong dikirim null (filter genre/status saja)
        String title = query.isEmpty() ? null : query;

        mangaViewModel.searchManga(title, PAGE_LIMIT, 0)
                .observe(getViewLifecycleOwner(), list -> {
                    if (currentId != requestId) return;

                    searchAdapter.setMangaList(list);

                    int total = list != null ? list.size() : 0;
                    tvJumlahHasil.setText(getString(R.string.total_pencarian) + ": " + total);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}