package com.example.a6_group2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBApi {
    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
        @Query("api_key") String apiKey,
        @Query("with_genres") int genreId,
        @Query("sort_by") String sortBy
    );
} 