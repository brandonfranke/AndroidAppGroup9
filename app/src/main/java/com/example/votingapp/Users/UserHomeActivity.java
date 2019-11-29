package com.example.votingapp.Users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.votingapp.R;
import com.example.votingapp.RegisterLogin.LoginActivity;
import com.example.votingapp.ViewHolder.ElectionViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import com.example.votingapp.Model.Election;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * User Home Activity displays the current open elections
 * and can toggle to view closed elections.
 */
public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button buttonLogout;
    private Switch activeSwitch;
    private Query query;

    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapterActive;
    private FirestoreRecyclerAdapter adapterInactive;
    //Connects our recycler view with the viewholder (how we want to show our model[data])
    // and the firestore adapter

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user is not currently logged in
        if(firebaseAuth.getCurrentUser() == null) {
            //finish current activity
            finish();
            //start login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

            recyclerView = findViewById(R.id.electionList);
            database = FirebaseFirestore.getInstance();

            firebaseUser = firebaseAuth.getCurrentUser();

        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);

        adapterActive = setUpAdapter(database);
        adapterInactive = setUpAdapterInactive(database);

        activeSwitch = findViewById(R.id.activeSwitch);
        activeSwitch.setChecked(false);

        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    recyclerView.setAdapter(adapterInactive);
                    Toast.makeText(getBaseContext(), "Showing Inactive Elections", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setAdapter(adapterActive);
                    Toast.makeText(getBaseContext(), "Showing Active Elections", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    /**
     *
     * @param rv creates list to display current elections
     * @param adapter listens to firestore query
     */
     private void setUpRecyclerView(RecyclerView rv, FirestoreRecyclerAdapter adapter)
    {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }


    //Creates a Firestore adapter to be used with a Recycler view.
    //We will see adapter in more details after the midterm
    //More info on this: https://github.com/firebase/FirebaseUI-Android/blob/master/firestore/README.md

    /**
     *
     * @param db Firestore database
     * @return Performs specified query for database
     */
    private FirestoreRecyclerAdapter setUpAdapter(FirebaseFirestore db) {

        query = db.collection("elections").whereEqualTo("active",true).limit(50);

        FirestoreRecyclerOptions<Election> options = new FirestoreRecyclerOptions.Builder<Election>()
                .setQuery(query, Election.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Election, ElectionViewHolder>(options)
        {
            //For each item in the database connect it to the view
            @Override
            public void onBindViewHolder(ElectionViewHolder holder, int position, final Election model)
            {
                holder.name.setText(model.name);

                int year;
                int month;
                int day;

                Object temp;
                String tempword;
                temp = model.getFreezeDate();

                if (temp != null) {
                    temp = temp.toString();
                    Scanner scan = new Scanner((String)temp);
                    scan.next();
                    tempword = scan.next();//Month
                    switch (tempword) {
                        case "Jan": month = 0;
                            break;
                        case "Feb": month = 1;
                            break;
                        case "Mar": month = 2;
                            break;
                        case "Apr": month = 3;
                            break;
                        case "May": month = 4;
                            break;
                        case "Jun": month = 5;
                            break;
                        case "Jul": month = 6;
                            break;
                        case "Aug": month = 7;
                            break;
                        case "Sep": month = 8;
                            break;
                        case "Oct": month = 9;
                            break;
                        case "Nov": month = 10;
                            break;
                        case "Dec": month = 11;
                            break;
                        default: month = -1;
                    }
                    tempword = scan.next();//Day
                    day = Integer.parseInt(tempword);
                    scan.next();
                    scan.next();
                    tempword = scan.next();//Year
                    year = Integer.parseInt(tempword);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);

                    model.setFreezeDate(calendar.getTime());
                }

                if (model.getFreezeDate().equals(System.currentTimeMillis())){
                    model.setActive(false);
                    DocumentReference ref = database.collection("elections/").document(model.getId());
                    ref.update("active", model.getActive());

                }

                //Set the on click for the button
                //I find this ugly :) but it is how you will see in most examples
                // You CAN use lambadas for the listeners
                // e.g. setOnClickListener ((View v) -> ....
                holder.detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHomeActivity.this, ElectionDetailActivity.class);
                        intent.putExtra("election",model);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public ElectionViewHolder onCreateViewHolder(ViewGroup group, int i)
            {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.election_entry,group,false);


                return new ElectionViewHolder(view);

            }
        };

        return adapter;

    }

    //Creates a Firestore adapter to be used with a Recycler view.
    //We will see adapter in more details after the midterm
    //More info on this: https://github.com/firebase/FirebaseUI-Android/blob/master/firestore/README.md
    private FirestoreRecyclerAdapter setUpAdapterInactive(FirebaseFirestore db)
    {
        Date tempDate = new Date();

        query = db.collection("elections").whereEqualTo("active",false).limit(50);

        FirestoreRecyclerOptions<Election> options = new FirestoreRecyclerOptions.Builder<Election>()
                .setQuery(query, Election.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Election, ElectionViewHolder>(options)
        {
            //For each item in the database connect it to the view

            /**
             *
             * @param holder stores election data to make binding easier
             * @param position updates current position based on method being called
             * @param model Election object used to get information on current election.
             */
            @Override
            public void onBindViewHolder(ElectionViewHolder holder, int position, final Election model)
            {
                holder.name.setText(model.name);

                int year;
                int month;
                int day;

                Object temp;
                String tempword;
                temp = model.getFreezeDate();

                if (temp != null) {
                    temp = temp.toString();
                    Scanner scan = new Scanner((String)temp);
                    scan.next();
                    tempword = scan.next();//Month
                    switch (tempword) {
                        case "Jan": month = 0;
                            break;
                        case "Feb": month = 1;
                            break;
                        case "Mar": month = 2;
                            break;
                        case "Apr": month = 3;
                            break;
                        case "May": month = 4;
                            break;
                        case "Jun": month = 5;
                            break;
                        case "Jul": month = 6;
                            break;
                        case "Aug": month = 7;
                            break;
                        case "Sep": month = 8;
                            break;
                        case "Oct": month = 9;
                            break;
                        case "Nov": month = 10;
                            break;
                        case "Dec": month = 11;
                            break;
                        default: month = -1;
                    }
                    tempword = scan.next();//Day
                    day = Integer.parseInt(tempword);
                    scan.next();
                    scan.next();
                    tempword = scan.next();//Year
                    year = Integer.parseInt(tempword);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);

                    model.setFreezeDate(calendar.getTime());
                }

                //Set the on click for the button
                //I find this ugly :) but it is how you will see in most examples
                // You CAN use lambadas for the listeners
                // e.g. setOnClickListener ((View v) -> ....
                holder.detailsButton.setOnClickListener(new View.OnClickListener() {
                    /**
                     * When details button is clicked, will switch to election details activity
                     * as well as sends election model to next class to use
                     * @param v default paramater needed for onClick method
                     */
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserHomeActivity.this, ElectionDetailActivity.class);
                        intent.putExtra("election",model);
                        startActivity(intent);

                    }
                });
            }

            /**
             *
             * @param group view containing children views
             * @param i integer value
             * @return updates ElectionViewHolder
             */
            @Override
            public ElectionViewHolder onCreateViewHolder(ViewGroup group, int i)
            {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.election_entry,group,false);


                return new ElectionViewHolder(view);

            }
        };

        return adapter;

    }

    /**
     * onClick method will wait for button to be pressed and redirect to new activity
     * @param view use view to determine which button was pressed
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

    }


    //Method called every time the activity starts.

    /**
     * onStart method starts when activity starts
     * Connects with database
     */
    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView(recyclerView, adapterActive);
        //setUpAdapter(database);
        //Tells the adapter to start listening for changes in the database
        adapterActive.startListening();
        adapterInactive.startListening();
    }

    //Method called every time the activity stops

    /**
     * onStop method happens when activity ends
     * Stops the adapter from listening and connecting to database
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Tells the adapter to stop listening since we are not using this activity
        //  anymore. Otherwise the adapter would still exist in the background draining battery
        //  with useful cycles...
        adapterActive.stopListening();
        adapterInactive.stopListening();
    }
}
