package com.example.rajivtiwari.cms;


/**
 * Created by rajivtiwari on 12/9/17.
 */

public class UserDetails {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String LoginType;
    private String linkedNumber;

    public UserDetails(String firstName, String lastName, String phoneNumber, String loginType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        LoginType = loginType;
        this.linkedNumber = null;
    }

    public String getLinkedNumber() {  return linkedNumber; }

    public void setLinkedNumber(String linkedNumber) {  this.linkedNumber = linkedNumber;    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLoginType() { return LoginType; }

    public void setFirstName(String firstName) { this.firstName = firstName;}

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLoginType(String loginType) {
        LoginType = loginType;
    }

}
