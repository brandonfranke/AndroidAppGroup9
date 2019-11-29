package com.example.votingapp.Users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Model.Candidate;
import com.example.votingapp.Model.Election;

import com.example.votingapp.R;
import com.example.votingapp.ViewHolder.CandidateViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Election Detail Activity shows the current election, the elections description and
 * any running candidates. This class also allows the user to vote
 */
public class ElectionDetailActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView electionStatus;
    private TextView timeRemaining;


    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore database;
    private Intent intent;
    private Election election;
    private Candidate candidate;
    FirebaseAuth firebaseAuth;
    String idNum;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_detail);

        intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();

        idNum = firebaseAuth.getCurrentUser().getUid();

        name = findViewById(R.id.electionName);
        description = findViewById(R.id.electionDescription);
        timeRemaining = findViewById(R.id.timeRemaining);
        electionStatus = findViewById(R.id.electionStatus);
        recyclerView = findViewById(R.id.candidateList);

        database = FirebaseFirestore.getInstance();

        election = (Election) intent.getSerializableExtra("election");


        name.setText(election.name);
        description.setText(election.desc);

        //System.out.println(election.id);
        final String path = "candidates/";


        adapter = setUpAdapter(database, path);
        setUpRecyclerView(recyclerView, adapter);

        name.setText(election.getTitle());
        description.setText(election.getDesc());


        if (!election.getActive()) {
            electionStatus.setText("Inactive");
        } else {
            electionStatus.setText("Active");
        }

        Date freezeDate = election.getFreezeDate();

        if (election.getActive()) {
            if (!(freezeDate.equals(new Date(0)))) {

                long dateNow = System.currentTimeMillis();

                long dateDiff = freezeDate.getTime() - dateNow;

                dateDiff = TimeUnit.MILLISECONDS.toDays(dateDiff);

                String dateDiffString = Long.toString(dateDiff);



                if (dateDiff > 0) {
                    timeRemaining.setText(dateDiffString + " Days Remaining");
                }
                else if (dateDiff == -1) {
                    election.setActive(false);
                    DocumentReference ref = database.collection("elections/").document(election.getId());
                    ref.update("active", election.getActive());
                    timeRemaining.setText("Election has Ended");
                }
                else {
                    timeRemaining.setText("No Freeze Time");
                }
            }



        }
        else {
            timeRemaining.setText("");
        }

        Date dateZero = new Date(1999,1,1);
        Date d = new Date(0);

        DateFormat df = DateFormat.getDateTimeInstance();
        df.format(dateZero);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        //System.out.println("NearBow Bells: " + df.format(d));
        Toast.makeText(ElectionDetailActivity.this, freezeDate.toString(), Toast.LENGTH_LONG).show();

    }

    /**
     *
     * @param rec creates list to display current candidates
     * @param adapter listens to firestore query
     */
    private void setUpRecyclerView(RecyclerView rec, FirestoreRecyclerAdapter adapter) {
        RecyclerView.LayoutManager man = new LinearLayoutManager(getApplicationContext());
        rec.setLayoutManager(man);
        rec.setItemAnimator(new DefaultItemAnimator());
        rec.setAdapter(adapter);
    }

    /**
     *
     * @param data Firestore database
     * @param path Path to correct collection in database
     * @return Performs specified query for database
     */
    private FirestoreRecyclerAdapter setUpAdapter(final FirebaseFirestore data, final String path) {
        Query query = data.collection(path).orderBy("name").whereEqualTo("electionId", election.getId());
        FirestoreRecyclerOptions<Candidate> options = new FirestoreRecyclerOptions.Builder<Candidate>()
                .setQuery(query, Candidate.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Candidate, CandidateViewHolder>(options) {
            //For each item in the database connect it to the view

            /**
             *
             * @param holder stores candidate data to make binding easier
             * @param position updates current position based on method being called
             * @param model Candidate object used to get information on current candidate.
             */
            @Override
            public void onBindViewHolder(CandidateViewHolder holder, int position, final Candidate model) {

                holder.name.setText(model.getName());
                holder.votes.setText("Vote Count " + model.getVotes());

                firebaseAuth = FirebaseAuth.getInstance();

                idNum = firebaseAuth.getCurrentUser().getUid();
//                if (idNum == null)
//                    Toast.makeText(ElectionDetailActivity.this, "ID not found", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(ElectionDetailActivity.this, idNum, Toast.LENGTH_LONG).show();


                    holder.voteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int voteCount = Integer.parseInt(model.getVotes());

                            //if the election isn't frozen
                            if (election.getActive()) {

                                //then check if the number of votes is limited
                                if (election.getLimitVote()) {

                                    //now check if user has already voted or not
                                    if (election.checkID(idNum)) {
                                        Toast.makeText(ElectionDetailActivity.this, "Sorry you only can vote once!", Toast.LENGTH_LONG).show();
                                    } else {
                                        election.addUser(idNum);
                                        voteCount++;

                                    }
                                }
                                else
                                    voteCount++;
                            }


                            String votes = Integer.toString(voteCount);

                            DocumentReference ref = data.collection(path).document(model.getId());
                            DocumentReference refElection = data.collection("elections/").document(election.id);
                            ref.update("votes", votes);
                            refElection.update("votesByUser", election.votesByUser);

                        }
                    });


            }

            /**
             *
             * @param group view containing children views
             * @param i integer value
             * @return updates CandidateViewHolder
             */
            @Override
            public CandidateViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.vote, group, false);
                return new CandidateViewHolder(view);

            }
        };

        return adapter;

    }


    //Method called every time the activity starts.

    /**
     * onStart method starts when activity starts
     * Connects with database
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Tells the adapter to start listening for changes in the database
        adapter.startListening();
    }

    //Method called every time the activity stops

    /**
     * onStop method happens when activity ends
     * Stops the adapter from listening and connecting to database
     */
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}


