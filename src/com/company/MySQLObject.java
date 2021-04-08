package com.company;

import java.sql.*;
import java.util.ArrayList;

public class MySQLObject {
    public String url = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6403334";
    public String username = "sql6403334";
    public String password = "ilAP5AHseR";

    public UserObject checkLogin(String user, String pw) {
        //database details
//        String url = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6403334";
//        String username = "sql6403334";
//        String password = "ilAP5AHseR";
        UserObject currentUser;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "SELECT * FROM users WHERE email = ? and pw = ?";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setString(2, pw);
            //executes the statement and retrieves results
            ResultSet result = statement.executeQuery();
            //reads the results
            if (result == null) {
                return null;
            } else {
                while (result.next()) {
                    int userId = result.getInt(1);
                    String firstName = result.getString(2);
                    String lastName = result.getString(3);
                    String email = result.getString("email");
                    String pass = result.getString("pw");
                    String division = result.getString("division");
                    System.out.println("Welcome " + firstName + " " + lastName + "! You are a " + division);
                    return currentUser = new UserObject(userId, firstName, lastName, email, pass, division);
                }
            }
            connection.close();
            } catch(SQLException throwables){
                System.out.println("an error has been encountered");
                throwables.printStackTrace();
            }
        return null;
    }

    public ArrayList<String> checkMeetings(int userId, String division){
        ArrayList<String> meetingList = new ArrayList<String>();
        int stopper = 0;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "SELECT * FROM meetings WHERE from_id ="+userId+" or to_id ="+userId+" ORDER BY sched";
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement.executeQuery(sql);
            //reads the results
            if (result == null) {
                return null;
            } else {
                while (result.next()) {
                    int from_id = result.getInt("from_id");
                    int to_id = result.getInt("to_id");
                    String sched = result.getString("sched");
                    if (from_id == userId && division.equals("Student")){
                        meetingList.add("You set a private meeting on "+sched);
                    } else if(from_id == userId && division.equals("Adviser") && stopper == 0){
                        String sql2 = "SELECT DISTINCT sched FROM meetings WHERE from_id IN (SELECT from_id FROM meetings WHERE from_id="+userId+")";
                        //prepares the sql query statement
                        Statement statement2 = connection.createStatement();
                        //executes the statement and retrieves results
                        ResultSet result2 = statement2.executeQuery(sql2);
                        while(result2.next()){
                            String sched2 = result2.getString("sched");
                            meetingList.add("You set a meeting on "+sched2);
                        }
                        stopper = 1;
                    } else if(to_id == userId && division.equals("Student")){
                        meetingList.add("You have a meeting on "+sched);
                    }else if(to_id == userId && division.equals("Adviser")){
                        meetingList.add("You have a private meeting on "+sched);
                    }
                }
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return meetingList;
    }
}