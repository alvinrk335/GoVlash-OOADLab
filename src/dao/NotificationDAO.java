package dao;

import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Notification;
import util.Connect;

/**
 * DAO untuk operasi CRUD Notification di database
 */
public class NotificationDAO {

    private final String table_name = "notification";
    private final Connect db = Connect.getInstance();

    /**
     * Menambahkan notifikasi baru ke database
     * @param recipientID id user penerima
     * @param message pesan notifikasi
     */
    public void addNotification(int recipientID, String message) {
        String query = "INSERT INTO " + table_name + " (recipientID, notificationMessage, createdAt, isRead) "
                + "VALUES (" + recipientID + ", '" + message + "', NOW(), false)";
        db.execUpdate(query);
    }

    /**
     * Menandai notifikasi sebagai sudah dibaca
     * @param notificationID id notifikasi
     */
    public void markAsRead(int notificationID) {
        String query = "UPDATE " + table_name + " SET isRead = true WHERE notificationID = " + notificationID;
        db.execUpdate(query);
    }

    /**
     * Menghapus notifikasi dari database
     * @param notificationID id notifikasi
     */
    public void deleteNotification(int notificationID) {
        String query = "DELETE FROM " + table_name + " WHERE notificationID = " + notificationID;
        db.execUpdate(query);
    }

    /**
     * Mengambil semua notifikasi
     * @return ObservableList<Notification>
     */
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

    /**
     * Mengambil notifikasi berdasarkan penerima
     * @param recipientID id user penerima
     * @return ObservableList<Notification>
     */
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

    /**
     * Mengambil notifikasi berdasarkan ID
     * @param id id notifikasi
     * @return Notification atau null jika tidak ditemukan
     */
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

    /**
     * Mengecek apakah notifikasi ada di database
     * @param id id notifikasi
     * @return true jika ada, false jika tidak
     */
    public boolean exists(int id) {
        String query = "SELECT * FROM " + table_name + " WHERE notificationID=" + id;
        ResultSet rs = db.execQuery(query);
        try {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mapping ResultSet ke object Notification
     */
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
