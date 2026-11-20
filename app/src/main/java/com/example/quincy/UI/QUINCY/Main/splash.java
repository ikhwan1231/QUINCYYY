package com.example.quincy.UI.QUINCY.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quincy.R;
public class splash extends AppCompatActivity {
    private ImageView imageViewLogo;
    private TextView textViewQuincy, textViewTagline, textViewTagSupport;
    private ProgressBar progressBarSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // penhubung kabeh material e
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewQuincy = findViewById(R.id.textViewQuincy);
        textViewTagline = findViewById(R.id.textViewTagline);
        textViewTagSupport = findViewById(R.id.textViewTagSupport);
        progressBarSplash = findViewById(R.id.progressBarSplash);

        // ngehide smua elemen di splash
        imageViewLogo.setVisibility(View.INVISIBLE);
        textViewQuincy.setVisibility(View.INVISIBLE);
        textViewTagline.setVisibility(View.INVISIBLE);
        textViewTagSupport.setVisibility(View.INVISIBLE);
        progressBarSplash.setVisibility(View.INVISIBLE);

        // buat animasi e
        Animation slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade);
        Animation fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // 1. logo ke atass e
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            imageViewLogo.setVisibility(View.VISIBLE);
            imageViewLogo.startAnimation(slideUpAnim);
        }, 300);

        // 2. teks judul quincy e
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            textViewQuincy.setVisibility(View.VISIBLE);
            textViewQuincy.startAnimation(fadeInAnim);

            textViewTagline.setVisibility(View.VISIBLE);
            textViewTagline.startAnimation(fadeInAnim);
        }, 800);

        // 3. loading bar dan teks mangadex e
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            textViewTagSupport.setVisibility(View.VISIBLE);
            textViewTagSupport.startAnimation(fadeInAnim);

            progressBarSplash.setVisibility(View.VISIBLE);
            progressBarSplash.startAnimation(fadeInAnim);
        }, 1200);

        // 4. langsung ke home, 3000 delay milis iku = 3 detik
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}