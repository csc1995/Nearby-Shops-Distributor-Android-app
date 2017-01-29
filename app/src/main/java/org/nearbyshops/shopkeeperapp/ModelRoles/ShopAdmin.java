package org.nearbyshops.shopkeeperapp.ModelRoles;

import java.sql.Timestamp;

/**
 * Created by sumeet on 14/6/16.
 */
public class ShopAdmin {

    // Note : ShopAdmin has one to one relationship with Shop therefore the columns of ShopAdmin has been
    // merged inside the Shop table for preserving data and relationship consistency.
    // The entity remains distinct and not the same as Shop which in simple terms imply that although both
    // entity columns are merged in one table the entity remains distinct.

    // Table Name : Table does not exist for ShopAdmin because the columns are merged in Shop Table

    // Table Name
    public static final String TABLE_NAME = "SHOP_ADMIN";

    // column Names
    public static final String SHOP_ADMIN_ID = "SHOP_ADMIN_ID";
    public static final String NAME = "NAME";

//    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String ABOUT = "ABOUT";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String DESIGNATION = "DESIGNATION";

    // to be Implemented
    public static final String IS_ENABLED = "IS_ENABLED";
    public static final String IS_WAITLISTED = "IS_WAITLISTED";

    public static final String ACCOUNT_PRIVATE = "ACCOUNT_PRIVATE";

    public static final String GOVERNMENT_ID_NUMBER = "GOVERNMENT_ID_NUMBER";
    public static final String GOVERNMENT_ID_NAME = "GOVERNMENT_ID_NAME";
    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";



    // create table statement
    public static final String createtableShopAdminPostgres
            = "CREATE TABLE IF NOT EXISTS " + ShopAdmin.TABLE_NAME + "("
            + " " + ShopAdmin.SHOP_ADMIN_ID + " INT UNIQUE NOT NULL,"
            + " " + ShopAdmin.NAME + " text,"

            + " " + ShopAdmin.PASSWORD + " text NOT NULL,"

            + " " + ShopAdmin.ABOUT + " text,"
            + " " + ShopAdmin.PROFILE_IMAGE_URL + " text,"
            + " " + ShopAdmin.PHONE_NUMBER + " text,"
            + " " + ShopAdmin.DESIGNATION + " text,"

            + " " + ShopAdmin.IS_ENABLED + " boolean NOT NULL,"
            + " " + ShopAdmin.IS_WAITLISTED + " boolean NOT NULL,"

            + " " + ShopAdmin.ACCOUNT_PRIVATE + " boolean,"

            + " " + ShopAdmin.GOVERNMENT_ID_NAME + " text,"
            + " " + ShopAdmin.GOVERNMENT_ID_NUMBER + " text,"
            + " " + ShopAdmin.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
//            + " FOREIGN KEY(" + ShopAdmin.SHOP_ADMIN_ID +") REFERENCES " + Usernames.TABLE_NAME + "(" + Usernames.USER_ID + ")"
            + ")";



    // instance Variables

    private int shopAdminID;
    private String name;
//    private Integer shopID;

    private String username;
    private String password;

    private String about;
    private String profileImageURL;
    private String phoneNumber;

    private String designation;

    private Boolean isEnabled;
    private Boolean isWaitlisted;

    private boolean accountPrivate;

    private String govtIDName;
    private String govtIDNumber;
    private Timestamp timestampCreated;


    // getter and setters


    public boolean isAccountPrivate() {
        return accountPrivate;
    }

    public void setAccountPrivate(boolean accountPrivate) {
        this.accountPrivate = accountPrivate;
    }

    public String getGovtIDName() {
        return govtIDName;
    }

    public void setGovtIDName(String govtIDName) {
        this.govtIDName = govtIDName;
    }

    public String getGovtIDNumber() {
        return govtIDNumber;
    }

    public void setGovtIDNumber(String govtIDNumber) {
        this.govtIDNumber = govtIDNumber;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public int getShopAdminID() {
        return shopAdminID;
    }

    public void setShopAdminID(int shopAdminID) {
        this.shopAdminID = shopAdminID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getWaitlisted() {
        return isWaitlisted;
    }

    public void setWaitlisted(Boolean waitlisted) {
        isWaitlisted = waitlisted;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
