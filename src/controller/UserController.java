package controller;

import model.Customer;
import model.Employee;
import model.User;
import util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import dao.UserDAO;
import javafx.collections.ObservableList;

public class UserController {

    private Connect connect = Connect.getInstance();
    private UserDAO dao = new UserDAO();

    public void addCustomer(String name, String email, String password,String gender, LocalDate dob) {
    	boolean inserted = dao.insertUser(name, email, password, gender, dob, "Customer");
    	System.out.println(inserted);
    }
    
    public User login(String username, String password) {
        User user = dao.getUserByUsername(username);
        
        if(user == null) return null;
        
        if(!user.getUserPassword().equals(password)) return null;
        
        return user;
    }
    
    public void addEmployee(String name, String email, String password,String gender, LocalDate dob, String role) {
    	boolean inserted = dao.insertUser(name, email, password, gender, dob, role);
    	System.out.println(inserted);
    }
    
    public ObservableList<Employee> getAllEmployees(){
    	ObservableList<Employee> e = dao.getAllEmployee();
    	return e;
    }
    
    public ObservableList<User> getUsersByRole(String role){
    	ObservableList<User> u =  dao.getUsersByRole(role);
    	return u;
    }
    
    
    public String validateAddEmployee(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
        if(name == null || name.isBlank() ||
        	       email == null || email.isBlank() ||
        	       password == null || password.isBlank() ||
        	       confirmPassword == null || confirmPassword.isBlank() ||
        	       gender == null || gender.isBlank() ||
        	       dob == null ||
        	       role == null || role.isBlank()) 
        {
        	return "all fields must be filled";
        }
    	if(isUsernameOrEmailTaken(name, email)) {
    		return "email or username has been taken";
    	}
    	if(!validatePasswordEmployee(password)) {
    		return "password must be more than 6 char";
    	}
    	if(!validateConfirmPasswordEmployee(password, confirmPassword)) {
    		return "confim password must be the same as password";
    	}
    	if(!validateGenderEmployee(gender)) {
    		return "gender must be either \"Male\" or \"Female\"";
    	}
    	if(!validateDOBEmployee(dob)) {
    		return "user must be 17 years old or above";
    	}
    	if(!validateRoleEmployee(role)) {
    		return "role Must be either “Admin”, “Laundry Staff”, or “Receptionist”";
    	}
    	
    	return "employee registered, go back to login to continue";
    }
    
    public String validateAddCustomer(String name, String email, String password, String confirmPassword, String gender, LocalDate dob) {
        if(name == null || name.isBlank() ||
        	       email == null || email.isBlank() ||
        	       password == null || password.isBlank() ||
        	       confirmPassword == null || confirmPassword.isBlank() ||
        	       gender == null || gender.isBlank() ||
        	       dob == null) 
        {
        	return "all fields must be filled";
        }
        if (isUsernameOrEmailTaken(name, email)) {
            return "email or username has been taken";
        }
        if(!validateUserEmailCustomer(email)) {
        	return "email must ends with @email.com";
        }
        if (!validatePasswordCustomer(password)) {
            return "password must be at least 6 characters long";
        }
        if (!validateConfirmPasswordCustomer(password, confirmPassword)) {
            return "confirm password must be the same as password";
        }
        if (!validateGenderCustomer(gender)) {
            return "gender must be either \"Male\" or \"Female\"";
        }
        if (!validateDOBCustomer(dob)) {
            return "user must be 12 years old or above";
        }

        return "customer registered, go back to login to continue";
    }
    
    public User getUserByEmail(String email) {
    	User user = dao.getUserByEmail(email);
    	return user;
    }
    
    public User getUserByName(String userName) {
    	User user = dao.getUserByUsername(userName);
    	return user;
    }

    
    
      
    //helper
    
    public int getAge(LocalDate userDOB) {
        if (userDOB == null) {
            return 0;
        }
        return Period.between(userDOB, LocalDate.now()).getYears();
    }
    
    private boolean isUsernameOrEmailTaken(String username, String email) {
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
    
    private boolean isEmailUnique(String email) {
    	User user = dao.getUserByEmail(email);
    	return user == null;
    }
    
    private boolean isUsernameUnique(String username) {
    	User user = dao.getUserByUsername(username);
    	return user == null;
    }
    
    //employee helper
    private boolean validatePasswordEmployee(String password) {
        return password.length() >= 6;
    }

    private boolean validateConfirmPasswordEmployee(String password, String confPass) {
        return password.equals(confPass);
    }

    private boolean validateGenderEmployee(String userGender) {
        return userGender.equals("Male") || userGender.equals("Female");
    }

    private boolean validateDOBEmployee(LocalDate dob) {
        int age = getAge(dob);
        return age >= 17;
    }

    private boolean validateRoleEmployee(String role) {
        return role.equals("Admin") || role.equals("Laundry Staff") || role.equals("Receptionist");
    }
    
    
    //customer helper
    private boolean validateUserNameCustomer(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return isUsernameUnique(username);
    }

    private boolean validateUserEmailCustomer(String email) {
        if (email == null || !email.endsWith("@email.com")) {
            return false;
        }
        return isEmailUnique(email);
    }

    private boolean validatePasswordCustomer(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean validateConfirmPasswordCustomer(String password, String confPass) {
        return password != null && password.equals(confPass);
    }

    private boolean validateGenderCustomer(String userGender) {
        return "Male".equals(userGender) || "Female".equals(userGender);
    }

    private boolean validateDOBCustomer(LocalDate dob) {
        if (dob == null) return false;
        int age = getAge(dob);
        return age >= 12;
    }

    private boolean validateRoleCustomer(String role) {
        return "Customer".equals(role);
    }
    

}
