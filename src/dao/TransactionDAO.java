package dao;

import model.Transaction;
import util.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connect connect = Connect.getInstance();
    private final String tableName = "headertransaction"; 

    // Insert transaction
    public boolean insert(Transaction transaction) {
        if (transaction == null) return false;
        try {
            String query = "INSERT INTO " + tableName + " (serviceID, customerID, transactionDate, transactionStatus, totalWeight, transactionNotes) VALUES (" +
                    transaction.getServiceID() + ", " + transaction.getCustomerID() + ", '" +
                    transaction.getTransactionDate() + "', '" +
                    transaction.getTransactionStatus() + "', " +
                    transaction.getTotalWeight() + ", '" +
                    (transaction.getTransactionNotes() != null ? transaction.getTransactionNotes() : "") + "')";
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Select all transactions
    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + tableName + " ORDER BY transactionDate DESC";
            ResultSet rs = connect.execQuery(query);
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Select transactions by customer
    public List<Transaction> getByCustomer(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + tableName + " WHERE customerID = " + customerId + " ORDER BY transactionDate DESC";
            ResultSet rs = connect.execQuery(query);
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Select transactions by status
    public List<Transaction> getByStatus(String status) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + tableName + " WHERE transactionStatus = '" + status + "' ORDER BY transactionDate DESC";
            ResultSet rs = connect.execQuery(query);
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Update transaction status
    public boolean updateStatus(int transactionId, String newStatus) {
        try {
            String query = "UPDATE " + tableName + " SET transactionStatus = '" + newStatus + "' WHERE transactionID = " + transactionId;
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Assign transaction to staff
    public boolean assignToStaff(int transactionId, int staffId) {
        try {
            String query = "UPDATE " + tableName + " SET laundryStaffID = " + staffId + ", transactionStatus = 'Assigned' WHERE transactionID = " + transactionId + " AND transactionStatus = 'Pending'";
            connect.execUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // map rs ke Transaction
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setServiceID(rs.getInt("serviceID"));
        transaction.setCustomerID(rs.getInt("customerID"));
        
        if (rs.getObject("receptionistID") != null) transaction.setReceptionistID(rs.getInt("receptionistID"));
        if (rs.getObject("laundryStaffID") != null) transaction.setLaundryStaffID(rs.getInt("laundryStaffID"));
        if (rs.getDate("transactionDate") != null) transaction.setTransactionDate(rs.getDate("transactionDate").toLocalDate());
        
        transaction.setTransactionStatus(rs.getString("transactionStatus"));
        transaction.setTotalWeight(rs.getDouble("totalWeight"));
        transaction.setTransactionNotes(rs.getString("transactionNotes"));
        return transaction;
    }
    // Validasi customer
    public boolean isValidCustomer(int customerId) {
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

    // Validasi service
    public boolean isValidService(int serviceId) {
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
