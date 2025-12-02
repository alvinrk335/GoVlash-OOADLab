package controller;

import model.Customer;
import util.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    private Connect connect = Connect.getInstance();

    public boolean isUsernameOrEmailTaken(String username, String email) {
        try {
            String query = "SELECT COUNT(*) AS cnt FROM MsUser WHERE userName = '" + username + 
                          "' OR userEmail = '" + email + "'";
            ResultSet rs = connect.execQuery(query);
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;  
    }

    public boolean registerCustomer(Customer c) {
        if (c == null || !c.isValidUser()) {
            return false;
        }

        if (isUsernameOrEmailTaken(c.getUserName(), c.getUserEmail())) {
            return false;
        }

        try {
            String query = "INSERT INTO MsUser (userName,userEmail,userPassword,userGender,userDOB,userRole) VALUES ('" +
                          c.getUserName() + "', '" + c.getUserEmail() + "', '" + c.getUserPassword() + "', '" +
                          c.getUserGender() + "', '" + c.getUserDOB() + "', '" + c.getUserRole() + "')";
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
