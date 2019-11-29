package com.example.votingapp.ElectionTests;

import com.example.votingapp.Model.Election;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ElectionModelTests {
    static String id;
    static String userID;
    static String userID1;
    static String name;
    static String desc;
    static int numCand;
    static boolean frozen;
    static boolean limitVote;
    static Election election;

    @BeforeClass
    public static void init()
    {
        id = "123456789";
        userID = "sd234gsdg3124";
        userID1 = "sd234gsdg3122324";
        name = "dsuElections";
        desc = "description";
        numCand = 2;
        frozen = true;
        limitVote = false;
        election = new Election("dsuElections", "description", 3);
        election.id = "123456789";
    }


    @Test
    public void testName()
    {
        Boolean tested = election.getTitle().equals(name);
        assertTrue(tested);
    }

    @Test
    public void testDesc()
    {
        Boolean tested = election.getDesc().equals(desc);
        assertTrue(tested);
    }

    @Test
    public void testNumCand()
    {
        Boolean tested = election.getNumCandidates() == numCand;
        assertFalse(tested);
    }

    @Test
    public void testActive()
    {
        Boolean tested = election.getActive() == frozen;
        assertTrue(tested);
    }

    @Test
    public void testId()
    {
        Boolean tested = election.getId() == id;
        assertTrue(tested);
    }

    @Test
    public void testLimitVote()
    {
        Boolean tested = election.getLimitVote() == limitVote;
        assertTrue(tested);
    }

    @Test
    public void testCheckId()
    {
        Boolean tested = election.checkID(userID1);
        assertFalse(tested);
    }

    @Test
    public void testAddUser()
    {
        election.addUser(userID);
        Boolean tested = election.checkID(userID);
        assertTrue(tested);
    }

}
