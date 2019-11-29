package com.example.votingapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.votingapp.R;

/**
 * ElectionViewHolder class
 * To hold elections information
 */
public class ElectionViewHolder extends RecyclerView.ViewHolder  {

    //declaring objects to hold election values
    public TextView name;
    public Button detailsButton;

    /**
     *
     * @param view used to store desired information for election
     */
    public ElectionViewHolder(View view)
    {
        super(view);
        name = view.findViewById(R.id.contactName);

        detailsButton = view.findViewById(R.id.goDetails);

    }




}

