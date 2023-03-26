package com.example.a454;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GuessActivity extends AppCompatActivity {

    private int score = 0;
    private ImageButton playPauseButton;
    private Button guessButton;
    private MediaPlayer mediaPlayer;
    private String genre;
    private List<String> songs;
    private List<String> answers;
    private List<String> popSongs = Arrays.asList(
            "/Music/00's Pop/Kanye West  Stronger-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Coldplay  Viva La Vida Official Video-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Kelly Clarkson  Since U Been Gone VIDEO-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Lady Gaga  Poker Face Official Music Video-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Rihanna  Dont Stop The Music-[AudioTrimmer.com].mp3");
    private List<String> popAnswers = Arrays.asList("stronger", "viva la vida", "since u been gone", "poker face", "dont stop the music");

    private List<String> jazzSongs = Arrays.asList(
            "/Music/Jazz/Frank Sinatra  Fly Me To The Moon Live At The Kiel Opera House St Louis MO1965.mp3",
            "/Music/Jazz/Kenny G  The Moment Official Video.mp3",
            "/Music/Jazz/Louis Armstrong  La Vie En Rose 1950 Digitally Remastered.mp3",
            "/Music/Jazz/Louis Armstrong  What A Wonderful World.mp3",
            "/Music/Jazz/Sade  Smooth Operator  Official  1984.mp3");
    private List<String> jazzAnswers = Arrays.asList("fly me to the moon", "the moment", "la vie en rose", "what a wonderful world", "smooth operator");

    private List<String> rockSongs = Arrays.asList(
            "/Music/Rock/BAD TO THE BONE.mp3",
            "/Music/Rock/Bon Jovi  Livin On A Prayer.mp3",
            "/Music/Rock/Guns N Roses  Sweet Child O Mine Official Music Video.mp3",
            "/Music/Rock/KISS  I Was Made For Loving You.mp3",
            "/Music/Rock/Survivor  Eye Of The Tiger Official HD Video.mp3");
    private List<String> rockAnswers = Arrays.asList("bad to the bone", "livin on a prayer", "sweet child o mine", "i was made for loving you", "eye of the tiger");

    private List<String> randomSongs = Arrays.asList(
            "/Music/00's Pop/Kanye West  Stronger-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Coldplay  Viva La Vida Official Video-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Kelly Clarkson  Since U Been Gone VIDEO-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Lady Gaga  Poker Face Official Music Video-[AudioTrimmer.com].mp3",
            "/Music/00's Pop/Rihanna  Dont Stop The Music-[AudioTrimmer.com].mp3",
            "/Music/Jazz/Frank Sinatra  Fly Me To The Moon Live At The Kiel Opera House St Louis MO1965.mp3",
            "/Music/Jazz/Kenny G  The Moment Official Video.mp3",
            "/Music/Jazz/Louis Armstrong  La Vie En Rose 1950 Digitally Remastered.mp3",
            "/Music/Jazz/Louis Armstrong  What A Wonderful World.mp3",
            "/Music/Jazz/Sade  Smooth Operator  Official  1984.mp3",
            "/Music/Rock/BAD TO THE BONE.mp3",
            "/Music/Rock/Bon Jovi  Livin On A Prayer.mp3",
            "/Music/Rock/Guns N Roses  Sweet Child O Mine Official Music Video.mp3",
            "/Music/Rock/KISS  I Was Made For Loving You.mp3",
            "/Music/Rock/Survivor  Eye Of The Tiger Official HD Video.mp3");

    private List<String> randomAnswers = Arrays.asList(
            "stronger",
            "viva la vida",
            "Since u been gone",
            "poker face",
            "dont stop the music",
            "fly me to the moon",
            "the moment",
            "la vie en rose",
            "what a wonderful world",
            "smooth operator",
            "bad to the bone",
            "livin on a prayer",
            "sweet child o mine",
            "i was made for loving you",
            "eye of the tiger");

    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_guess);
        Intent intent = getIntent();
        genre = intent.getStringExtra("genre");

        if (genre.equals("pop")) {
            songs = popSongs;
            answers = popAnswers;
        } else if (genre.equals("rock")) {
            songs = rockSongs;
            answers = rockAnswers;
        } else if (genre.equals("jazz")) {
            songs = jazzSongs;
            answers = jazzAnswers;
        } else {
            songs = randomSongs;
            answers = randomAnswers;
        }

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
        Random random = new Random();
        currentSongIndex = random.nextInt(songs.size());
        String song = songs.get(currentSongIndex);
        StorageReference songRef = FirebaseStorage.getInstance().getReference().child(song);

        // Get the download URL for the song
        songRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Play the song using a media player
                playPauseButton.setImageResource(R.drawable.pause);
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
        if (currentSongIndex >= popSongs.size()) {
            currentSongIndex = 0;
        }

        TextView resultText = findViewById(R.id.result_textview);
        EditText textEntry = findViewById(R.id.guess_edittext);
        resultText.setText("New Song");
        textEntry.setText("");

        // Play the next song in the list
        playSong();
    }

    public void guessCheck() {
        EditText guessEditText = findViewById(R.id.guess_edittext);
        String userGuess = guessEditText.getText().toString();

        String correctAnswer = answers.get(currentSongIndex); // change to your correct answer

        // convert user's guess to lowercase and remove leading/trailing white space
        userGuess = userGuess.trim().toLowerCase();

        // compare formattedGuess to the correctAnswer and return true if they match
        TextView resultText = findViewById(R.id.result_textview);
        TextView points = findViewById(R.id.points_text);
        boolean result = userGuess.equals(correctAnswer.toLowerCase());
        if (result) {
            resultText.setText("Correct");
            score += 10;
            points.setText(String.valueOf(score));
            nextSong();
        } else {
            resultText.setText("Wrong");
            score -= 1;
            points.setText(String.valueOf(score));
        }

    }

    public void onDoneClick(View view) {
        // Stop playing the current song
        mediaPlayer.stop();
        mediaPlayer.release();
        uploadScore();
        finish();

        Intent intent = new Intent(this, GameEndActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    public void uploadScore() {
        // after game ended:
        // if score > high_score:
            // upload score to database
        FirebaseFirestore ls = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uids = user.getUid();
        DocumentReference docRef = ls.collection("Leaderboard").document(uids);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String them = String.valueOf((document.getData()).get("score"));
                    int num = Integer.valueOf(them);
                    if(score > num) {
                        Map<String, Object> scores = new HashMap<>();
                        scores.put("score", score);
                        String email = user.getEmail();
                        String name = email.split("@")[0];
                        scores.put("name", name);
                        ls.collection("Leaderboard").document(uids).set(scores);
                    }
                }
            }
        });

    }

}
