package com.company;

public class UserObject {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int adviser;
    private String division;
    private int user_id;


    public UserObject(int userId, String firstName, String lastName, String email, String password, String division, int adviser){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.division = division;
        this.user_id = userId;
        this.adviser = adviser;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getDivision(){ return division; }
    public int getUser_id(){return user_id;}
    public int getAdviser(){return adviser;}
}
