package com.example.quincy.UI.QUINCY.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quincy.R;
import com.example.quincy.data.model.MangaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    private final List<MangaModel> mangaList;
    private final boolean horizontal;
    private final Map<String, String> chapterOverrides = new HashMap<>();
    private final Map<String, String> ratingOverrides = new HashMap<>();

    //konstruktor lama tetap ada, otomatis mode vertikal (Home, SeeMore)
    public MangaAdapter(List<MangaModel> mangaList) {
        this(mangaList, false);
    }

    //konstruktor dengan pilihan horizontal (dipakai FragmentSearch)
    public MangaAdapter(List<MangaModel> mangaList, boolean horizontal) {
        this.horizontal = horizontal;
        this.mangaList = new ArrayList<>();

        if (mangaList != null) {
            this.mangaList.addAll(mangaList);
        }
    }

    //ganti seluruh isi list (dipakai hasil search)
    @SuppressLint("NotifyDataSetChanged")
    public void setMangaList(List<MangaModel> newList) {
        mangaList.clear();

        if (newList != null) {
            mangaList.addAll(newList);
        }

        notifyDataSetChanged();
    }

    // Infinite Scroll buat bagian See More Activity
    public void addMangaList(List<MangaModel> newMangaList) {

        if (newMangaList == null || newMangaList.isEmpty()) {
            return;
        }

        int startPosition = mangaList.size();

        mangaList.addAll(newMangaList);

        notifyItemRangeInserted(
                startPosition,
                newMangaList.size()
        );
    }

    // update no chapter
    public void setChapterForManga(String mangaId, String chapter) {

        if (mangaId == null || chapter == null
                || chapter.trim().isEmpty()) {
            return;
        }

        chapterOverrides.put(mangaId, chapter);

        for (int i = 0; i < mangaList.size(); i++) {

            MangaModel manga = mangaList.get(i);

            if (mangaId.equals(manga.getId())) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    // set rating API
    public void setRatingForManga(String mangaId, String rating) {
        if (mangaId == null || rating == null || rating.trim().isEmpty()) {
            return;
        }

        ratingOverrides.put(mangaId, rating);

        for (int i = 0; i < mangaList.size(); i++) {
            MangaModel manga = mangaList.get(i);
            if (mangaId.equals(manga.getId())) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // pilih layout sesuai mode
        int layoutId = horizontal
                ? R.layout.item_manga_horizontal
                : R.layout.item_manga;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new MangaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull MangaViewHolder holder, int position) {

        MangaModel manga = mangaList.get(position);

        // 1. Set judul manga e
        String title = "No Title";

        if (manga.getAttributes() != null) {

            String mangaTitle =
                    manga.getAttributes().getEnglishTitle();

            if (mangaTitle != null
                    && !mangaTitle.trim().isEmpty()) {
                title = mangaTitle;
            }
        }

        holder.tvTitle.setText(title);


        if (holder.tvDescription != null) {
            String desc = manga.getAttributes() != null
                    ? manga.getAttributes().getDescriptionText() : null;
            holder.tvDescription.setText(desc != null ? desc : "");
        }

        // 2. Set Rating e
        String rating = ratingOverrides.get(manga.getId());
        if (rating != null && !rating.trim().isEmpty()) {
            holder.tvRating.setText(rating);
        } else {
            holder.tvRating.setText("N/A");
        }

        // 3. Set nomor chapter e
        String chapter = chapterOverrides.get(manga.getId());

        if (chapter == null && manga.getAttributes() != null) {
            chapter = manga.getAttributes().getLastChapter();
        }

        if (chapter != null && !chapter.trim().isEmpty()) {
            holder.tvChapter.setText("Ch. " + chapter);
        } else {
            holder.tvChapter.setText("Ch. -");
        }

        // 4.Status (Ongoing / Completed / Hiatus)
        String status = manga.getAttributes() != null ? manga.getAttributes().getStatus() : "ongoing";
        if (status == null) status = "ongoing";

        switch (status.toLowerCase()) {
            case "completed":
                holder.tvStatus.setText("Completed");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#43A047")); //iki hijau
                holder.tvStatus.setVisibility(View.VISIBLE);
                break;
            case "hiatus":
                holder.tvStatus.setText("Hiatus");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E53935")); // iki merah
                holder.tvStatus.setVisibility(View.VISIBLE);
                break;
            case "ongoing":
            default:
                holder.tvStatus.setText("Ongoing");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#fa4d14")); //oren  werno iki
                holder.tvStatus.setVisibility(View.VISIBLE);
                break;
        }

        // 5. Set gambar cover e
        String coverUrl = manga.getCoverUrl();

        if (coverUrl != null && !coverUrl.trim().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(coverUrl)
                    .placeholder(R.drawable.cover_card_manga)
                    .error(R.drawable.cover_card_manga)
                    .into(holder.imgCover);

        } else {

            Glide.with(holder.itemView.getContext())
                    .clear(holder.imgCover);

            holder.imgCover.setImageResource(
                    R.drawable.cover_card_manga
            );
        }
    }

    @Override
    public int getItemCount() {
        return mangaList != null ? mangaList.size() : 0;
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvRating, tvChapter, tvStatus, tvDescription;
        ImageView imgCover;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvDescription = itemView.findViewById(R.id.tvDescription); // [BARU] null di item_manga
        }
    }
}