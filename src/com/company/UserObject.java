package com.company;

public class UserObject {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String adviser;
    private String division;


    public UserObject(String firstName, String lastName, String email, String password, String division){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.division = division;
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
    public String getDivision(){
        return division;
    }
}
