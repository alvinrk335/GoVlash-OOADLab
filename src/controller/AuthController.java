package controller;

import model.User;
import util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    private Connect connect = Connect.getInstance();

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM MsUser WHERE userName = ? AND userPassword = ?";
        try (Connection conn = connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUserName(rs.getString("userName"));
                user.setUserEmail(rs.getString("userEmail"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setUserGender(rs.getString("userGender"));
                if (rs.getDate("userDOB") != null) {
                    user.setUserDOB(rs.getDate("userDOB").toLocalDate());
                }
                user.setUserRole(rs.getString("userRole"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
