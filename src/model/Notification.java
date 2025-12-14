package model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationID;
    private int recipientID;
    private String notificationMessage;
    private LocalDateTime createdAt;
    private boolean isRead;

    public Notification(int notificationID, int recipientID, String notificationMessage, LocalDateTime createdAt, boolean isRead) {
        this.notificationID = notificationID;
        this.recipientID = recipientID;
        this.notificationMessage = notificationMessage;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public Notification(int recipientID, String notificationMessage) {
        this.recipientID = recipientID;
        this.notificationMessage = notificationMessage;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    // getters & setters
    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }

    public int getRecipientID() { return recipientID; }
    public void setRecipientID(int recipientID) { this.recipientID = recipientID; }

    public String getNotificationMessage() { return notificationMessage; }
    public void setNotificationMessage(String notificationMessage) { this.notificationMessage = notificationMessage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
