package com.example.a6_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceActivity extends AppCompatActivity {
    private static final String API_KEY = "0b6f4c7785d802fc2b2916c70df15f67";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private Spinner movie_genre, movie_cnt;
    private Button sendRequestButton;
    private LinearLayout movieContainer;
    private TextView loadingText;
    private TMDBApi tmdbApi;
    private Map<String, Integer> genreMap;
    private int selectedCount = 1;
    private int selectedGenreId = 28; // Default to Action
    private Handler handler = new Handler();
    private int dotCount = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webservice);

        initializeViews();
        setupRetrofit();
        setupGenreMap();
        setupSpinners();
        setupSendRequestButton();
    }

    private void initializeViews() {
        movie_genre = findViewById(R.id.genres_dropdown);
        movie_cnt = findViewById(R.id.movie_cnt);
        sendRequestButton = findViewById(R.id.send_request);
        movieContainer = findViewById(R.id.movie_poster_container);
        loadingText = findViewById(R.id.loading_text);
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tmdbApi = retrofit.create(TMDBApi.class);
    }

    private void setupGenreMap() {
        genreMap = new HashMap<>();
        genreMap.put("Action", 28);
        genreMap.put("Adventure", 12);
        genreMap.put("Animation", 16);
        genreMap.put("Comedy", 35);
        genreMap.put("Crime", 80);
        genreMap.put("Documentary", 99);
        genreMap.put("Drama", 18);
        genreMap.put("Family", 10751);
        genreMap.put("Fantasy", 14);
        genreMap.put("History", 36);
        genreMap.put("Horror", 27);
        genreMap.put("Music", 10402);
        genreMap.put("Mystery", 9648);
        genreMap.put("Romance", 10749);
        genreMap.put("Science Fiction", 878);
        genreMap.put("TV Movie", 10770);
        genreMap.put("Thriller", 53);
        genreMap.put("War", 10752);
        genreMap.put("Western", 37);
    }

    private void setupSpinners() {
        // Movie genre spinner
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(
                this, R.array.movie_genres, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movie_genre.setAdapter(genreAdapter);
        movie_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = parent.getItemAtPosition(position).toString();
                selectedGenreId = genreMap.get(selectedGenre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Movie count spinner
        String[] counts = {"1", "3", "5"};
        ArrayAdapter<String> countAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, counts);
        countAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movie_cnt.setAdapter(countAdapter);
        movie_cnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCount = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSendRequestButton() {
        sendRequestButton.setOnClickListener(v -> fetchMovies());
    }

    private void startLoadingAnimation() {
        isLoading = true;
        loadingText.setVisibility(View.VISIBLE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isLoading) return;

                StringBuilder dots = new StringBuilder();
                for (int i = 0; i < dotCount; i++) dots.append(".");
                loadingText.setText("Loading" + dots);
                dotCount = (dotCount + 1) % 4;

                handler.postDelayed(this, 500);
            }
        });
    }

    private void stopLoadingAnimation() {
        isLoading = false;
        loadingText.setVisibility(View.GONE);
    }

    private void fetchMovies() {
        movieContainer.removeAllViews();
        startLoadingAnimation();

        tmdbApi.getMoviesByGenre(API_KEY, selectedGenreId, "popularity.desc")
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        stopLoadingAnimation();
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body().getResults();
                            displayMovies(movies.subList(0, Math.min(selectedCount, movies.size())));
                        } else {
                            showError("Error fetching movies");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        stopLoadingAnimation();
                        showError("Network error: " + t.getMessage());
                    }
                });
    }

    private void displayMovies(List<Movie> movies) {
        for (Movie movie : movies) {
            View movieView = getLayoutInflater().inflate(R.layout.movie_item, movieContainer, false);

            ImageView posterImage = movieView.findViewById(R.id.movie_poster);
            TextView titleText = movieView.findViewById(R.id.movie_title);
            TextView ratingText = movieView.findViewById(R.id.movie_rating);
            TextView overviewText = movieView.findViewById(R.id.movie_overview);

            titleText.setText(movie.getTitle());
            ratingText.setText(String.format("Rating: %.1f", movie.getVoteAverage()));
            overviewText.setText(movie.getOverview());

            if (movie.getPosterPath() != null) {
                String imageUrl = IMAGE_BASE_URL + movie.getPosterPath();
                Glide.with(this)
                        .load(imageUrl)
                        .into(posterImage);
            }

            movieView.setOnClickListener(v -> {
                Intent intent = new Intent(WebserviceActivity.this, MovieDetailActivity.class);
                intent.putExtra("movieId", movie.getId());
                startActivity(intent);
            });

            movieContainer.addView(movieView);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}