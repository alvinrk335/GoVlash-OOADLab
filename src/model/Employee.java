package model;

import java.time.LocalDate;

public class Employee extends User {
    
    // Constructor
    public Employee(int userID, String userName, String userEmail, String userPassword, 
                   String userGender, LocalDate userDOB, String userRole) {
        super(userID, userName, userEmail, userPassword, userGender, userDOB, userRole);
    }
    
    // Default constructor
    public Employee() {
        super();
    }
    
    // Employee-specific validation
    @Override
    public boolean isValidUser() {
        // Basic validation from parent
        if (!super.isValidUser()) {
            return false;
        }
        
        // Employee must be at least 17 years old
        if (getAge() < 17) {
            return false;
        }
        
        // Employee email must end with @govlash.com
        if (!userEmail.endsWith("@govlash.com")) {
            return false;
        }
        
        // Employee role must be Admin, Laundry Staff, or Receptionist
        if (!userRole.equals("Admin") && !userRole.equals("Laundry Staff") && !userRole.equals("Receptionist")) {
            return false;
        }
        
        return true;
    }
    
    // Employee-specific methods
    public String getEmployeeInfo() {
        return String.format("Employee: %s (%s) - Role: %s - Age: %d", 
                           userName, userEmail, userRole, getAge());
    }
    
    public boolean isAdmin() {
        return "Admin".equals(userRole);
    }
    
    public boolean isLaundryStaff() {
        return "Laundry Staff".equals(userRole);
    }
    
    public boolean isReceptionist() {
        return "Receptionist".equals(userRole);
    }
}