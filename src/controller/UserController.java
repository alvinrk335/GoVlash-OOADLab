package controller;

import model.Employee;
import model.User;
import java.time.LocalDate;
import java.time.Period;
import dao.UserDAO;
import javafx.collections.ObservableList;

/**
 * Controller untuk mengelola logika terkait User (Customer dan Employee)
 * Menghubungkan View dengan DAO User.
 */
public class UserController {

    private UserDAO dao = new UserDAO();

    /**
     * Menambahkan customer baru ke database.
     * @param name nama customer
     * @param email email customer
     * @param password password
     * @param gender gender
     * @param dob tanggal lahir
     */
    public void addCustomer(String name, String email, String password, String gender, LocalDate dob) {
        boolean inserted = dao.insertUser(name, email, password, gender, dob, "Customer");
        System.out.println(inserted);
    }

    /**
     * Melakukan login user.
     * @param username username
     * @param password password
     * @return objek User jika login valid, null jika gagal
     */
    public User login(String username, String password) {
        User user = dao.getUserByUsername(username);
        if (user == null) return null;
        if (!user.getUserPassword().equals(password)) return null;
        return user;
    }

    /**
     * Menambahkan employee baru ke database.
     * @param name nama
     * @param email email
     * @param password password
     * @param gender gender
     * @param dob tanggal lahir
     * @param role role employee
     */
    public void addEmployee(String name, String email, String password, String gender, LocalDate dob, String role) {
        boolean inserted = dao.insertUser(name, email, password, gender, dob, role);
        System.out.println(inserted);
    }

    /**
     * Mengambil semua employee.
     * @return ObservableList<Employee>
     */
    public ObservableList<Employee> getAllEmployees() {
        return dao.getAllEmployee();
    }

    /**
     * Mengambil semua user berdasarkan role.
     * @param role role user
     * @return ObservableList<User>
     */
    public ObservableList<User> getUsersByRole(String role) {
        return dao.getUsersByRole(role);
    }

    /**
     * Validasi data input untuk menambahkan employee.
     * @return pesan validasi
     */
    public String validateAddEmployee(String name, String email, String password, String confirmPassword, String gender, LocalDate dob, String role) {
        if(name == null || name.isBlank() ||
           email == null || email.isBlank() ||
           password == null || password.isBlank() ||
           confirmPassword == null || confirmPassword.isBlank() ||
           gender == null || gender.isBlank() ||
           dob == null ||
           role == null || role.isBlank()) {
            return "all fields must be filled";
        }
        if(isUsernameOrEmailTaken(name, email)) {
            return "email or username has been taken";
        }
        if(!validatePasswordEmployee(password)) {
            return "password must be more than 6 char";
        }
        if(!validateConfirmPasswordEmployee(password, confirmPassword)) {
            return "confirm password must be the same as password";
        }
        if(!validateGenderEmployee(gender)) {
            return "gender must be either \"Male\" or \"Female\"";
        }
        if(!validateDOBEmployee(dob)) {
            return "user must be 17 years old or above";
        }
        if(!validateRoleEmployee(role)) {
            return "role must be either “Admin”, “Laundry Staff”, or “Receptionist”";
        }

        return "employee registered";
    }

    /**
     * Validasi data input untuk menambahkan customer.
     * @return pesan validasi
     */
    public String validateAddCustomer(String name, String email, String password, String confirmPassword, String gender, LocalDate dob) {
        if(name == null || name.isBlank() ||
           email == null || email.isBlank() ||
           password == null || password.isBlank() ||
           confirmPassword == null || confirmPassword.isBlank() ||
           gender == null || gender.isBlank() ||
           dob == null) {
            return "all fields must be filled";
        }
        if (isUsernameOrEmailTaken(name, email)) {
            return "email or username has been taken";
        }
        if(!validateUserEmailCustomer(email)) {
            return "email must end with @email.com";
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

    /**
     * Mengambil user berdasarkan email.
     */
    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    /**
     * Mengambil user berdasarkan username.
     */
    public User getUserByName(String userName) {
        return dao.getUserByUsername(userName);
    }

    /**
     * Menghitung umur user dari tanggal lahir.
     */
    public int getAge(LocalDate userDOB) {
        if (userDOB == null) return 0;
        return Period.between(userDOB, LocalDate.now()).getYears();
    }

    /* ===========================
       Helper & Validator Methods
       =========================== */

    private boolean isUsernameOrEmailTaken(String username, String email) {
        return dao.isUsernameOrEmailTaken(username, email);
    }

    // Employee validation
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
        return getAge(dob) >= 17;
    }
    private boolean validateRoleEmployee(String role) {
        return role.equals("Admin") || role.equals("Laundry Staff") || role.equals("Receptionist");
    }

    // Customer validation
    private boolean validateUserEmailCustomer(String email) {
        return email != null && email.endsWith("@email.com") && isEmailUnique(email);
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
        return dob != null && getAge(dob) >= 12;
    }

    private boolean isEmailUnique(String email) {
        return dao.getUserByEmail(email) == null;
    }
    private boolean isUsernameUnique(String username) {
        return dao.getUserByUsername(username) == null;
    }
}
