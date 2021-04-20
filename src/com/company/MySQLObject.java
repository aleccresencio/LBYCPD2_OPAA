package com.company;

import javafx.concurrent.Worker;

import java.sql.*;
import java.util.ArrayList;

public class MySQLObject {
    //online database details
    public String url = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6403334";
    public String username = "sql6403334";
    public String password = "ilAP5AHseR";

    public UserObject checkLogin(String user, String pw) {
        //local database details
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
                    int adviser = result.getInt("adviser_id");
                    System.out.println("Welcome " + firstName + " " + lastName + "! You are a " + division);
                    return currentUser = new UserObject(userId, firstName, lastName, email, pass, division, adviser);
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
            //sql query that selects the meetings where the user is involved
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
                    //if statements check whether the user is a student or an adviser and if he/she was the one who set the meeting
                    if (from_id == userId && division.equals("Student")){
                        meetingList.add("You set a private meeting on "+sched);
                    } else if(from_id == userId && division.equals("Adviser") && stopper == 0){
                        //selects only the unique dates of the meetings that the adviser has set
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

    public void sendRequest(int fromId, int toId, String requestSched){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "INSERT INTO requests(from_id, to_id, sched) VALUES(?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fromId);
            statement.setInt(2, toId);
            statement.setString(3, requestSched);
            //executes the statement
            statement.execute();
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void setMeeting(int fromId, String meetingSched){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String getAdviseesQuery = "SELECT user_id FROM users WHERE adviser_id ="+fromId;
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getAdviseesQuery);
            while(results.next()){
                int toId = results.getInt("user_id");
                String sql = "INSERT INTO meetings(from_id, to_id, sched) VALUES(?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, fromId);
                statement.setInt(2, toId);
                statement.setString(3, meetingSched);
                //executes the statement
                statement.execute();
            }
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void removeMeetingRequest(int fromId, int toId, String meetingSched) {
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "DELETE FROM meetings WHERE from_id = ? and to_id = ? and sched = ?";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fromId);
            statement.setInt(2, toId);
            statement.setString(3, meetingSched);
            //executes the statement
            statement.execute();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void removeMeeting(int fromId, String meetingSched) {
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "DELETE FROM meetings WHERE from_id = ? and sched = ?";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fromId);
            statement.setString(2, meetingSched);
            //executes the statement
            statement.execute();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void checkRequests(int adviserId, String meetingSched) {

    }
}