package controller;

import model.Transaction;
import util.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    
    private Connect connect = Connect.getInstance();
    
    // Create new transaction (Customer)
    public boolean createTransaction(Transaction transaction) {
        if (transaction == null || !transaction.isValidTransaction()) {
            return false;
        }
        
        // Validate customer exists
        if (!isValidCustomer(transaction.getCustomerID())) {
            return false;
        }
        
        // Validate service exists
        if (!isValidService(transaction.getServiceID())) {
            return false;
        }
        
        try {
            String query = "INSERT INTO headertransaction (serviceID, customerID, transactionDate, " +
                          "transactionStatus, totalWeight, transactionNotes) VALUES (" +
                          transaction.getServiceID() + ", " + transaction.getCustomerID() + ", '" +
                          transaction.getTransactionDate() + "', '" + transaction.getTransactionStatus() + "', " +
                          transaction.getTotalWeight() + ", '" + 
                          (transaction.getTransactionNotes() != null ? transaction.getTransactionNotes() : "") + "')";
            
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get all transactions
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM headertransaction ORDER BY transactionDate DESC";
            ResultSet rs = connect.execQuery(query);
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    // Get transactions by customer
    public List<Transaction> getTransactionsByCustomer(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM headertransaction WHERE customerID = " + customerId + 
                          " ORDER BY transactionDate DESC";
            ResultSet rs = connect.execQuery(query);
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    // Get pending transactions (for receptionist)
    public List<Transaction> getPendingTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM headertransaction WHERE transactionStatus = 'Pending' " +
                          "ORDER BY transactionDate ASC";
            ResultSet rs = connect.execQuery(query);
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    // Get transactions assigned to specific staff
    public List<Transaction> getTransactionsByStaff(int staffId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM headertransaction WHERE laundryStaffID = " + staffId + 
                          " AND transactionStatus IN ('Assigned', 'In Process') ORDER BY transactionDate ASC";
            ResultSet rs = connect.execQuery(query);
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    // Assign transaction to staff (Receptionist function)
    public boolean assignTransactionToStaff(int transactionId, int staffId) {
        try {
            String query = "UPDATE headertransaction SET laundryStaffID = " + staffId + 
                          ", transactionStatus = 'Assigned' WHERE transactionID = " + transactionId + 
                          " AND transactionStatus = 'Pending'";
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Update transaction status
    public boolean updateTransactionStatus(int transactionId, String newStatus) {
        try {
            String query = "UPDATE headertransaction SET transactionStatus = '" + newStatus + 
                          "' WHERE transactionID = " + transactionId;
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Helper method to create Transaction object from ResultSet
    private Transaction createTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setServiceID(rs.getInt("serviceID"));
        transaction.setCustomerID(rs.getInt("customerID"));
        
        if (rs.getObject("receptionistID") != null) {
            transaction.setReceptionistID(rs.getInt("receptionistID"));
        }
        if (rs.getObject("laundryStaffID") != null) {
            transaction.setLaundryStaffID(rs.getInt("laundryStaffID"));
        }
        
        if (rs.getDate("transactionDate") != null) {
            transaction.setTransactionDate(rs.getDate("transactionDate").toLocalDate());
        }
        
        transaction.setTransactionStatus(rs.getString("transactionStatus"));
        transaction.setTotalWeight(rs.getDouble("totalWeight"));
        transaction.setTransactionNotes(rs.getString("transactionNotes"));
        
        return transaction;
    }
    
    // Validation methods
    private boolean isValidCustomer(int customerId) {
        try {
            String query = "SELECT COUNT(*) as cnt FROM msuser WHERE userID = " + customerId + 
                          " AND userRole = 'Customer'";
            ResultSet rs = connect.execQuery(query);
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean isValidService(int serviceId) {
        try {
            String query = "SELECT COUNT(*) as cnt FROM msservice WHERE serviceID = " + serviceId;
            ResultSet rs = connect.execQuery(query);
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
