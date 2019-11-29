package com.example.votingapp.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.votingapp.R;

/**
 * createCandProfile used to create/update candidates
 */
public class createCandProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextDescription;
    private Button buttonCreate;

    /**
     *
     * @param savedInstanceState saves state of application to be able to recreate another instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonCreate = findViewById(R.id.buttonCreateProfile);

        buttonCreate.setOnClickListener(this);
    }

    /**
     *
     * @param view used to determine which button was pressed and the
     * correct activity to redirect too
     */
    @Override
    public void onClick(View view)
    {
        if (view == buttonCreate)
        {
            //direct to candidates viewing page
        }
    }
}
