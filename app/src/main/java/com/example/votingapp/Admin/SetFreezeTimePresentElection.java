package com.example.votingapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.votingapp.Model.Election;
import com.example.votingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class SetFreezeTimePresentElection extends AppCompatActivity implements View.OnClickListener{


    Election election;
    CalendarView enddate;

    Button nextPage;

    Date date;

    Intent intent;

    FirebaseFirestore databaseElections;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_freeze_time);

        intent = getIntent();

        enddate = findViewById(R.id.calendarView);
        nextPage = findViewById(R.id.setCandidates);

        databaseElections = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        election = (Election) intent.getSerializableExtra("election");

        date = new Date();

        nextPage.setOnClickListener(this);
        enddate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView enddate, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,day);
                date.setTime(cal.getTimeInMillis());
            }

        });

    }


    @Override
    public void onClick(View view){
        if (view == nextPage){
            election.setFreezeDate(date);
            Intent intentEnd = new Intent(this, electionView.class);

            DocumentReference ref = databaseElections.collection("elections/").document(election.getId());
            ref.update("freezeDate", election.getFreezeDate());
            System.out.println(election.getFreezeDate());

            intentEnd.putExtra("election",election);

            startActivity(intentEnd);
        }

    }
}

