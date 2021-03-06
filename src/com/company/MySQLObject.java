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

    public void updatePassword (String newPw, int id) {
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered password is in the database
            String sql = "UPDATE users SET pw = ? WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newPw);
            statement.setInt(2, id);
            //executes the statement and retrieves results
            statement.execute();

        } catch(SQLException throwable){
            System.out.println("an error has been encountered");
            throwable.printStackTrace();
        }
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
            String checkStudents = "SELECT user_id FROM users WHERE adviser_id ="+fromId;
            Statement checkStatement1 = connection.createStatement();
            ResultSet results2 = checkStatement1.executeQuery(checkStudents);
            if(results2.next() == false){
                return "You do not have any assigned students yet.";
            }
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
    public boolean addStudent(String firstName, String lastName, String email, int adviser){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String checkEmails = "SELECT email FROM users WHERE email ='"+email+"'";
            Statement checkStatement1 = connection.createStatement();
            ResultSet results2 = checkStatement1.executeQuery(checkEmails);
            if(results2.next()){
                System.out.println("ERROR");
                return false;
            }
            //sql query that inserts new student into the users table
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
        return true;
    }

    public int returnLastId(){
        return newUserId;
    }

    public void addGrades(int userId, String course, String grade){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "SELECT user_id FROM grades WHERE user_id ="+userId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement
            ResultSet result = statement.executeQuery(sql);
            if(result.next() == false){
                String sql1 = "INSERT INTO grades(user_id, course_names, grade_values) VALUES("+userId+",'"+course+"','"+grade+"')";
                //prepares the sql query statement
                Statement statement1 = connection.createStatement();
                statement1.execute(sql1);
            }else{
                String sql2 = "SELECT course_names, grade_values FROM grades WHERE user_id ="+userId;
                //prepares the sql query statement
                Statement statement2 = connection.createStatement();
                //executes the statement
                ResultSet result2 = statement2.executeQuery(sql2);
                while(result2.next()){
                    String courses = result2.getString("course_names");
                    String grades = result2.getString("grade_values");
                    String sql3 = "UPDATE grades SET course_names = '"+courses+", "+course+"', grade_values = '"+grades+", "+grade+"' WHERE user_id = "+userId;
                    //prepares the sql query statement
                    Statement statement3 = connection.createStatement();
                    statement3.execute(sql3);
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void removeGrades(int userId, String courseNames, String courseGrades){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "UPDATE grades SET course_names = '"+courseNames+"', grade_values = '"+courseGrades+"' WHERE user_id = "+userId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement
            statement.execute(sql);
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public ObservableList<UserObject> getStudents(int userId){
        ObservableList<UserObject> studentList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String getAdviseesQuery = "SELECT user_id, first_name, last_name FROM users WHERE adviser_id ="+userId;
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getAdviseesQuery);
            while(results.next()) {
                int studentId = results.getInt("user_id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                studentList.add(new UserObject(studentId, firstName, lastName));
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return studentList;
    }

    public String getCourseNamesOrGrades(int user_id, String choice){
        String returnString = null;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String getGrades = "SELECT * FROM grades WHERE user_id ="+user_id;
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getGrades);
            while(results.next()) {
                if(choice.equals("names")){
                    String courseNames = results.getString("course_names");
                    returnString = courseNames;
                } else if(choice.equals("grades")){
                    String courseGrades = results.getString("grade_values");
                    returnString = courseGrades;
                }
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return returnString;
    }

    public boolean addAdviser(String firstName, String lastName, String email){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String checkEmails = "SELECT email FROM users WHERE email ='"+email+"'";
            Statement checkStatement1 = connection.createStatement();
            ResultSet results2 = checkStatement1.executeQuery(checkEmails);
            if(results2.next()){
                System.out.println("ERROR");
                return false;
            }
            //sql query that inserts new student into the users table
            String sql = "INSERT INTO users(first_name, last_name, email, division) VALUES(?,?,?,?)";
            //prepares the sql query statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, "Adviser");
            //executes the statement
            statement.execute();
            //closes connection
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return true;
    }

    public ObservableList<UserObject> getAllStudents(){
        ObservableList<UserObject> studentList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that gets all of the students in the database
            String getStudentsQuery = "SELECT user_id, first_name, last_name, adviser_id FROM users WHERE division = 'Student'";
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getStudentsQuery);
            while(results.next()) {
                int studentId = results.getInt("user_id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                int adviserId = results.getInt("adviser_id");
                studentList.add(new UserObject(studentId, firstName, lastName, adviserId));
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return studentList;
    }

    public ObservableList<UserObject> getPossibleAdvisers(int adviserId) {
        ObservableList<UserObject> adviserList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that selects the meetings where the user is involved
            String sql = "SELECT * FROM users WHERE division = 'Adviser' && user_id !="+adviserId;
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

    public String getAdviserName(int adviserId) {
        String adviserName = null;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that selects the meetings where the user is involved
            String sql = "SELECT first_name, last_name FROM users WHERE user_id ="+adviserId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement.executeQuery(sql);
            //reads the results
            if (result == null) {
                return null;
            } else {
                while (result.next()) {
                    String first_name = result.getString("first_name");
                    String last_name = result.getString("last_name");
                    adviserName = first_name+" "+last_name;
                    return adviserName;
                }
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return adviserName;
    }

    public void changeAdviser(int userId, int newAdviserId){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that updates the adviser id of the student in the database
            String sql = "UPDATE users SET adviser_id = "+newAdviserId+" WHERE user_id = "+userId;
            String sql1 = "DELETE FROM meetings WHERE from_id = "+userId+" OR to_id = "+userId;
            String sql2 = "DELETE FROM notifications WHERE from_id = "+userId+" OR to_id = "+userId;
            String sql3 = "DELETE FROM requests WHERE from_id ="+userId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            statement.addBatch(sql);
            statement.addBatch(sql1);
            statement.addBatch(sql2);
            statement.addBatch(sql3);
            //executes the statement and retrieves results
            statement.executeBatch();
            //adds meetings that were set by the new adviser
            String sql4 = "SELECT DISTINCT sched FROM meetings WHERE from_id IN (SELECT from_id FROM meetings WHERE from_id="+newAdviserId+")";
            //prepares the sql query statement
            Statement statement2 = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result2 = statement2.executeQuery(sql4);
            while(result2.next()){
                String sched2 = result2.getString("sched");
                String sql5 = "INSERT INTO meetings(from_id, to_id, sched) VALUES(?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement3 = connection.prepareStatement(sql5);
                statement3.setInt(1, newAdviserId);
                statement3.setInt(2, userId);
                statement3.setString(3, sched2);
                //executes the statement
                statement3.execute();
                String sql6 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
                //prepares the sql query statement
                PreparedStatement statement4 = connection.prepareStatement(sql6);
                statement4.setInt(1, newAdviserId);
                statement4.setInt(2, userId);
                statement4.setString(3, sched2);
                statement4.setString(4, "meeting");
                //executes the statement
                statement4.execute();
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public ObservableList<EventObject> getStudentEvents(int userId, String date){
        ObservableList<EventObject> eventList = FXCollections.observableArrayList();
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that gets all of the students in the database
            String getMeetings = "SELECT * FROM meetings WHERE (from_id = "+userId+" OR to_id = "+userId+") AND sched LIKE '"+date+"%'";
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getMeetings);
            while(results.next()) {
                int fromId = results.getInt("from_id");
                int toId = results.getInt("to_id");
                String schedule = results.getString("sched");
                String time = schedule.substring(schedule.length()-5);
                if(fromId == userId){
                    eventList.add(new EventObject(time, "Private meeting with adviser"));
                }else if(toId == userId){
                    eventList.add(new EventObject(time, "Meeting with adviser"));
                }
            }
            String getEvents = "SELECT * FROM events WHERE user_id ="+userId+" AND sched LIKE '"+date+"%'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getEvents);
            while(result.next()) {
                int eventId = result.getInt("event_id");
                String title = result.getString("event_title");
                String schedule = result.getString("sched");
                String time = schedule.substring(schedule.length()-5);
                eventList.add(new EventObject(eventId, time, title));
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return eventList;
    }

    public ObservableList<EventObject> getAdviserEvents(int userId, String date){
        ObservableList<EventObject> eventList = FXCollections.observableArrayList();
        int counter = 0;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that gets all of the students in the database
            String getMeetings = "SELECT * FROM meetings WHERE (from_id = "+userId+" OR to_id = "+userId+") AND sched LIKE '"+date+"%'";
            Statement firstStatement = connection.createStatement();
            ResultSet results = firstStatement.executeQuery(getMeetings);
            while(results.next()) {
                int fromId = results.getInt("from_id");
                int toId = results.getInt("to_id");
                String schedule = results.getString("sched");
                String time = schedule.substring(schedule.length()-5);
                if(fromId == userId && counter == 0){
                    eventList.add(new EventObject(time, "Meeting with advisees"));
                    counter = 1;
                }else if(toId == userId){
                    MySQLObject sql = new MySQLObject();
                    String name = sql.getAdviserName(fromId);
                    eventList.add(new EventObject(time, "Meeting with "+name));
                }
            }
            String getEvents = "SELECT * FROM events WHERE user_id ="+userId+" AND sched LIKE '"+date+"%'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(getEvents);
            while(result.next()) {
                int eventId = result.getInt("event_id");
                String title = result.getString("event_title");
                String schedule = result.getString("sched");
                String time = schedule.substring(schedule.length()-5);
                eventList.add(new EventObject(eventId, time, title));
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return eventList;
    }

    public int addEvent(int userId, String title, String schedule){
        int newEventId = 0;
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that inserts the event in the database
            String sql = "INSERT INTO events(user_id, sched, event_title) VALUES("+userId+",'"+schedule+"','"+title+"')";
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement and retrieves results
            statement.execute(sql);
            String sql1 = "SELECT max(event_id) FROM events";
            //prepares the sql query statement
            Statement statement1 = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement1.executeQuery(sql1);
            while(result.next()){
                newEventId = result.getInt(1);
                return newEventId;
            }
            connection.close();
        } catch(SQLException throwables){
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
        return newEventId;
    }

    public void removeEvent(int eventId){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "DELETE FROM events WHERE event_id ="+eventId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            //executes the statement
            statement.execute(sql);
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void deleteStudent(int userId){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            //sql query that checks if the entered username and password is in the database
            String sql = "DELETE FROM users WHERE user_id ="+userId;
            String sql1 = "DELETE FROM meetings WHERE from_id ="+userId+" OR to_id ="+userId;
            String sql2 = "DELETE FROM requests WHERE from_id ="+userId;
            String sql3 = "DELETE FROM events WHERE user_id ="+userId;
            String sql4 = "DELETE FROM notifications WHERE from_id ="+userId+" OR to_id ="+userId;
            String sql5 = "DELETE FROM grades WHERE user_id ="+userId;
            //prepares the sql query statement
            Statement statement = connection.createStatement();
            statement.addBatch(sql);
            statement.addBatch(sql1);
            statement.addBatch(sql2);
            statement.addBatch(sql3);
            statement.addBatch(sql4);
            statement.addBatch(sql5);
            //executes the statement and retrieves results
            statement.executeBatch();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }

    public void deleteAdviser(int adviserId, int replacerId){
        try {
            //establishes a connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            String getStudents = "SELECT user_id FROM users WHERE adviser_id ="+adviserId;
            Statement statement1 = connection.createStatement();
            //executes the statement and retrieves results
            ResultSet result = statement1.executeQuery(getStudents);
            while(result.next()){
                int userId = result.getInt(1);
                //sql query that updates the adviser id of the student in the database
                String sql = "UPDATE users SET adviser_id = "+replacerId+" WHERE user_id = "+userId;
                //prepares the sql query statement
                Statement statement = connection.createStatement();
                //executes the statement and retrieves results
                statement.execute(sql);
                //adds meetings that were set by the new adviser
                String sql4 = "SELECT DISTINCT sched FROM meetings WHERE from_id IN (SELECT from_id FROM meetings WHERE from_id="+replacerId+")";
                //prepares the sql query statement
                Statement statement2 = connection.createStatement();
                //executes the statement and retrieves results
                ResultSet result2 = statement2.executeQuery(sql4);
                while(result2.next()){
                    String sched2 = result2.getString("sched");
                    String sql5 = "INSERT INTO meetings(from_id, to_id, sched) VALUES(?,?,?)";
                    //prepares the sql query statement
                    PreparedStatement statement3 = connection.prepareStatement(sql5);
                    statement3.setInt(1, replacerId);
                    statement3.setInt(2, userId);
                    statement3.setString(3, sched2);
                    //executes the statement
                    statement3.execute();
                    String sql6 = "INSERT INTO notifications(from_id, to_id, sched, identifier) VALUES(?,?,?,?)";
                    //prepares the sql query statement
                    PreparedStatement statement4 = connection.prepareStatement(sql6);
                    statement4.setInt(1, replacerId);
                    statement4.setInt(2, userId);
                    statement4.setString(3, sched2);
                    statement4.setString(4, "meeting");
                    //executes the statement
                    statement4.execute();
                }
            }
            //sql query that checks if the entered username and password is in the database
            String sql00 = "DELETE FROM users WHERE user_id ="+adviserId;
            String sql01 = "DELETE FROM meetings WHERE from_id ="+adviserId+" OR to_id ="+adviserId;
            String sql02 = "DELETE FROM requests WHERE to_id ="+adviserId;
            String sql03 = "DELETE FROM events WHERE user_id ="+adviserId;
            String sql04 = "DELETE FROM notifications WHERE from_id ="+adviserId+" OR to_id ="+adviserId;
            //prepares the sql query statement
            Statement statement01 = connection.createStatement();
            statement01.addBatch(sql00);
            statement01.addBatch(sql01);
            statement01.addBatch(sql02);
            statement01.addBatch(sql03);
            statement01.addBatch(sql04);
            //executes the statement and retrieves results
            statement01.executeBatch();
            //closes connection
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }
}