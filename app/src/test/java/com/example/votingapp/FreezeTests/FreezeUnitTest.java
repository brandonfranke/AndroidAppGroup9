/*
I cant seem to come up with other JUnit tests since most of the work on this part is using void methods and UI
stuff, so there isn't much to test here.
 */

package com.example.votingapp.FreezeTests;

import com.example.votingapp.Model.Election;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;

public class FreezeUnitTest {

    @Test
    public void ElectionGetSetActive() {
        Election election = new Election("Name","Name",1);
        assertEquals(election.getActive(),true);
        election.setActive(false);
        assertEquals(election.getActive(),false);
    }

    @Test
    public void ElectionGetSetFreezeDate() throws InterruptedException {
        Election election = new Election("name", "aname", 1);
        assertEquals(election.getFreezeDate(), new Date(0));
        election.setFreezeDate(new Date());
        sleep(1000);
        assertEquals(election.getFreezeDate().compareTo(Calendar.getInstance().getTime())<0, true);
    }
}
