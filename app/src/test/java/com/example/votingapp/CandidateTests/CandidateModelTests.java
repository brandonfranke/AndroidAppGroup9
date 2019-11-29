package com.example.votingapp.CandidateTests;

import com.example.votingapp.Model.Candidate;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CandidateModelTests {
    static String id;
    static String name;
    static String desc;
    static String electionId;
    static String votes;
    static Candidate candidate;

    @BeforeClass
    public static void init()
    {
        id = "123456789";
        name = "John Doe";
        desc = "something here";
        electionId = "223344";
        votes = "3";
        candidate = new Candidate("John Doe","description", "223344", "7");
        candidate.id = "123456789";
    }


    @Test
    public void testId()
    {
        Boolean tested = candidate.getId().equals(id);
        assertTrue(tested);
    }

    @Test
    public void testName()
    {
        Boolean tested = candidate.getName().equals(name);
        assertTrue(tested);
    }

    @Test
    public void testDesc()
    {
        Boolean tested = candidate.getDescription().equals(desc);
        assertFalse(tested);
    }

    @Test
    public void testElectionId()
    {
        Boolean tested = candidate.getelectionId().equals(electionId);
        assertTrue(tested);
    }

    @Test
    public void testVotes()
    {
        Boolean tested = candidate.getVotes().equals(votes);
        assertFalse(tested);
    }

}
