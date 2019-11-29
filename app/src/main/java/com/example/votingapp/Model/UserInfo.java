package com.example.votingapp.Model;

import java.io.Serializable;

/**
 * UserInfo is a model class used for User objects
 */
public class UserInfo implements Serializable {

    public String id;
    public String email;
    public String userType;
    public String name;

    /**
     * empty election constructor
     */
    public  UserInfo()
    {

    }

    /**
     * User Constructor
     * @param email sets email param as email for User
     * @param userType sets user type param as user type
     * @param name sets name param as name of user
     */
    public UserInfo(String email, String userType, String name)
    {
        this.email = email;
        this.userType = userType;
        this.name = name;
    }

    /**
     *
     * @return returns user type
     */
    public String getUserType(){
        return userType;
    }

    /**
     *
     * @return returns email
     */
    public String getEmail(){return email; }

    /**
     *
     * @return returns name
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return returns id
     */
    public String getId(){
        return id;
    }

    /**
     *
     * @return returns list of data for a user object
     */
    @Override
    public String toString()
    {
        return "email: " + email + " userType: " + userType + "name: " + name;
    }
}

