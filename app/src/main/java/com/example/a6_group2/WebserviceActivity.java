package com.example.a6_group2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebserviceActivity extends AppCompatActivity {
    Spinner movie_genre, movie_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webservice);

        // for movie genre dropdown menu
        movie_genre = (Spinner) findViewById(R.id.genres_dropdown);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.movie_genres,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        movie_genre.setAdapter(adapter);

        // Set onItemSelectedListener
        movie_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // update query category
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        // for movie cnt dropdown
        movie_cnt = findViewById(R.id.movie_cnt);

        // Data for dropdown menu
        String[] items = {"1", "3", "5"};

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter_cnt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        // Set adapter to Spinner
        movie_cnt.setAdapter(adapter_cnt);

        // Set onItemSelectedListener
        movie_cnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // update the query cnt
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });





    }
}