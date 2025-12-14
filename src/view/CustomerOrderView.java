package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import model.Service;
import model.Transaction;

public class CustomerOrderView {
    private Stage primaryStage;

    private TableView<Transaction> ordersTable;
    private Label noOrdersLabel;
    private Button backButton;
    private ObservableList<Transaction> transactionList;
    private List<Service> serviceList;

    public CustomerOrderView(Stage stage, List<Transaction> transactions, List<Service> serviceList) {
        this.primaryStage = stage;
        this.transactionList = FXCollections.observableArrayList(transactions);
        this.serviceList = serviceList;
        initializeComponents();
    }

    private void initializeComponents() {
        primaryStage.setTitle("GoVlash Laundry - My Orders");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("My Laundry Orders");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        ordersTable = new TableView<>();
        ordersTable.setPrefHeight(400);

        TableColumn<Transaction, Integer> idColumn = new TableColumn<>("Order ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        idColumn.setPrefWidth(80);

        TableColumn<Transaction, Double> weightColumn = new TableColumn<>("Weight (kg)");
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        weightColumn.setPrefWidth(100);

        TableColumn<Transaction, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        statusColumn.setPrefWidth(100);

        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Order Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateColumn.setPrefWidth(120);

        TableColumn<Transaction, String> serviceColumn = new TableColumn<>("Service");
        serviceColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            Service txService = serviceList.stream()
                    .filter(s -> s.getServiceID() == transaction.getServiceID())
                    .findFirst()
                    .orElse(null);
            return new SimpleStringProperty(txService != null ? txService.getServiceName() : "Unknown");
        });
        serviceColumn.setPrefWidth(120);

        TableColumn<Transaction, String> priceColumn = new TableColumn<>("Estimated Price");
        priceColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            Service txService = serviceList.stream()
                    .filter(s -> s.getServiceID() == transaction.getServiceID())
                    .findFirst()
                    .orElse(null);
            if (txService != null) {
                double estimatedPrice = transaction.getTotalWeight() * txService.getServicePrice();
                return new SimpleStringProperty("Rp " + String.format("%.0f", estimatedPrice));
            }
            return new SimpleStringProperty("N/A");
        });
        priceColumn.setPrefWidth(120);

        TableColumn<Transaction, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("transactionNotes"));
        notesColumn.setPrefWidth(150);

        ordersTable.getColumns().addAll(idColumn, weightColumn, statusColumn, dateColumn, serviceColumn, priceColumn, notesColumn);

        noOrdersLabel = new Label("You haven't placed any orders yet.\nClick 'View Services & Place Order' to get started!");
        noOrdersLabel.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");

        backButton = new Button("Back to Dashboard");

        if (transactionList != null && !transactionList.isEmpty()) {
            ordersTable.setItems(transactionList);
            root.getChildren().addAll(titleLabel, ordersTable, backButton);
        } else {
            root.getChildren().addAll(titleLabel, noOrdersLabel, backButton);
        }

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    public void setBackAction(Runnable action) {
        backButton.setOnAction(e -> action.run());
    }

    public void show() {
        primaryStage.show();
    }
}
