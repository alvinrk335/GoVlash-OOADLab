package view;

import controller.NotificationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Notification;
import model.User;

public class NotificationView {

    private TableView<Notification> table;
    private Button btnViewDetail, btnDelete, btnRefresh, btnBack;
    private NotificationController controller = new NotificationController();
    private Runnable backAction; 
    private Stage stage;
    private User user;

    public NotificationView(Stage stage, User user) {
        this.stage = stage;
        this.user  = user;
        init();
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }

    public void init() {
        stage.setTitle("Notifications");

        boolean isAdmin = user.getUserRole().equalsIgnoreCase("admin");

        // LEFT ACTIONS
        VBox actionBox = new VBox(10);
        actionBox.setPadding(new Insets(20));
        actionBox.setPrefWidth(250);
        actionBox.setAlignment(Pos.TOP_CENTER);
        actionBox.setStyle("-fx-background-color: #f1f1f1;");

        Label lblTitle = new Label("Actions");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        actionBox.getChildren().add(lblTitle);

        // tombol hanya muncul kalau admin
        if (isAdmin) {
            btnDelete = new Button("Delete");
            btnRefresh = new Button("Refresh");


            btnDelete.setPrefWidth(200);
            btnRefresh.setPrefWidth(200);

            actionBox.getChildren().addAll(btnDelete, btnRefresh);

            // BUTTON ACTIONS

            btnDelete.setOnAction(e -> deleteNotification());
            btnRefresh.setOnAction(e -> refreshTable());
        }
        
        btnViewDetail = new Button("View Detail");
        btnViewDetail.setPrefWidth(200);
        btnViewDetail.setOnAction(e -> viewDetail());

        // tombol back selalu ada
        btnBack = new Button("Back");
        btnBack.setPrefWidth(200);
        btnBack.setOnAction(e -> {
            if (backAction != null) backAction.run();
        });
        actionBox.getChildren().addAll(btnViewDetail, btnBack);

        // RIGHT TABLE
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Notification, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNotificationID()).asObject());

        TableColumn<Notification, String> colMessage = new TableColumn<>("Message");
        colMessage.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNotificationMessage()));

        TableColumn<Notification, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().isRead() ? "Read" : "Unread"
        ));

        table.getColumns().addAll(colID, colMessage, colStatus);
        table.setPrefWidth(700);

        refreshTable();

        HBox root = new HBox(actionBox, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 1000, 500);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    private void refreshTable() {
        ObservableList<Notification> list = FXCollections.observableArrayList(controller.getNotificationsByRecipientID(user.getUserID()));
        table.setItems(list);
    }

    private void viewDetail() {
        Notification notif = table.getSelectionModel().getSelectedItem();
        if (notif == null) {
            showErrorAlert("Select a notification first!");
            return;
        }

        if (!notif.isRead()) {
            controller.markAsRead(notif.getNotificationID());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification Detail");
        alert.setHeaderText(null);
        alert.setContentText(notif.getNotificationMessage());
        alert.showAndWait();

        refreshTable();
    }

    private void deleteNotification() {
        Notification notif = table.getSelectionModel().getSelectedItem();
        if (notif == null) {
            showErrorAlert("Select a notification first!");
            return;
        }

        controller.deleteNotification(notif.getNotificationID());
        showInfoAlert("Notification deleted successfully!");
        refreshTable();
    }

    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
