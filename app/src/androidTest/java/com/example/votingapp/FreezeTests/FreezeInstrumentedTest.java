/*
This is the espresso testing class for the freeze functionality story. Each test is for testing the page changes
to actually get to the pages where elections can be frozen.
 */

package com.example.votingapp.FreezeTests;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.MotionEvent;
import android.view.View;

import com.example.votingapp.R;
import com.example.votingapp.RegisterLogin.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class FreezeInstrumentedTest {
    String name = "test@test.com";
    String pass = "testtest";

    @Rule
    public ActivityTestRule<RegisterActivity> banana = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test//This test can fail depending on the wait time since the page changing is dependent on the login authentication time
    //Should this test fail, increase the Thread sleep time.
    public void GoToAdminElectionView() throws Exception{
        Espresso.onView(withId(R.id.textViewSignIn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(name),closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(pass),closeSoftKeyboard());
        Espresso.onView(withId(R.id.buttonLoginAdmin)).perform(ViewActions.click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.buttonViewElection)).perform(ViewActions.click());
    }

    @Test//This test doesnt work, I cant seem to figure out how to have the espresso test interact with the button in the recyclerview
    public void GoToAdminElectionDetail() throws Exception{
        Espresso.onView(withId(R.id.textViewSignIn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(name),closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(pass),closeSoftKeyboard());
        Espresso.onView(withId(R.id.buttonLoginAdmin)).perform(ViewActions.click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.buttonViewElection)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.re_election_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1,ViewActions.click(R.id.goDetails, MotionEvent.BUTTON_PRIMARY)));
        Thread.sleep(3000);


    }

    @Test//This is much like the previous test where I am encountering the problem of dealing with the recyclerview
    public void GetAdminElectionTitle() throws Exception{
        Espresso.onView(withId(R.id.textViewSignIn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText(name),closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText(pass),closeSoftKeyboard());
        Espresso.onView(withId(R.id.buttonLoginAdmin)).perform(ViewActions.click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.buttonViewElection)).perform(ViewActions.click());
        //Espresso.onView(withId(R.id.re_election_view))
        //        .perform(RecyclerViewActions.actionOnItem(
        //                hasDescendant(withText("whatever")), click()));
        Espresso.onView(withId(R.id.goDetails)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        Thread.sleep(3000);
        Espresso.onView(withId(R.id.buttonFreeze)).check(matches(withText("Freeze Election")));
    }
}
