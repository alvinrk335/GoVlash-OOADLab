package view;

import controller.NotificationController;
import controller.TransactionController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Transaction;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tampilan untuk melihat dan mengelola semua transaksi.
 * Tombol dan aksi berbeda sesuai role user:
 * - Admin: Bisa filter transaksi
 * - Receptionist: Bisa assign transaksi ke Laundry Staff
 * - Laundry Staff: Bisa menandai transaksi selesai
 * - Customer: Bisa melihat transaksi miliknya
 */
public class AllTransactionView{

    private TableView<Transaction> table;
    private Stage  stage;
    private ComboBox<String> filterCombo;
    UserController userController = new UserController();
    private Button assignButton, markFinishedButton, backButton;
    private TransactionController controller = new TransactionController();
    private NotificationController notificationController = new NotificationController();
    private Runnable backAction;
    private String role;
    private int userId;

    /**
     * Konstruktor menerima stage, role user, dan userID
     */
    public AllTransactionView(Stage stage, String role, int userId) {
        this.role = role.toLowerCase();
        this.userId = userId;
        this.stage = stage;
        init();
    }

    /**
     * Setter aksi tombol Back
     */
    public void setBackAction(Runnable action) {
        this.backAction = action;
    }

    /**
     * Inisialisasi UI
     */
    public void init() {
        stage.setTitle("Transactions Management");

        // LEFT FORM: area tombol aksi
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(20));
        formBox.setPrefWidth(300);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setStyle("-fx-background-color: #f1f1f1;");

        Label lblTitle = new Label("Transaction Actions");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Admin filter ComboBox
        if (role.equals("admin")) {
            filterCombo = new ComboBox<>(FXCollections.observableArrayList("All", "Pending", "Finished"));
            filterCombo.setValue("All");
            filterCombo.setOnAction(e -> refreshTable());
            formBox.getChildren().addAll(lblTitle, filterCombo);
        } else {
            formBox.getChildren().add(lblTitle);
        }

        // Receptionist assign button
        if (role.equals("receptionist")) {
            assignButton = new Button("Assign to Staff");
            assignButton.setPrefWidth(200);
            assignButton.setOnAction(e -> assignTransaction());
            formBox.getChildren().add(assignButton);
        }

        // Laundry Staff mark finished button
        if (role.equals("laundry staff")) {
            markFinishedButton = new Button("Mark as Finished");
            markFinishedButton.setPrefWidth(200);
            markFinishedButton.setOnAction(e -> markTransactionFinished());
            formBox.getChildren().add(markFinishedButton);
        }

        // Tombol back untuk semua role
        backButton = new Button("Back");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            if (backAction != null) backAction.run();
        });
        formBox.getChildren().add(backButton);

        // RIGHT TABLE: menampilkan daftar transaksi
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(600);

        TableColumn<Transaction, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTransactionID()).asObject());

        TableColumn<Transaction, Integer> colCust = new TableColumn<>("Customer ID");
        colCust.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCustomerID()).asObject());

        TableColumn<Transaction, Integer> colService = new TableColumn<>("Service ID");
        colService.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getServiceID()).asObject());

        TableColumn<Transaction, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTransactionStatus()));

        TableColumn<Transaction, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTransactionDate().toString()));

        TableColumn<Transaction, String> colStaff = new TableColumn<>("Staff ID");
        colStaff.setCellValueFactory(data -> {
            Integer staffId = data.getValue().getLaundryStaffID();
            return new javafx.beans.property.SimpleStringProperty(staffId == null ? "" : staffId.toString());
        });

        TableColumn<Transaction, Double> colWeight = new TableColumn<>("Weight");
        colWeight.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotalWeight()).asObject());

        table.getColumns().addAll(colID, colCust, colService, colWeight, colStatus, colDate, colStaff);

        // Enable tombol ketika row dipilih
        table.setOnMouseClicked(e -> tableRowSelected());

        // Load data
        refreshTable();

        HBox root = new HBox(formBox, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);

    }

    /**
     * Menampilkan stage
     */
    public void show() {
    	stage.show();
    }
    
    /**
     * Set aksi tombol Back dari parent view
     */
    public void setBackButtonAction(Runnable action) {
        this.backAction = action;
    }

    /**
     * Refresh data tabel sesuai role dan filter
     */
    private void refreshTable() {
        List<Transaction> transactions;
        switch (role) {
            case "admin":
                if (filterCombo != null && filterCombo.getValue().equalsIgnoreCase("Finished")) {
                    transactions = controller.getTransactionsByStatus("Finished");
                } else if (filterCombo != null && filterCombo.getValue().equalsIgnoreCase("Pending")) {
                    transactions = controller.getTransactionsByStatus("Pending");
                } else {
                    transactions = controller.getAllTransactions();
                }
                break;
            case "receptionist":
                transactions = controller.getTransactionsByStatus("Pending");
                break;
            case "laundry staff":
                transactions = controller.getAllTransactions().stream()
                        .filter(t -> t.getLaundryStaffID() != null && t.getLaundryStaffID() == userId)
                        .collect(Collectors.toList());
                break;
            case "customer":
                transactions = controller.getTransactionsByCustomer(userId);
                break;
            default:
                transactions = List.of();
        }

        transactions.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));

        ObservableList<Transaction> list = FXCollections.observableArrayList(transactions);
        table.setItems(list);
    }

    /**
     * Memberi notifikasi ke customer setelah transaksi selesai
     */
    private void finishTransaction() {
        Transaction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a transaction first!");
            return;
        }
        String message = "Your order is finished and ready for pickup. Thank you for choosing our service!";
        notificationController.sendNotification(selected.getCustomerID(), message);

        showAlert("Transaction marked as finished and customer notified!");
        refreshTable();
    }

    /**
     * Enable tombol ketika row dipilih
     */
    private void tableRowSelected() {
        Transaction t = table.getSelectionModel().getSelectedItem();
        if (t == null) return;

        if (role.equals("receptionist")) {
            assignButton.setDisable(false);
        }
        if (role.equals("laundry staff")) {
            markFinishedButton.setDisable(false);
        }
    }

    /**
     * Assign transaksi ke staff melalui ChoiceDialog
     */
    private void assignTransaction() {
        Transaction t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showAlert("Select a transaction first!");
            return;
        }

        ObservableList<User> staffList = userController.getUsersByRole("Laundry Staff");

        if (staffList.isEmpty()) {
            showAlert("No staff available to assign.");
            return;
        }

        ChoiceDialog<User> dialog = new ChoiceDialog<>(staffList.get(0), staffList);
        dialog.setTitle("Assign Transaction");
        dialog.setHeaderText("Select a staff to assign:");
        dialog.setContentText("Staff:");

        // panggil showAndWait hanya sekali
        dialog.showAndWait().ifPresent(selectedStaff -> {
            int staffId = selectedStaff.getUserID();
            if (controller.assignTransactionToStaff(t.getTransactionID(), staffId)) {
                showAlert("Transaction assigned to " + selectedStaff.getUserName());
                notificationController.sendNotification(staffId, "A new transaction has been assigned to you.");
                refreshTable();
            } else {
                showAlert("Failed to assign transaction.");
            }
        });
    }

    /**
     * Mark transaksi selesai
     */
    private void markTransactionFinished() {
        Transaction t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showAlert("Select a transaction first!");
            return;
        }

        if (controller.updateTransactionStatus(t.getTransactionID(), "Finished")) {
            showAlert("Transaction marked as finished.");
            finishTransaction();
            refreshTable();
        } else showAlert("Failed to mark finished.");
    }

    /**
     * Menampilkan Alert informasi
     */
    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
