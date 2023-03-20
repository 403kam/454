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

    public void onSingleModeClick(View view) {
        finish();

        Intent intent = new Intent(this, GuessActivity.class);
        startActivity(intent);
    }

}