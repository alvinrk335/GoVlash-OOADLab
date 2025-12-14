package controller;

import model.Notification;
import dao.NotificationDAO;

import java.util.List;

public class NotificationController {

    private NotificationDAO dao = new NotificationDAO();

    public void sendNotification(int recipientID, String message) {
        Notification n = new Notification(recipientID, message);
        dao.addNotification(n.getRecipientID(), n.getNotificationMessage());
    }

    public List<Notification> getNotificationsByRecipientID(int recipientID) {
        return dao.getNotificationsByRecipientID(recipientID);
    }

    public Notification getNotificationByID(int notificationID) {
        return dao.getNotificationByID(notificationID);
    }

    public void deleteNotification(int notificationID) {
        dao.deleteNotification(notificationID);
    }

    public void markAsRead(int notificationID) {
        dao.markAsRead(notificationID);
    }

    public boolean validateIsRead(Notification notification) {
        return notification != null && notification.isRead();
    }
}
