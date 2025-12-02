package model;

import java.time.LocalDate;

public class Transaction {
    private int transactionID;
    private int serviceID;
    private int customerID;
    private Integer receptionistID;
    private Integer laundryStaffID;
    private LocalDate transactionDate;
    private String transactionStatus;
    private double totalWeight;
    private String transactionNotes;
    
    public Transaction() {
        this.transactionStatus = "Pending";
        this.transactionDate = LocalDate.now();
    }
    
    public Transaction(int serviceID, int customerID, double totalWeight, String notes) {
        this();
        this.serviceID = serviceID;
        this.customerID = customerID;
        this.totalWeight = totalWeight;
        this.transactionNotes = notes;
    }
    
    // Getters and setters
    public int getTransactionID() {
        return transactionID;
    }
    
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }
    
    public int getServiceID() {
        return serviceID;
    }
    
    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
    
    public int getCustomerID() {
        return customerID;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    public Integer getReceptionistID() {
        return receptionistID;
    }
    
    public void setReceptionistID(Integer receptionistID) {
        this.receptionistID = receptionistID;
    }
    
    public Integer getLaundryStaffID() {
        return laundryStaffID;
    }
    
    public void setLaundryStaffID(Integer laundryStaffID) {
        this.laundryStaffID = laundryStaffID;
    }
    
    public LocalDate getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getTransactionStatus() {
        return transactionStatus;
    }
    
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
    
    public double getTotalWeight() {
        return totalWeight;
    }
    
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    
    public String getTransactionNotes() {
        return transactionNotes;
    }
    
    public void setTransactionNotes(String transactionNotes) {
        this.transactionNotes = transactionNotes;
    }
    
    // Validation methods
    public boolean isValidTransaction() {
        return serviceID > 0 &&
               customerID > 0 &&
               totalWeight >= 2 && totalWeight <= 50 && // weight validation 2-50kg
               transactionDate != null &&
               transactionStatus != null;
    }
    
    @Override
    public String toString() {
        return "Transaction #" + transactionID + " - Customer: " + customerID + 
               " - Weight: " + totalWeight + "kg - Status: " + transactionStatus;
    }
}