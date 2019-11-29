package com.example.votingapp.RegisterLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.votingapp.Admin.AdminHomeActivity;
import com.example.votingapp.R;
import com.example.votingapp.Users.UserHomeActivity;
import com.example.votingapp.Model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextType;
    private EditText editTextName;

    private Button buttonSetup;


    FirebaseFirestore databaseUsers;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //setting up dropdown for user types
        editTextType = findViewById(R.id.editTextType);
        editTextName = findViewById(R.id.editTextName);
        buttonSetup = findViewById(R.id.buttonSetup);


        databaseUsers = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        buttonSetup.setOnClickListener(this);
    }

    /**
     * setup method used to gather remaining information
     * for new user and sets it to database and redirects
     * to correct page
     */
    public void setup()
    {
        String email = user.getEmail();
        String type = editTextType.getText().toString();
        String name = editTextName.getText().toString();

        UserInfo userInfo = new UserInfo(email, type, name);

        DocumentReference ref = databaseUsers.collection("users").document();
        userInfo.id = ref.getId();
        ref.set(userInfo);

        if(type.equals("User"))
        {
            //finish current activity
            finish();
            //start profile activity
            startActivity(new Intent(this, UserHomeActivity.class));
        }

        //if type is admin redirect to admin page
        else
        {
            //finish current activity
            finish();
            //start profile activity
            startActivity(new Intent(this, AdminHomeActivity.class));
        }
    }

    /**
     *
     * @param view used to call setup method when button pressed
     */
    //when button pressed calls register method
    public void onClick(View view)
    {
        setup();
    }
}
