package com.workos.models;

/**
 * Created by Murad on 07/10/2017.
 */

public class UserModel {

    private String username;
    private String gender;
    private byte[] imageData;

    private String email;

    public String getImagePath() {
        return imagePath;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private String password;
    private String phonenumber;
    private String firstname;
    private String lastname;
    private int user_id;
    private String imagePath;
    private String ImageString;
    private boolean chatCreated=false;
    private int chatID;

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }
    public String getFullName(){
        return firstname+" "+lastname;
    }
    public boolean isChatCreated() {
        return chatCreated;

    }

    public void setChatCreated(boolean chatCreated) {
        this.chatCreated = chatCreated;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String setImageString) {
        this.ImageString = setImageString;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserModel() {

    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getUser_id() {
        return user_id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
