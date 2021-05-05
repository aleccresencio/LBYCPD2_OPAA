package com.company;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
                        meetingList.add("You set a private meeting on "+sched.substring(0,8)+" at "+sched.substring(9));
                    } else if(from_id == userId && division.equals("Adviser") && stopper == 0){
                        //selects only the unique dates of the meetings that the adviser has set
                        String sql2 = "SELECT DISTINCT sched FROM meetings WHERE from_id IN (SELECT from_id FROM meetings WHERE from_id="+userId+")";
                        //prepares the sql query statement
                        Statement statement2 = connection.createStatement();
                        //executes the statement and retrieves results
                        ResultSet result2 = statement2.executeQuery(sql2);
                        while(result2.next()){
                            String sched2 = result2.getString("sched");
                            meetingList.add("You set a meeting on "+sched2.substring(0,8)+" at "+sched2.substring(9));
                        }
                        stopper = 1;
                    } else if(to_id == userId && division.equals("Student")){
                        meetingList.add("You have a meeting on "+sched.substring(0,8)+" at "+sched.substring(9));
                    }else if(to_id == userId && division.equals("Adviser")){
                        String sql3 = "SELECT first_name, last_name FROM users WHERE user_id="+from_id;
                        //prepares the sql query statement
                        Statement statement3 = connection.createStatement();
                        //executes the statement and retrieves results
                        ResultSet result3 = statement3.executeQuery(sql3);
                        while(result3.next()){
                            String firstName = result3.getString("first_name");
                            String lastName = result3.getString("last_name");
                            meetingList.add("You have a private meeting with "+firstName+" "+lastName+" on "+sched.substring(0,8)+" at "+sched.substring(9));
                        }
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

    public String sendRequest(int fromId, int toId, String requestSched){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String checkSched = "SELECT sched FROM requests WHERE sched ='"+requestSched+"' AND from_id ="+fromId;
            Statement checkStatement = connection.createStatement();
            ResultSet results1 = checkStatement.executeQuery(checkSched);
            if(results1.next()){
                return "You already sent a meeting request scheduled on the same date and time";
            }
            //sql query that inserts data into the request table
            String sql = "INSERT INTO requests(from_id, to_id, sched) VALUES(?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, fromId);
            statement.setInt(2, toId);
            statement.setString(3, requestSched);
            //executes the statement
            statement.execute();
            String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1, fromId);
            statement1.setInt(2, toId);
            statement1.setString(3, requestSched);
            statement1.setString(4, "request");
            //executes the statement
            statement1.execute();
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return "Succesfully sent a meeting request!";
    }

    public String setMeeting(int fromId, String meetingSched){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String checkSched = "SELECT sched FROM meetings WHERE sched ='"+meetingSched+"' AND from_id ="+fromId;
            Statement checkStatement = connection.createStatement();
            ResultSet results1 = checkStatement.executeQuery(checkSched);
            if(results1.next()){
                return "You already have a meeting scheduled on the same date and time";
            }
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
                String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                statement1.setInt(1, fromId);
                statement1.setInt(2, toId);
                statement1.setString(3, meetingSched);
                statement1.setString(4, "meeting");
                //executes the statement
                statement1.execute();
            }
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return "Succesfully set a meeting";
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
            String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1, fromId);
            statement1.setInt(2, toId);
            statement1.setString(3, meetingSched);
            statement1.setString(4, "removed");
            //executes the statement
            statement1.execute();
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
            String getAdviseesQuery = "SELECT user_id FROM users WHERE adviser_id ="+fromId;
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getAdviseesQuery);
            while(results.next()) {
                int toId = results.getInt("user_id");
                String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                statement1.setInt(1, fromId);
                statement1.setInt(2, toId);
                statement1.setString(3, meetingSched);
                statement1.setString(4, "removed");
                //executes the statement
                statement1.execute();
            }
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public ObservableList<RequestObject> checkRequests(int adviserId) {
        ObservableList<RequestObject> requestList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that selects the meetings where the user is involved
            String sql = "SELECT * FROM requests WHERE to_id ="+adviserId+" and accept_denied IS NULL ORDER BY sched";
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement.executeQuery(sql);
            //reads the results
            if (result == null) {
                return null;
            } else {
                while (result.next()) {
                    int request_id = result.getInt("request_id");
                    int from_id = result.getInt("from_id");
                    int to_id = result.getInt("to_id");
                    String sched = result.getString("sched");
                    //if statements check whether the user is a student or an adviser and if he/she was the one who set the meeting
                    String sql2 = "SELECT first_name, last_name FROM users WHERE user_id ="+from_id;
                    //prepares the sql query statement
                    Statement statement2 = connection.createStatement();
                    //executes the statement and retrieves results
                    ResultSet result2 = statement2.executeQuery(sql2);
                    while(result2.next()){
                        String firstName = result2.getString("first_name");
                        String lastName = result2.getString("last_name");
                        requestList.add(new RequestObject(request_id, from_id, to_id, firstName, lastName, sched.substring(0,9), sched.substring(sched.length()-5)));
                    }
                }
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return requestList;
    }

    public void acceptRequest(int requestId, int from_id, int to_id, String date, String time) {
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "UPDATE requests SET accept_denied = 'accept' WHERE request_id ="+requestId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement
            statement.execute(sql);
            String sched = date + time;
            String sql2 = "INSERT INTO meetings(from_id, to_id, sched) VALUES(?,?,?)";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, from_id);
            statement2.setInt(2, to_id);
            statement2.setString(3, sched);
            statement2.execute();
            String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1, from_id);
            statement1.setInt(2, to_id);
            statement1.setString(3, sched);
            statement1.setString(4, "accepted");
            //executes the statement
            statement1.execute();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void declineRequest(int requestId, int from_id, int to_id, String date, String time) {
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "UPDATE requests SET accept_denied = 'denied' WHERE request_id ="+requestId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement
            statement.execute(sql);
            String sched = date + time;
            String sql1 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setInt(1, from_id);
            statement1.setInt(2, to_id);
            statement1.setString(3, sched);
            statement1.setString(4, "declined");
            //executes the statement
            statement1.execute();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public ArrayList<String> studentNotifs(int userId) {
        ArrayList<String> notifList = new ArrayList<String>();
        int stopper = 0;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that selects the meetings where the user is involved
            String sql = "SELECT * FROM notifications WHERE from_id ="+userId+" or to_id ="+userId+" ORDER BY sched";
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
                    String identifier = result.getString("identifier");
                    String sched = result.getString("sched");
                    //if statements check whether the user is a student or an adviser and if he/she was the one who set the meeting
                    if (to_id == userId && identifier.equals("removed")){
                        String sql1 = "SELECT division, first_name, last_name FROM users WHERE user_id = "+userId;
                        //prepares the sql query statement
                        Statement statement1 = connection.createStatement();
                        //executes the statement and retrieves results
                        ResultSet result1 = statement1.executeQuery(sql1);
                        while (result1.next()) {
                            String division = result1.getString("division");
                            String firstName = result1.getString("first_name");
                            String lastName = result1.getString("last_name");
                            if(division.equals("Student")) {
                                notifList.add("Your academic adviser cancelled the meeting scheduled on " + sched.substring(0, 8) + " at " + sched.substring(9));
                            }else if(division.equals("Adviser")){
                                notifList.add(firstName+" "+lastName+" cancelled the meeting scheduled on " + sched.substring(0, 8) + " at " + sched.substring(9));
                            }
                        }
                    } else if (from_id == userId && identifier.equals("accepted")){
                        notifList.add("Your meeting request for "+sched.substring(0,8)+" at "+sched.substring(9)+" was accepted.");
                    } else if (from_id == userId && identifier.equals("declined")){
                        notifList.add("Your meeting request for "+sched.substring(0,8)+" at "+sched.substring(9)+" was declined.");
                    } else if (to_id == userId && identifier.equals("meeting")){
                        notifList.add("Your academic adviser has set a meeting for "+sched.substring(0,8)+" at "+sched.substring(9));
                    } else if(to_id == userId && identifier.equals("request")){
                        String sql1 = "SELECT first_name, last_name FROM users WHERE user_id = "+from_id;
                        //prepares the sql query statement
                        Statement statement1 = connection.createStatement();
                        //executes the statement and retrieves results
                        ResultSet result1 = statement1.executeQuery(sql1);
                        while (result1.next()) {
                            String firstName = result1.getString("first_name");
                            String lastName = result1.getString("last_name");
                            notifList.add(firstName+" "+lastName+" has requested a meeting for " + sched.substring(0, 8) + " at " + sched.substring(9));
                        }
                    }
                }
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return notifList;
    }

    public ObservableList<UserObject> getAdvisers() {
        ObservableList<UserObject> adviserList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that selects the meetings where the user is involved
            String sql = "SELECT * FROM users WHERE division = 'Adviser'";
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement.executeQuery(sql);
            //reads the results
            if (result == null) {
                return null;
            } else {
                while (result.next()) {
                    int user_id = result.getInt("user_id");
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    String email = result.getString("email");
                    int adviser = result.getInt("adviser_id");
                    String password = result.getString("pw");
                    adviserList.add(new UserObject(user_id, first_name, last_name, email, password, "Adviser", adviser));
                }
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return adviserList;
    }
    int newUserId;
    public void addStudent(String firstName, String lastName, String email, int adviser){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that inserts data into the request table
            String sql = "INSERT INTO users(first_name, last_name, email, division, adviser_id) VALUES(?,?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, "Student");
            statement.setInt(5, adviser);
            //executes the statement
            statement.execute();
            String sql1 = "SELECT LAST_INSERT_ID()";
            //prepares the sql query statement
            Statement statement1 = connection.createStatement();
            //executes the statement
            ResultSet result1 = statement1.executeQuery(sql1);
            while (result1.next()){
                newUserId = result1.getInt(1);
            }
            String sql2 = "SELECT DISTINCT sched FROM meetings WHERE from_id IN (SELECT from_id FROM meetings WHERE from_id="+adviser+")";
            //prepares the sql query statement
            Statement statement2 = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result2 = statement2.executeQuery(sql2);
            while(result2.next()){
                String sched2 = result2.getString("sched");
                String sql3 = "INSERT INTO meetings(from_id, to_id, sched) VALUES(?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement3 = connection.prepareStatement(sql3);
                statement3.setInt(1, adviser);
                statement3.setInt(2, newUserId);
                statement3.setString(3, sched2);
                //executes the statement
                statement3.execute();
                String sql4 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement4 = connection.prepareStatement(sql4);
                statement4.setInt(1, adviser);
                statement4.setInt(2, newUserId);
                statement4.setString(3, sched2);
                statement4.setString(4, "meeting");
                //executes the statement
                statement4.execute();
            }
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }
}