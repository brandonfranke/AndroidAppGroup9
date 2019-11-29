package com.example.votingapp.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;

/**
 * Election is a model class for a election object
 */
public class Election implements Serializable {

    public String id;
    public String name;
    public String desc;
    public int num_candidates;
    public boolean active;
    public Date freezeDate;
    public boolean limitVote;
    public HashMap<String, Integer> votesByUser;

    /**
     * empty election constructor
     */
    public  Election()
    {
        this.votesByUser = new HashMap<String, Integer>();
    }

    /**
     * Election Constructor
     * @param name returns election name
     */
    public Election(String name)
    {
        this.name = name;
        this.votesByUser = new HashMap<String, Integer>();
    }

    /**
     * Election Constructor
     * @param title returns election title name
     * @param desc returns election description
     * @param num_candidates returns election number of candidates
     */
    public Election(String title, String desc, int num_candidates) {
        new Date(0);
        this.num_candidates = num_candidates;
        this.desc = desc;
        this.name = title;
        this.active = true;
        this.freezeDate = new Date(0);
        this.limitVote = false;
        this.votesByUser = new HashMap<String, Integer>();

    }

    /**
     *
     * @return id of election
     */
    public String getId(){return id;}

    /**
     *
     * @return title name of election
     */
    public String getTitle(){return name;}

    /**
     *
     * @return description of election
     */
    public String getDesc(){return desc;}

    /**
     *
     * @return number of candidates of election
     */
    public int getNumCandidates(){ return num_candidates;}
    public boolean getActive(){ return active;}

    public void setActive(boolean bool){
        active = bool;
    }

    /**
     *
     * @return if election allows more than one vote
     */
    public boolean getLimitVote(){ return limitVote;}

    /**
     *
     * @return time and date of election freeze
     */
    public Date getFreezeDate(){ return freezeDate;}

    public void setFreezeDate(Date freezeDate){this.freezeDate = freezeDate;}


    /*
    public Date toDate(Election election){
        election.getFreezeDate()
    }
    */

    /**
     *
     * @param limitVote boolean for if election is one vote more multiple
     */
    public void setLimitVote(boolean limitVote){
        this.limitVote = limitVote;
    }

    /**
     *
     * @param id takes in id of current logged in user
     * @return returns if the user has voted or not
     */
    public boolean checkID(String id){

        if(votesByUser.isEmpty())
            return false;
        else {
            if (votesByUser.containsKey(id))
                return true;
            else
                return false;
        }
    }

    /**
     *
     * @param id adds current logged users id to map
     */
    public void addUser(String id)
    {
        votesByUser.put(id, 0);
    }

    /**
     *
     * @return returns a string of the election object
     */
    @Override
    public String toString()
    {
        return "Name: " + name + "Description: " + desc + "Number of Candidates: " + num_candidates + " Election is Open: " + active + "You are allowed more than one vote: " + limitVote;
    }
}

