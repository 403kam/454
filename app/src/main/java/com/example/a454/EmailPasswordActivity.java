package com.example.a454;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//Taken from firebase and made to work in our application
public class EmailPasswordActivity extends Activity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private static final String bypass = "454";

    //When app boots up this will be the first screen
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth and setup screen
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.email_password_activity);

        //If user is saved just move to main activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //if(currentUser != null)
        //    updateUI(currentUser);

        //When login button is pressed get data to database
        final Button logging = findViewById(R.id.Login);
        logging.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String pass = ((EditText)findViewById(R.id.Password)).getText().toString();

                //special if statement to bypass school wifi
                //if(email.equals(bypass) && pass.equals(bypass))
                    //updating();

                //Move on with normal if statement
                if(!(email.isEmpty()) && !(pass.isEmpty()))
                    signIn(email, pass);
                else
                    Toast.makeText(EmailPasswordActivity.this, "Authentication Failed.",
                            Toast.LENGTH_SHORT).show();
            }
        });

        //When register button is pressed get data to database
        final Button signup = findViewById(R.id.Register);
        signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String pass = ((EditText)findViewById(R.id.Password)).getText().toString();
                if(!(email.isEmpty()) && !(pass.isEmpty()))
                    createAccount(email, pass);
                else
                    Toast.makeText(EmailPasswordActivity.this, "Authentication Failed.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }//end onCreate

    //Connect and add to database
    private void createAccount(String email, String password) {
        //Connect to database to create
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(EmailPasswordActivity.this, "Authentication Passed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Add new user to database for score
                            FirebaseFirestore newsuser = FirebaseFirestore.getInstance();
                            Map<String, Object> newscore = new HashMap<>();
                            newscore.put("score", 0);
                            String name = email.split("@")[0];
                            newscore.put("name", name);
                            String uids = user.getUid();
                            newsuser.collection("Leaderboard").document(uids).set(newscore);

                            //Move on
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }//end createAccount

    //Connect and check if user is saved
    private void signIn(String email, String password) {
        //Connect to database to check user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(EmailPasswordActivity.this, "Authentication Passed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }//end signIn

    //Move on to main activity
    private void updating() {
        Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}//end EmailPassword class
