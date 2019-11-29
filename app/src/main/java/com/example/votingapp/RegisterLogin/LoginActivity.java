/********
 * THINGS TO FIX
 * MAKE SURE IF REGISTRATION/SETUP/LOGIN ONLY SWITCH ACTIVITIES IF SUCCESS
 * SET UP LOGIN TO GO TO SPECIFIC PAGE ADMIN OR USER
 * MAKE CANDIDATE PROFILE ON ADMIN PAGE
 */


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
import com.google.firebase.firestore.FirebaseFirestore;

//implemented for OnClick method

/**
 * LoginActivity is the default login page for this app
 * It checks the database to authentication
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    //declaring UI objects
    private EditText editTextEmail;
    private EditText editTextPass;
    private Button buttonLoginUser;
    private Button buttonLoginAdmin;
    private TextView textViewSignUp;
    private FirebaseFirestore databaseUsers;
    private FirebaseAuth firebaseAuth;
    private String email;
    private String pass;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing objects
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPass = findViewById(R.id.editTextPassword);
        buttonLoginUser = findViewById(R.id.buttonLoginUser);
        buttonLoginAdmin = findViewById(R.id.buttonLoginAdmin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        //initializing database
        databaseUsers = FirebaseFirestore.getInstance();

        //initializing firebase authetication
        firebaseAuth = FirebaseAuth.getInstance();

        //attach listener to button for click-ability
        buttonLoginUser.setOnClickListener(this);

        //attach listener to button for click-ability
        buttonLoginAdmin.setOnClickListener(this);

        //attach listener to textview for click-ability
        textViewSignUp.setOnClickListener(this);

    }

    //login method to add users to firebase database

    /**
     * loginUser method used to verify with firebase user credentials
     */
    private void loginUser()
    {

        //initializing strings based on user input
        email = editTextEmail.getText().toString().trim();
        pass = editTextPass.getText().toString().trim();

        //if user id text is empty display message
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show();
            return;
        }

        //if password text is empty display message
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        //signing user in authentication
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            /**
             *
             * @param task used to run onComplete method until task is completed
             * if tasks successful method will call user home activity
             */
            @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));

                        }else{
                            Toast.makeText(LoginActivity.this, "Login Error, please try again", Toast.LENGTH_LONG).show();

                        }
                    }
        });
    }

    //register method to add users to firebase database

    /**
     * loginAdmin method used to verify with firebase admin credentials
     */
    private void loginAdmin()
    {

        //initializing strings based on user input
        email = editTextEmail.getText().toString().trim();
        pass = editTextPass.getText().toString().trim();

        //if user id text is empty display message
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show();
            return;
        }

        //if password text is empty display message
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        //signing user in authentication
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            /**
             *
             * @param task used to run onComplete method until task is completed
             * if tasks successful method will call admin home activity
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));

                }else{
                    Toast.makeText(LoginActivity.this, "Login Error, please try again", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    //method called when listener is clicked

    /**
     *
     * @param view used to determine which button was pressed
     * and therefore determine which activity should be started
     */
    @Override
    public void onClick(View view) {
        //calling login method if button pressed for user
        if(view == buttonLoginUser)
        {
            loginUser();

        }

        //calling login method if button pressed for admin
        if(view == buttonLoginAdmin)
        {
            loginAdmin();

        }

        //calling sign up if link pressed
        if(view == textViewSignUp)
        {
            //closes current view
            finish();
            //brings to sign up page
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

}