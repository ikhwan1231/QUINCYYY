
package com.example.quincy.UI.QUINCY.Adapter;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    private final List<MangaModel> mangaList;
    private final Map<String, String> chapterOverrides = new HashMap<>();

    public MangaAdapter(List<MangaModel> mangaList) {
        this.mangaList = mangaList;
    }

    // Memperbarui nomor chapter dari API
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

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manga, parent, false);

        return new MangaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull MangaViewHolder holder, int position) {

        MangaModel manga = mangaList.get(position);

        // 1. Set judul manga
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

        // 2. Rating sementara
        holder.tvRating.setText("9.0");

        // 3. Set nomor chapter
        String chapter = chapterOverrides.get(manga.getId());

        if (chapter == null && manga.getAttributes() != null) {
            chapter = manga.getAttributes().getLastChapter();
        }

        if (chapter != null && !chapter.trim().isEmpty()) {
            holder.tvChapter.setText("Ch. " + chapter);
        } else {
            holder.tvChapter.setText("Chapter belum tersedia");
        }

        // 4. Set gambar cover
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
                    R.drawable.cover_card_manga);
        }
    }

    @Override
    public int getItemCount() {
        return mangaList != null ? mangaList.size() : 0;
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvRating, tvChapter;
        ImageView imgCover;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            imgCover = itemView.findViewById(R.id.imgCover);
        }
    }
}
