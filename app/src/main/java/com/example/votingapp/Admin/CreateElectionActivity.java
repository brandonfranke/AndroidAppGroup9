package com.example.votingapp.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Model.Election;
import com.example.votingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * CreateElectionActivity used to create and open a new election
 */
public class CreateElectionActivity extends AppCompatActivity implements View.OnClickListener {

        Election election;
        EditText title_box;
        EditText desc_box;
        EditText num_box;

        TextView error_box;
        Switch limitVotes;

        Button create_election;

        FirebaseFirestore databaseElections;
        FirebaseAuth firebaseAuth;

        String electionId;
        Boolean setFreezeDate;
        Switch freezeDateSwitch;
        Boolean limiter;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_creation_start);

            title_box = findViewById(R.id.election_name);
            desc_box = findViewById(R.id.election_desc);
            num_box = findViewById(R.id.candidate_num);
            freezeDateSwitch = findViewById(R.id.setFreezeTimeSwitch);
            create_election = findViewById(R.id.button_create_election);
            create_election.setOnClickListener(this);
            freezeDateSwitch.setChecked(false);
            limiter = false;
            setFreezeDate = false;

            error_box = findViewById(R.id.error_display);

            databaseElections = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            electionId = "";

            limitVotes = findViewById(R.id.limitVoteSwitch);
            limitVotes.setChecked(false);

            limitVotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    limiter = true;
                    Toast.makeText(getBaseContext(), "Limit Election Votes", Toast.LENGTH_SHORT).show();
                } else {
                    limiter = false;
                    Toast.makeText(getBaseContext(), "Unlimited Election Votes", Toast.LENGTH_SHORT).show();

                }
            }
        });

        }

    /**
     * createElection method used to get information for new election
     * and set it to firebase db
     */
    public void createElection() {//This handles the button press for next page
            String name = title_box.getText().toString();
            String desc = desc_box.getText().toString();

            int num;
            //Right, so when you go to print out the value nothing happens, its like it ignores the print statement
            if (num_box.getText().toString().equals("")){
                num = 0;
            }
            else{
                num = Integer.parseInt(num_box.getText().toString());//This conversion causes error if it is null/blank
            }

            //create election
                election = new Election(name,desc, num);
                DocumentReference ref = databaseElections.collection("elections").document();
                election.id = ref.getId();
                election.setLimitVote(limiter);
                electionId = election.id;
                if(!freezeDateSwitch.isChecked()){
                    election.setActive(true);
                }
                ref.set(election);

    }

    /**
     *
     * @param view used to determine which view to go to
     * as well as sending data to that activity
     */
    public void onClick(View view){
        setFreezeDate = freezeDateSwitch.isChecked();
        if (view == create_election) {
            createElection();

            Intent intent;

            //Setup an if statement so that it will go to a page for handling freeze times.
            if (setFreezeDate) {//This is to go to a page that allows the user to put in a date for the election to freeze
                intent = new Intent(this, SetFreezeTime.class);
                intent.putExtra("election", election);
            } else {
                intent = new Intent(CreateElectionActivity.this, addCandidate.class);
            }
            intent.putExtra("numOcand", num_box.getText().toString());
            intent.putExtra("electionid", electionId);
            startActivity(intent);
        }
    }

}
