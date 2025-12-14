package dao;

import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Notification;
import util.Connect;

public class NotificationDAO {
    private final String table_name = "notification";
    private final Connect db = Connect.getInstance();

    // Insert Notification
    public void addNotification(int recipientID, String message) {
        String query = "INSERT INTO " + table_name + " (recipientID, notificationMessage, createdAt, isRead) "
                + "VALUES (" + recipientID + ", '" + message + "', NOW(), false)";
        db.execUpdate(query);
    }

    // Update as read
    public void markAsRead(int notificationID) {
        String query = "UPDATE " + table_name + " SET isRead = true WHERE notificationID = " + notificationID;
        db.execUpdate(query);
    }

    // Delete notification
    public void deleteNotification(int notificationID) {
        String query = "DELETE FROM " + table_name + " WHERE notificationID = " + notificationID;
        db.execUpdate(query);
    }

    // Get all notifications
    public ObservableList<Notification> getAllNotifications() {
        ObservableList<Notification> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + table_name + " ORDER BY createdAt DESC";

        try {
            ResultSet rs = db.execQuery(query);
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get notifications by recipientID
    public ObservableList<Notification> getNotificationsByRecipientID(int recipientID) {
        ObservableList<Notification> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + table_name + " WHERE recipientID=" + recipientID + " ORDER BY createdAt DESC";

        try {
            ResultSet rs = db.execQuery(query);
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get by ID
    public Notification getNotificationByID(int id) {
        String query = "SELECT * FROM " + table_name + " WHERE notificationID=" + id;
        ResultSet rs = db.execQuery(query);
        try {
            if (rs.next()) return mapResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check existence
    public boolean exists(int id) {
        String query = "SELECT * FROM " + table_name + " WHERE notificationID=" + id;
        ResultSet rs = db.execQuery(query);
        try {
            if (rs.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Map ResultSet to Notification
    private Notification mapResultSet(ResultSet rs) throws Exception {
        return new Notification(
                rs.getInt("notificationID"),
                rs.getInt("recipientID"),
                rs.getString("notificationMessage"),
                rs.getTimestamp("createdAt").toLocalDateTime(),
                rs.getBoolean("isRead")
        );
    }
}
