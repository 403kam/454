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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class friend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //firebase
        FirebaseFirestore ls = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        /*//load user in corner THIS HAS BEEN REMOVED DUE TO UGLINESS
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
        });*/

        //load total scoreboard
        ls.collection("Leaderboard").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            //Solution below is not the best way to implement
                            //leaderboard but gets general job done for now
                            ArrayList<String> al = new ArrayList<>();
                            ArrayList<String> fin = new ArrayList<>();
                            SortedMap<String, Integer> sm = new TreeMap<String, Integer>();
                            String name = email.split("@")[0];
                            String end;

                            //Put everthing in a map
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String us = String.valueOf((document.getData()).get("score"));
                                String nameing = String.valueOf((document.getData().get("name")));
                                sm.put(nameing, Integer.parseInt(us));
                            }

                            //sort map from array list
                            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(sm.entrySet());
                            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){
                                    return(o1.getValue()).compareTo(o2.getValue());
                                }
                            });

                            //put back into map
                            Map<String, Integer> smFinal = new LinkedHashMap<String, Integer>();
                            for(Map.Entry<String, Integer> entry : list){
                              smFinal.put(entry.getKey(), entry.getValue());
                            }

                            //Put map into array to display
                            for(Map.Entry<String, Integer> entry : smFinal.entrySet()) {
                                String finalScore = String.valueOf(entry.getValue());
                                String names = entry.getKey();
                                if(name.equals(names))
                                    end = finalScore + " " + names + "     *You*";
                                else
                                    end = finalScore + " " + names;
                                al.add(end);
                            }

                            //display
                            for(int i = al.size() - 1; i >= 0; i--)
                                fin.add(al.get(i));
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(friend.this, android.R.layout.simple_list_item_1, fin);
                            ListView bl = findViewById(R.id.leaderboard);
                            bl.setAdapter(adapter);
                        }
                    }
                });
        /*//Move back to main menu TAKEN OUT DUE TO UGLINESS
        final Button leave = findViewById(R.id.menu);
        leave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //will change later in development
                updating();
                //adding(5);
            }
        });*/
    }
    /*//when back button pressed just move on
    private void updating() {
        Intent intent = new Intent(friend.this, MainActivity.class);
        startActivity(intent);
    }*/
}