package com.example.a454;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class friend extends AppCompatActivity {

    //Currently in work by Konnor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //Load User score
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
                    TextView scoreText = (TextView)findViewById(R.id.mine);
                    String them = String.valueOf((document.getData()).get("score"));
                    scoreText.setText(them);
                }
            }
        });

        //load total scoreboard
        ls.collection("Leaderboard").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> al = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String us = String.valueOf((document.getData()).get("score"));
                                if(us.length() == 1){
                                    us = "0" + us;
                                }
                                String nameing = String.valueOf((document.getData().get("name")));
                                String white = " ";
                                String end = "";
                                end = us + white + nameing;
                                al.add(end);
                            }
                            //7 is higher then 10 since 7 is higher then 1! Does not work!
                            al.sort(Comparator.reverseOrder());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(friend.this, android.R.layout.simple_list_item_1, al);
                            ListView bl = findViewById(R.id.leaderboard);
                            bl.setAdapter(adapter);
                        }
                    }
                });

        //Move back to main menu
        final Button leave = findViewById(R.id.menu);
        leave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //will change later in development
                updating();
                //adding(5);
            }
        });
    }

    //when back button pressed just move on
    private void updating() {
        Intent intent = new Intent(friend.this, MainActivity.class);
        startActivity(intent);
    }
}