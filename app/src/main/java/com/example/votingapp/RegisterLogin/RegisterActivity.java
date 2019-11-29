package com.example.votingapp.RegisterLogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Admin.AdminHomeActivity;
import com.example.votingapp.R;
import com.example.votingapp.Users.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//implemented for OnClick method

/**
 * RegisterActivity used to allow new users to create an
 * admin or user account for this application
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring UI objects
    private EditText editTextUserId;
    private EditText editTextPass;
    private Button buttonRegister;
    private TextView textViewSignIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore databaseUsers;
    private FirebaseUser firebaseUser;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing objects
        editTextUserId = findViewById(R.id.editTextUserId);
        editTextPass = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        //initializing database
        databaseUsers = FirebaseFirestore.getInstance();

        //initializing firebase authetication
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing current user if there is one
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //checking if user is already logged in
        if(firebaseAuth.getCurrentUser() != null){

            //reference to users database
            DocumentReference ref = databaseUsers.collection("users").document(firebaseUser.getUid());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                //using method to get database usertype value

                /**
                 *
                 * @param task using reference to determine if account made
                 * is a user account or an admin account
                 */
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null)
                    {
                        //setting string to user type for current user
                        String userType = documentSnapshot.getString("UserType");
                        //if type is user redirect to user profile
                        if(userType == "User")
                        {
                            //finish current activity
                            finish();
                            //start profile activity
                            startActivity(new Intent(getApplicationContext(), UserHomeActivity.class));
                        }

                        //if type is admin redirect to admin page
                        if(userType == "Admin")
                        {
                            //finish current activity
                            finish();
                            //start profile activity
                            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        }
                    }
                }
            });
        }

        //attach listener to button for click-ability
        buttonRegister.setOnClickListener(this);

        //attach listener to textview for click-ability
        textViewSignIn.setOnClickListener(this);

    }

    //register method to add users to firebase database

    /**
     * register method used to add the newly registered account to firebase db
     */
    private void register() {

        //declaring variables for sign in process
        String userId = editTextUserId.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();

        //if user id text is empty display message
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "Enter User ID Number", Toast.LENGTH_LONG).show();
            return;
        }

        //if password text is empty display message
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        //create new user
        firebaseAuth.createUserWithEmailAndPassword(userId, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            /**
             *
             * @param task used to ensure registration process was a success
             * if so starts the setup activity page
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registration Success!", Toast.LENGTH_LONG).show();
                    //closing current view
                    finish();
                    //bring to set up page
                    startActivity(new Intent(RegisterActivity.this, SetupActivity.class));

                }else{
                    Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        //signing in registered user
        firebaseAuth.signInWithEmailAndPassword(userId, pass);
        
    }

    //when button pressed calls register method

    /**
     *
     * @param view used to determine which button was pressed and
     * therefore which activity should be started
     */
    @Override
    public void onClick(View view) {
        //calling login method if button pressed
        if(view == buttonRegister)
        {
            //calling register method
            register();

        }

        //calling sign up if link pressed
        if(view == textViewSignIn)
        {
            //closes current view
            finish();
            //brings to sign up page
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}