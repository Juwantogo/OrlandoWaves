package com.juwan.orlandowaves.toAccess;

/**
 * Created by Juwan on 9/26/2017.
 */

public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = ""; //change once AWS is up

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "pass";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "owavesapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "MEM_EMAIL";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //If server response is equal to this that means login is successful
    public static final String fName= "fName";

    //If server response is equal to this that means login is successful
    public static final String lName = "lName";

    public static final String fbc = "FBC";
    public static final String auth = "auth";

    ///REPLACE THIS WITH REAL PAYPAL ID
    public static final String PAYPAL_CLIENT_ID = "AVR1Onz_IPH17O1Y0ImRkIjy8ZTHDXdT_6jqGIwZWFHiawYD6Wh4XnSWBBT8TBA7vWxlzGlIsXYy56kO";
}
