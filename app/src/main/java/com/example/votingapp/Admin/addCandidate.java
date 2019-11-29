package com.example.votingapp.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Model.Candidate;
import com.example.votingapp.R;
import com.example.votingapp.Users.ElectionDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * addCandidate class used to add candidate to current created election
 */
public class addCandidate extends AppCompatActivity implements View.OnClickListener {

    TextView canDisplay;
    EditText input;
    EditText inputDesc;
    Button next;
    Button finish;
    public int counter;
    public String numOcan;
    public String electionId;
    public int numOcand;
    public String votes;
    Intent intent;
    FirebaseFirestore databaseCandidates;
    FirebaseAuth firebaseAuth;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        intent = getIntent();
        numOcan = intent.getStringExtra("numOcand");
        numOcand = Integer.parseInt(numOcan);
        electionId = intent.getStringExtra("electionid");
        input = findViewById(R.id.canNameInput);
        inputDesc = findViewById(R.id.canDesc);
        next = findViewById(R.id.nextBtn);
        finish = findViewById(R.id.buttonFinish);
        canDisplay = findViewById(R.id.candidateState);
        counter = 1;
        votes = "0";
        next.setOnClickListener(this);
        finish.setOnClickListener(this);
        finish.setEnabled(false);

        databaseCandidates = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    /**
     * addCandidates method used to create new candidates and set them to the current election id
     */
    private void addCandidates()
    {
            canDisplay.setText("Candidate "+counter+" of "+numOcand);
            String name = input.getText().toString();
            String description = inputDesc.getText().toString();

            Candidate candidate = new Candidate(name, description, electionId, votes);

            DocumentReference ref = databaseCandidates.collection("candidates").document();
            candidate.id = ref.getId();
            ref.set(candidate);
            Toast.makeText(addCandidate.this, "Candidate Created.", Toast.LENGTH_LONG).show();
            counter ++;
            if(counter > numOcand){
                finish.setEnabled(true);
                next.setEnabled(false);
            }
    }

    /**
     *
     * @param view used to determine which button was pressed
     * and depending on the button pressed, determines which view should be started
     */
    public void onClick(View view)
    {
        if (view == next) {
            addCandidates();
            input.setText("");
            inputDesc.setText("");
        }
        if (view == finish){
            finish();
            startActivity(new Intent(addCandidate.this, AdminHomeActivity.class));
        }


    }


}
