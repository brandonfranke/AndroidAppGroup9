package com.example.votingapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.votingapp.R;

/**
 * CandidateViewHolder class
 * To hold candidates information
 */
public class CandidateViewHolder extends RecyclerView.ViewHolder {

    //declaring objects to hold candidate values
    public TextView name;
    public TextView votes;
    public Button voteButton;

    /**
     *
     * @param view used to store desired information for candidate
     */
    public CandidateViewHolder(View view){
        super(view);
        name = view.findViewById(R.id.candidateName);

        votes = view.findViewById(R.id.totalVotes);

        voteButton = view.findViewById(R.id.voteSelect);
    }
}

