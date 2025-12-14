package model;

import java.time.LocalDate;
import java.time.Period;

public class User {
    protected int userID;
    protected String userName;
    protected String userEmail;
    protected String userPassword;
    protected String userGender;
    protected LocalDate userDOB;
    protected String userRole;
    
    // Constructor
    public User(int userID, String userName, String userEmail, String userPassword, 
                String userGender, LocalDate userDOB, String userRole) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userDOB = userDOB;
        this.userRole = userRole;
    }

    public User() {

    }
    
    // Getters and Setters
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
    
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public String getUserGender() {
        return userGender;
    }
    
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
    
    public LocalDate getUserDOB() {
        return userDOB;
    }
    
    public void setUserDOB(LocalDate userDOB) {
        this.userDOB = userDOB;
    }
    
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    // Calculate age from DOB
    public int getAge() {
        if (userDOB == null) {
            return 0;
        }
        return Period.between(userDOB, LocalDate.now()).getYears();
    }
    
    // Email validation 
    public boolean isValidEmail() {
        if (userEmail == null || userEmail.isEmpty()) {
            return false;
        }
        // General validation - specific validation in child classes
        return userEmail.contains("@") && userEmail.contains(".");
    }
    
    // validation
    public boolean isValidUser() {
        return userName != null && !userName.isEmpty() &&
               userEmail != null && !userEmail.isEmpty() && isValidEmail() &&
               userPassword != null && userPassword.length() >= 6 &&
               userGender != null && (userGender.equals("Male") || userGender.equals("Female")) &&
               userDOB != null;
    }
    
    @Override
    public String toString() {
        return getUserName() + " (ID: " + getUserID() + ")";
    }
}