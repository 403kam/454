package com.example.a454;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;



public class GuessActivity extends AppCompatActivity {

    private ImageButton playPauseButton;
    private Button guessButton;
    private MediaPlayer mediaPlayer;
    private String musicDirectory = "/Music/00's Pop/";
    private List<String> songNames = Arrays.asList("Kanye West  Stronger-[AudioTrimmer.com].mp3", "Coldplay  Viva La Vida Official Video-[AudioTrimmer.com].mp3", "Kelly Clarkson  Since U Been Gone VIDEO-[AudioTrimmer.com].mp3");
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_guess);

        // Find the play/pause button by ID
        playPauseButton = findViewById(R.id.play_pause_button);
        playPauseButton.setImageResource(R.drawable.pause);
        guessButton = findViewById(R.id.guess_button);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    // Change the button icon to "play"
                    playPauseButton.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    // Change the button icon to "pause"
                    playPauseButton.setImageResource(R.drawable.pause);
                }
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guessCheck();
            }
        });

        // Play the first song in the list
        playSong();
    }

    private void playSong() {
        // Get a reference to the current song in the list
        String song = musicDirectory + songNames.get(currentSongIndex);
        StorageReference songRef = FirebaseStorage.getInstance().getReference().child(song);

        // Get the download URL for the song
        songRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Play the song using a media player
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(uri.toString());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void nextSong() {
        // Stop playing the current song
        mediaPlayer.stop();
        mediaPlayer.release();

        // Increment the current song index
        currentSongIndex++;
        if (currentSongIndex >= songNames.size()) {
            currentSongIndex = 0;
        }

        // Play the next song in the list
        playSong();
    }

    public void guessCheck() {
        EditText guessEditText = findViewById(R.id.guess_edittext);
        String userGuess = guessEditText.getText().toString();

        String correctAnswer = "stronger"; // change to your correct answer

        // convert user's guess to lowercase and remove leading/trailing white space
        userGuess = userGuess.trim().toLowerCase();

        // compare formattedGuess to the correctAnswer and return true if they match
        TextView resultText = findViewById(R.id.result_textview);
        boolean result = userGuess.equals(correctAnswer.toLowerCase());
        if (result) {
            resultText.setText("Correct");
        } else {
            resultText.setText("Wrong");
        }

    }

}
