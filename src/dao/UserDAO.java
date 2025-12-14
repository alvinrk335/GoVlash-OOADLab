package dao;

import model.Employee;
import model.User;
import util.Connect;

import java.sql.*;
import java.time.LocalDate;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserDAO {

    private final Connect db = Connect.getInstance();
    private final String table_name = "msuser";


    //insert
    public boolean insertUser(
            String userName,
            String userEmail,
            String userPassword,
            String userGender,
            LocalDate userDOB,
            String userRole
    ) {
        String query = "INSERT INTO " + table_name + " (userName, userEmail, userPassword, userGender, userDOB, userRole) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, userName);
            ps.setString(2, userEmail);
            ps.setString(3, userPassword);
            ps.setString(4, userGender);
            ps.setDate(5, userDOB != null ? java.sql.Date.valueOf(userDOB) : null);
            ps.setString(6, userRole);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    //read by id
    public User getUserById(int userID) {
        String query = "SELECT * FROM " + table_name +" WHERE userID = ?";

        try {
        	Connection conn = db.getConnection();
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //read by email
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM " + table_name + " WHERE userEmail = ?";

        try {
        	Connection conn = db.getConnection();
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //read by username
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM " + table_name + " WHERE userName = ?";

        try {
        	Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
				try {
					return mapToUser(rs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    //update 
    public boolean updateUser(User user) {
        String query = "UPDATE " + table_name + " SET userName=?, userEmail=?, userPassword=?, userGender=?, userDOB=?, userRole=? "
                     + "WHERE userID=?";

        try  {
        	Connection conn = db.getConnection();
        	PreparedStatement ps = conn.prepareStatement(query);
        	
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getUserPassword());
            ps.setString(4, user.getUserGender());
            ps.setDate(5, user.getUserDOB() != null ? Date.valueOf(user.getUserDOB()) : null);
            ps.setString(6, user.getUserRole());
            ps.setInt(7, user.getUserID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //delete
    public boolean deleteUser(int userID) {
        String query = "DELETE FROM " + table_name +" WHERE userID=?";

        try {
        	Connection conn = db.getConnection();
        	PreparedStatement ps = conn.prepareStatement(query);
        	
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //search for employee
    public ObservableList<Employee> getAllEmployee(){
    	ObservableList<Employee> e = FXCollections.observableArrayList();
    	String query = "SELECT * FROM " + table_name + " WHERE userRole != 'Customer'";
    	
    	try {
			ResultSet rs = db.execQuery(query);
			
			while(rs.next()) {
				Employee emp = new Employee();
	            emp.setUserID(rs.getInt("userID"));
	            emp.setUserName(rs.getString("userName"));
	            emp.setUserEmail(rs.getString("userEmail"));
	            emp.setUserPassword(rs.getString("userPassword"));
	            emp.setUserGender(rs.getString("userGender"));

	            if (rs.getDate("userDOB") != null) {
	                emp.setUserDOB(rs.getDate("userDOB").toLocalDate());
	            }

	            emp.setUserRole(rs.getString("userRole"));
	            e.add(emp);
			}
			
			
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
    	
    	return e;
    	
    }
    
    //search by role
    public ObservableList<User> getUsersByRole(String role){
    	ObservableList<User> e = FXCollections.observableArrayList();
    	String query = "SELECT * FROM " + table_name + " WHERE userRole ==" + '\'' + role + '\'';
    	
    	try {
			ResultSet rs = db.execQuery(query);
			
			while(rs.next()) {
				Employee emp = new Employee();
	            emp.setUserID(rs.getInt("userID"));
	            emp.setUserName(rs.getString("userName"));
	            emp.setUserEmail(rs.getString("userEmail"));
	            emp.setUserPassword(rs.getString("userPassword"));
	            emp.setUserGender(rs.getString("userGender"));

	            if (rs.getDate("userDOB") != null) {
	                emp.setUserDOB(rs.getDate("userDOB").toLocalDate());
	            }

	            emp.setUserRole(rs.getString("userRole"));
	            e.add(emp);
			}
			
			
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
    	
    	return e;
    	
    }
    
    
    


    // helper
    private User mapToUser(ResultSet rs) throws Exception {
        LocalDate dob = rs.getDate("userDOB") != null ? rs.getDate("userDOB").toLocalDate() : null;

        return new User(
            rs.getInt("userID"),
            rs.getString("userName"),
            rs.getString("userEmail"),
            rs.getString("userPassword"),
            rs.getString("userGender"),
            dob,
            rs.getString("userRole")
        );
    }
    
    public boolean isUsernameOrEmailTaken(String username, String email) {
        try {
            String query = "SELECT COUNT(*) AS cnt FROM MsUser WHERE userName = '" + username + 
                          "' OR userEmail = '" + email + "'";
            ResultSet rs = db.execQuery(query);
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;  
    }
}
