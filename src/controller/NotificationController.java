package controller;

import model.Notification;
import dao.NotificationDAO;

import java.util.List;

/**
 * Controller untuk mengelola logika terkait Notification.
 * Menghubungkan View dengan DAO Notification.
 */
public class NotificationController {

    private NotificationDAO dao = new NotificationDAO();

    /**
     * Mengirim notifikasi ke user tertentu.
     * Membuat objek Notification baru dan menambahkan ke database.
     * @param recipientID ID penerima notifikasi
     * @param message isi notifikasi
     */
    public void sendNotification(int recipientID, String message) {
        Notification n = new Notification(recipientID, message);
        dao.addNotification(n.getRecipientID(), n.getNotificationMessage());
    }

    /**
     * Mengambil semua notifikasi yang ditujukan untuk user tertentu.
     * @param recipientID ID penerima notifikasi
     * @return List notifikasi user tersebut
     */
    public List<Notification> getNotificationsByRecipientID(int recipientID) {
        return dao.getNotificationsByRecipientID(recipientID);
    }

    /**
     * Mengambil notifikasi berdasarkan ID notifikasi.
     * @param notificationID ID notifikasi
     * @return objek Notification jika ditemukan, null jika tidak
     */
    public Notification getNotificationByID(int notificationID) {
        return dao.getNotificationByID(notificationID);
    }

    /**
     * Menghapus notifikasi berdasarkan ID.
     * @param notificationID ID notifikasi yang akan dihapus
     */
    public void deleteNotification(int notificationID) {
        dao.deleteNotification(notificationID);
    }

    /**
     * Menandai notifikasi sebagai sudah dibaca.
     * @param notificationID ID notifikasi yang akan ditandai
     */
    public void markAsRead(int notificationID) {
        dao.markAsRead(notificationID);
    }

    /**
     * Mengecek apakah notifikasi sudah dibaca.
     * @param notification objek Notification
     * @return true jika notifikasi sudah dibaca, false jika belum atau objek null
     */
    public boolean validateIsRead(Notification notification) {
        return notification != null && notification.isRead();
    }
}
