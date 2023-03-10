package com.example.a454;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

//splash screen
//Instead of theme to display we could add as an activity with an animation
//This would be after the loading of the app
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(SplashScreenActivity.this, EmailPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
