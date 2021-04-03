package com.company;

import java.awt.desktop.SystemEventListener;
import java.sql.*;

public class mySQLTest {
    public static void main(String[] args){
        String url = "jdbc:mysql://localhost:3306/opaa_database";
        String username = "root";
        String password = "password";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            int count = 1;
            while(result.next()){
                int userId = result.getInt(1);
                String firstName = result.getString(2);
                String email = result.getString("email");
                String division = result.getString("division");
                System.out.println("Customer "+count+": "+firstName+" Email: "+email+" Division: "+division+" User Id: "+userId);
                count += 1;
            }
            connection.close();
        } catch (SQLException throwables) {
            System.out.println("an error has been encountered");
            throwables.printStackTrace();
        }
    }
}
