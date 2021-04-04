package com.company;

import java.sql.*;

public class LoginObject {
    public UserObject checkLogin(String user, String pw) {
        //database details
        String url = "jdbc:mysql://localhost:3306/opaa_database";
        String username = "root";
        String password = "password";
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
}