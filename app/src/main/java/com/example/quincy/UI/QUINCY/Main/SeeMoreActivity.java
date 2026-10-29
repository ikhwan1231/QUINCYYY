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
import com.example.quincy.viewmodel.MangaViewModel;

public class SeeMoreActivity extends AppCompatActivity {

    private RecyclerView rvSeeMoreGrid;
    private TextView tvToolbarTitle;
    private ImageView btnBack;
    private MangaViewModel mangaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        rvSeeMoreGrid = findViewById(R.id.rvSeeMoreGrid);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnBack = findViewById(R.id.btnBack);

        // Ambil data kategori yang dikirim dari FragmentHome
        String categoryType = getIntent().getStringExtra("CATEGORY_TYPE");

        // Set Grid Layout 3 Kolom
        rvSeeMoreGrid.setLayoutManager(new GridLayoutManager(this, 3));

        // Inisialisasi MangaViewModel
        mangaViewModel = new ViewModelProvider(this).get(MangaViewModel.class);

        // Ubah teks judul header & ambil data dari API sesuai kategori yang diklik
        if ("LATEST".equals(categoryType)) {
            tvToolbarTitle.setText("Latest Manga");

            // Ambil data Latest Manga (misal 20 data)
            mangaViewModel.getLatestMangaList(20, 0).observe(this, mangaList -> {
                if (mangaList != null && !mangaList.isEmpty()) {
                    MangaAdapter adapter = new MangaAdapter(mangaList);
                    rvSeeMoreGrid.setAdapter(adapter);
                }
            });

        } else if ("POPULAR".equals(categoryType)) {
            tvToolbarTitle.setText("Most Popular");

            // Ambil data Most Popular Manga (misal 20 data)
            mangaViewModel.getPopularMangaList(20, 0).observe(this, mangaList -> {
                if (mangaList != null && !mangaList.isEmpty()) {
                    MangaAdapter adapter = new MangaAdapter(mangaList);
                    rvSeeMoreGrid.setAdapter(adapter);
                }
            });
        }

        // Tombol Back untuk kembali ke halaman sebelumnya
        btnBack.setOnClickListener(v -> finish());
    }
}