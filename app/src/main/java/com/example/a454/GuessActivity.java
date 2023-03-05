package com.example.a454;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.media.MediaPlayer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;



public class GuessActivity extends AppCompatActivity {

    private ImageButton playPauseButton;
    private MediaPlayer mediaPlayer;
    private String musicDirectory = "Music/";
    private List<String> songNames = Arrays.asList("Kanye West  Stronger-[AudioTrimmer.com].mp3", "Coldplay  Viva La Vida Official Video-[AudioTrimmer.com].mp3", "Kelly Clarkson  Since U Been Gone VIDEO-[AudioTrimmer.com].mp3");
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_guess);

        // Find the play/pause button by ID
        playPauseButton = findViewById(R.id.play_pause_button);

        // Play the first song in the list
        playSong();
    }

    private void playSong() {
        // Get a reference to the current song in the list
        StorageReference songRef = FirebaseStorage.getInstance().getReference().child(musicDirectory + songNames.get(currentSongIndex));

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


    public void onPlayPauseButtonClick(View view) {
        // Switch the image resource and tag depending on the current state
        if (playPauseButton.getTag() == null || playPauseButton.getTag().equals("play")) {
            playPauseButton.setImageResource(R.drawable.pause);
            playPauseButton.setTag("pause");
        } else {
            playPauseButton.setImageResource(R.drawable.play);
            playPauseButton.setTag("play");
        }
    }
}
