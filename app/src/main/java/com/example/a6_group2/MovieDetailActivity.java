package com.example.a6_group2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView detailPoster;
    private TextView detailTitle;
    private TextView detailOverview;
    private TextView detailReleaseDate;
    private TextView detailRating;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "0b6f4c7785d802fc2b2916c70df15f67";

    private TMDBApi tmdbApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // 初始化控件
        detailPoster = findViewById(R.id.detail_poster);
        detailTitle = findViewById(R.id.detail_title);
        detailOverview = findViewById(R.id.detail_overview);
        detailReleaseDate = findViewById(R.id.detail_release_date);
        detailRating = findViewById(R.id.detail_rating);

        // 获取传递的 movieId
        int movieId = getIntent().getIntExtra("movieId", -1);
        if (movieId == -1) {
            Toast.makeText(this, "id error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 初始化 Retrofit 和 API 接口
        setupRetrofit();

        // 调用接口获取电影详情
        fetchMovieDetails(movieId);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdbApi = retrofit.create(TMDBApi.class);
    }

    private void fetchMovieDetails(int movieId) {
        tmdbApi.getMovieDetails(movieId, API_KEY)
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI(response.body());
                        } else {
                            Toast.makeText(MovieDetailActivity.this, "failed to get movie detail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetail> call, Throwable t) {
                        Toast.makeText(MovieDetailActivity.this, "network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(MovieDetail movieDetail) {
        detailTitle.setText(movieDetail.getTitle());
        detailOverview.setText(movieDetail.getOverview());
        detailReleaseDate.setText("release date: " + movieDetail.getReleaseDate());
        detailRating.setText("rating: " + movieDetail.getVoteAverage());

        if (movieDetail.getPosterPath() != null) {
            String posterUrl = IMAGE_BASE_URL + movieDetail.getPosterPath();
            Glide.with(this)
                    .load(posterUrl)
                    .into(detailPoster);
        }
    }
}
