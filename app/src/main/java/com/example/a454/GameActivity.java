package com.example.a454;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public void onRandomClick(View view) {
        finish();

        Intent intent = new Intent(this, GuessActivity.class);
        intent.putExtra("genre", "random");
        startActivity(intent);
    }

    public void onPopClick(View view) {
        finish();

        Intent intent = new Intent(this, GuessActivity.class);
        intent.putExtra("genre", "pop");
        startActivity(intent);
    }

    public void onRockClick(View view) {
        finish();

        Intent intent = new Intent(this, GuessActivity.class);
        intent.putExtra("genre", "rock");
        startActivity(intent);
    }

    public void onJazzClick(View view) {
        finish();

        Intent intent = new Intent(this, GuessActivity.class);
        intent.putExtra("genre", "jazz");
        startActivity(intent);
    }

}