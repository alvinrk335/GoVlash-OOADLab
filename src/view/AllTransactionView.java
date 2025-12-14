package view;

import controller.NotificationController;
import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class AllTransactionView{

    private TableView<Transaction> table;
    private Stage  stage;
    private ComboBox<String> filterCombo; // admin filter
    private Button assignButton, markFinishedButton, backButton;
    private TransactionController controller = new TransactionController();
    private NotificationController notificationController = new NotificationController();
    private Runnable backAction;
    private String role;
    private int userId;

    public AllTransactionView(Stage stage, String role, int userId) {
        this.role = role.toLowerCase();
        this.userId = userId;
        this.stage = stage;
        init();
    }

    public void setBackAction(Runnable action) {
        this.backAction = action;
    }

    public void init() {
        stage.setTitle("Transactions Management");

        // LEFT FORM
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(20));
        formBox.setPrefWidth(300);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setStyle("-fx-background-color: #f1f1f1;");

        Label lblTitle = new Label("Transaction Actions");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // admin filter
        if (role.equals("admin")) {
            filterCombo = new ComboBox<>(FXCollections.observableArrayList("All", "Pending", "Finished"));
            filterCombo.setValue("All");
            filterCombo.setOnAction(e -> refreshTable());
            formBox.getChildren().addAll(lblTitle, filterCombo);
        } else {
            formBox.getChildren().add(lblTitle);
        }

        // receptionist assign
        if (role.equals("receptionist")) {
            assignButton = new Button("Assign to Staff");
            assignButton.setPrefWidth(200);
            assignButton.setOnAction(e -> assignTransaction());
            formBox.getChildren().add(assignButton);
        }

        // laundry staff mark finished
        if (role.equals("laundry staff")) {
            markFinishedButton = new Button("Mark as Finished");
            markFinishedButton.setPrefWidth(200);
            markFinishedButton.setOnAction(e -> markTransactionFinished());
            formBox.getChildren().add(markFinishedButton);
        }

        backButton = new Button("Back");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            Stage stg = (Stage) backButton.getScene().getWindow();
            stg.close();
            if (backAction != null) backAction.run();
        });
        formBox.getChildren().add(backButton);

        // RIGHT TABLE
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

        table.setOnMouseClicked(e -> tableRowSelected());

        refreshTable();

        HBox root = new HBox(formBox, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);

    }
    
    public void show() {
    	stage.show();
    }
    

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

    private void assignTransaction() {
        Transaction t = table.getSelectionModel().getSelectedItem();
        if (t == null) {
            showAlert("Select a transaction first!");
            return;
        }

        TextInputDialog staffDialog = new TextInputDialog();
        staffDialog.setHeaderText("Enter Staff ID to assign:");
        staffDialog.showAndWait().ifPresent(input -> {
            try {
                int staffId = Integer.parseInt(input);
                if (controller.assignTransactionToStaff(t.getTransactionID(), staffId)) {
                    showAlert("Transaction assigned.");
                    refreshTable();
                } else showAlert("Failed to assign.");
            } catch (NumberFormatException e) {
                showAlert("Invalid Staff ID.");
            }
        });
    }

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

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
