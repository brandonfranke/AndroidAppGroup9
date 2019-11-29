package com.example.votingapp.Model;


import java.io.Serializable;

/**
 * Candidate is a model class for a candidate object
 */
public class Candidate implements Serializable {

    public String id;
    private String name;
    private String description;
    private String electionId;
    private String votes;

    /**
     * empty candidate constructor
     */
    public Candidate()
    {

    }

    /**
     * Candidate constructor
     * @param name for candidate
     * @param description for candidate
     * @param electionId for candidate
     * @param votes for candidate
     */
    public Candidate(String name, String description, String electionId, String votes) {
        this.name = name;
        this.description = description;
        this.electionId = electionId;
        this.votes = votes;
    }

    /**
     *
     * @return get method to return name of candidate
     */
    public String getName(){ return name;}

    /**
     *
     * @return get method to return description of candidate
     */
    public String getDescription(){return description;}

    /**
     *
     * @return get method to return electionId of candidate
     */
    public String getelectionId(){return electionId;}

    /**
     *
     * @return get method to return votes of candidate
     */
    public String getVotes(){return votes;}

    /**
     *
     * @return get method to return id of candidate
     */
    public String getId(){return id;}

    /**
     *
     * @return get method to return string of candidate
     */
    public String toString(){return "Name: " + name + " Description: " + description + "Election Id: " + electionId + "Votes: " + votes; }
}