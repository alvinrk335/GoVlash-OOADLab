package model;

import java.time.LocalDate;

public class Customer extends User {
    
    // Constructor
    public Customer(int userID, String userName, String userEmail, String userPassword, 
                   String userGender, LocalDate userDOB) {
        super(userID, userName, userEmail, userPassword, userGender, userDOB, "Customer");
    }
    
    // Default constructor
    public Customer() {
        super();
        this.userRole = "Customer";
    }
    
    // Customer-specific validation
    @Override
    public boolean isValidUser() {
        // Basic validation from parent
        if (!super.isValidUser()) {
            return false;
        }
        
        // Customer must be at least 17 years old
        if (getAge() < 17) {
            return false;
        }
        
        // Customer email must end with @gmail.com 
        if (!userEmail.endsWith("@gmail.com")) {
            return false;
        }
        
        return true;
    }
    
    public String getCustomerInfo() {
        return String.format("Customer: %s (%s) - Age: %d", userName, userEmail, getAge());
    }
}