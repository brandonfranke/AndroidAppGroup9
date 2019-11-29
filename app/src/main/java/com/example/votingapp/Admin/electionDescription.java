
/*
So for some reason it is setting the active variable to false somewhere in here.
 */

package com.example.votingapp.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.votingapp.Model.Candidate;
import com.example.votingapp.Model.Election;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.example.votingapp.R;
import com.example.votingapp.ViewHolder.CandidateViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class electionDescription extends AppCompatActivity implements View.OnClickListener{

    private TextView name;
    private TextView description;
    private TextView endDate;
    private Button freezeButton;
    private Button freezeDateButton;

    private Date tempDate = new Date(0);

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore database;
    private Intent intent;
    private Election election;
    private Candidate candidate;

    Date currTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_election_detail);


        name = findViewById(R.id.electionName);
        description = findViewById(R.id.electionDescription);
        endDate = findViewById(R.id.EndDate);
        recyclerView = findViewById(R.id.candidateList);
        freezeDateButton = findViewById(R.id.freezeDateButton);
        freezeDateButton.setOnClickListener(this);

        freezeButton = findViewById(R.id.buttonFreeze);
        freezeButton.setOnClickListener(this);

        intent = getIntent();

        database = FirebaseFirestore.getInstance();

        election = (Election) intent.getSerializableExtra("election");

        if(election.getActive() == true){
            freezeButton.setText("Freeze Election");
        }
        else{
            freezeButton.setText("Un-freeze Election");
        }

        name.setText(election.name);
        description.setText(election.getDesc());

        if (election.getFreezeDate() == tempDate){
            endDate.setText("No end date.");
        }
        else {
            String tempText = election.getFreezeDate().toString();
            String reText;
            Scanner scan = new Scanner(tempText);
            reText = scan.next() + " ";
            reText = reText + scan.next() + " ";
            reText = reText + scan.next() + " ";
            scan.next();
            scan.next();
            reText = reText + scan.next();

            endDate.setText("End date: "+reText);
            if(reText.equals("Wed Dec 31 1969")){
                endDate.setText("No End Date.");
            }
        }

        final String path = "candidates/";

        adapter = setUpAdapter(database, path);
        setUpRecyclerView(recyclerView, adapter);

        currTime = new Date(System.currentTimeMillis());
        Date freezeDate = election.getFreezeDate();
        if (election.getActive()) {
            if (!(freezeDate.equals(new Date(0)))) {

                long dateNow = System.currentTimeMillis();

                long dateDiff = freezeDate.getTime() - dateNow;

                dateDiff = TimeUnit.MILLISECONDS.toDays(dateDiff);

                String dateDiffString = Long.toString(dateDiff);



                if (dateDiff > 0) {
                    election.setActive(true);
                    DocumentReference ref = database.collection("elections/").document(election.getId());
                    ref.update("active", election.getActive());
                    freezeButton.setText("Freeze Election");
                }

                else if (dateDiff == 0||dateDiff == -1) {
                    election.setActive(false);
                    DocumentReference ref = database.collection("elections/").document(election.getId());
                    ref.update("active", election.getActive());
                    freezeButton.setText("Un-freeze Election");
                    endDate.setText("Election Closed.");
                }
                else {
                    election.setActive(true);
                    DocumentReference ref = database.collection("elections/").document(election.getId());
                    ref.update("active", election.getActive());
                    freezeButton.setText("Freeze Election");
                }
            }



        }

    }

    private void setUpRecyclerView(RecyclerView rec, FirestoreRecyclerAdapter adapter) {
        RecyclerView.LayoutManager man = new LinearLayoutManager(getApplicationContext());
        rec.setLayoutManager(man);
        rec.setItemAnimator(new DefaultItemAnimator());
        rec.setAdapter(adapter);
    }
    private FirestoreRecyclerAdapter setUpAdapter(final FirebaseFirestore data, final String path){
        Query query = data.collection(path).orderBy("name").whereEqualTo("electionId", election.getId());
        FirestoreRecyclerOptions<Candidate> options = new FirestoreRecyclerOptions.Builder<Candidate>()
                .setQuery(query,Candidate.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Candidate, CandidateViewHolder>(options)
        {
            //For each item in the database connect it to the view
            @Override
            public void onBindViewHolder(CandidateViewHolder holder, int position, final Candidate model)
            {
                holder.name.setText(model.getName());
                holder.votes.setText("Vote Count " + model.getVotes());
            }

            @Override
            public CandidateViewHolder onCreateViewHolder(ViewGroup group, int i)
            {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.admin_vote,group,false);
                return new CandidateViewHolder(view);

            }
        };

        return adapter;

    }


    //Method called every time the activity starts.
    @Override
    protected void onStart() {
        super.onStart();
        //Tells the adapter to start listening for changes in the database
        adapter.startListening();
    }

    //Method called every time the activity stops
    @Override
    protected void onStop() {
        super.onStop();
        //Tells the adapter to stop listening since we are not using this activity
        //  anymore. Otherwise the adapter would still exist in the background draining battery
        //  with useful cycles...
        adapter.stopListening();
    }

    public void onClick(View view){
        if (view == freezeButton){
            currTime = new Date(System.currentTimeMillis());
            //Have it set the election to frozen
            if (election.getActive() == true && election.getFreezeDate().compareTo(currTime) > 0){
                freezeButton.setText("Un-freeze Election");
                election.setFreezeDate(currTime);
                election.setActive(false);
                endDate.setText("Election Closed.");
            }
            else if (election.getActive() == true && election.getFreezeDate().equals(new Date(0))){
                freezeButton.setText("Un-freeze Election");
                election.setFreezeDate(currTime);
                election.setActive(false);
                endDate.setText("Election Closed.");
            }
            else{
                freezeButton.setText("Freeze Election");
                election.setFreezeDate(tempDate);
                election.setActive(true);
                endDate.setText("No end date.");
            }
            //Update the database
            DocumentReference ref = database.collection("elections/").document(election.getId());
            ref.update("freezeDate", election.getFreezeDate());
            ref.update("active", election.getActive());
        }

        if (view == freezeDateButton){
            Intent intentEnd = new Intent(this, SetFreezeTimePresentElection.class);
            intentEnd.putExtra("election",election);
            startActivity(intentEnd);

        }

    }

}


