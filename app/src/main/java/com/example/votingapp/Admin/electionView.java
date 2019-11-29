/*
The purpose of this class is to be a page in which an admin can use to view all the elections
from this the admin can select an election to do whatever they want with it.
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

import com.example.votingapp.Model.Election;
import com.example.votingapp.R;
import com.example.votingapp.ViewHolder.ElectionViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class electionView extends AppCompatActivity{

    FirebaseFirestore databaseElections;
    FirebaseAuth firebaseAuth;
    private FirestoreRecyclerAdapter adapter;

    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_view);

        //Database Stuff
        databaseElections = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //Android Stuff
        recyclerView = findViewById(R.id.re_election_view);

        //Adapter to let the recyclerView work
        adapter = setUpAdapter(databaseElections);
        setUpRecyclerView(recyclerView,adapter);

    }

    //Something to setup the recyclerView
    private void setUpRecyclerView(RecyclerView rv, FirestoreRecyclerAdapter adapter)
    {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }

    //Something to setup the adapter
    private FirestoreRecyclerAdapter setUpAdapter(FirebaseFirestore db)
    {

        Query query = db.collection("elections").orderBy("name").limit(50);
        FirestoreRecyclerOptions<Election> options = new FirestoreRecyclerOptions.Builder<Election>()
                .setQuery(query,Election.class)
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

                Date tempDate = new Date(0);

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
                holder.detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(electionView.this, electionDescription.class);//Change Election Detail activity to the new page
                        intent.putExtra("election",model);
                        startActivity(intent);

                    }
                });
            }


            /*

            BROKEN HERE

             */

            @Override//I THINK IT IS BREAKING HERE
            public ElectionViewHolder onCreateViewHolder(ViewGroup group, int i)
            {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.election_entry,group,false);


                return new ElectionViewHolder(view);

            }
        };

        return adapter;

    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView(recyclerView, adapter);
        setUpAdapter(databaseElections);
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

}
