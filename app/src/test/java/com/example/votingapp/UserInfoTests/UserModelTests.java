package com.example.votingapp.UserInfoTests;

import com.example.votingapp.Model.UserInfo;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserModelTests {
    static String id;
    static String email;
    static String userType;
    static String name;
    static UserInfo userInfo;

    @BeforeClass
    public static void init()
    {
        id = "123456789";
        email = "test@test.com";
        userType = "Admin";
        name = "Test Name";
        userInfo = new UserInfo("test@email.com", "Admin", "John Doe");
        userInfo.id = "123456789";
    }


    @Test
    public void testEmail()
    {
        Boolean tested = userInfo.getEmail().equals(email);
        assertFalse(tested);
    }

    @Test
    public void testType()
    {
        Boolean tested = userInfo.getUserType().equals(userType);
        assertTrue(tested);
    }

    @Test
    public void testName()
    {
        Boolean tested = userInfo.getName().equals(name);
        assertFalse(tested);
    }

    @Test
    public void testId()
    {
        Boolean tested = userInfo.getId().equals(id);
        assertTrue(tested);
    }

}
