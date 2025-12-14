package controller;

import model.Transaction;
import util.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionController {
    
	private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean createTransaction(Transaction transaction) {
        if (transaction == null || !transaction.isValidTransaction()) return false;
        if (!transactionDAO.isValidCustomer(transaction.getCustomerID()) || !transactionDAO.isValidService(transaction.getServiceID())) return false;
        return transactionDAO.insert(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAll();
    }

    public List<Transaction> getTransactionsByCustomer(int customerId) {
        return transactionDAO.getByCustomer(customerId);
    }

    public boolean updateTransactionStatus(int transactionId, String status) {
        return transactionDAO.updateStatus(transactionId, status);
    }

    public boolean assignTransactionToStaff(int transactionId, int staffId) {
        return transactionDAO.assignToStaff(transactionId, staffId);
    }

    public List<Transaction> getTransactionsByStatus(String status) {
        return transactionDAO.getByStatus(status);
    }
}






