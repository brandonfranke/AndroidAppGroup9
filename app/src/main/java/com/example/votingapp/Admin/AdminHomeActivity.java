package com.example.votingapp.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.votingapp.R;
import com.example.votingapp.RegisterLogin.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * AdminHomeActivity used to display different functions of an admin profile
 */
public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button buttonLogout;
    //private Button buttonCreateProfile;
    private Button buttonCreateElection;
    private Button buttonViewElection;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user is not currently logged in
        if(firebaseAuth.getCurrentUser() == null){
            //finish current activity
            finish();
            //start login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        firebaseUser = firebaseAuth.getCurrentUser();

        buttonLogout = findViewById(R.id.buttonLogout);
        //buttonCreateProfile = findViewById(R.id.buttonCreateProfile);
        buttonCreateElection = findViewById(R.id.buttonCreateElection);
        buttonViewElection = findViewById(R.id.buttonViewElection);

        buttonLogout.setOnClickListener(this);
        //buttonCreateProfile.setOnClickListener(this);
        buttonCreateElection.setOnClickListener(this);
        buttonViewElection.setOnClickListener(this);
    }

    /**
     *
     * @param view used to determine which button is pressed and which activity to start
     */
    @Override
    public void onClick(View view)
    {
        if (view==buttonLogout)
        {
            firebaseAuth.signOut();
            //finish current activity
            finish();
            //start login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //if (view==buttonCreateProfile)
       // {
        //    startActivity(new Intent(this, createCandProfile.class));
       // }

        if (view == buttonCreateElection)
        {
            startActivity(new Intent(AdminHomeActivity.this, CreateElectionActivity.class));
        }

        if (view == buttonViewElection){
            startActivity(new Intent(this,electionView.class));
        }

    }
}
