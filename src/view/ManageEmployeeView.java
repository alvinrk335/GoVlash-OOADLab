package view;

import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Employee;

public class ManageEmployeeView {

    private TableView<Employee> table;
    private TextField tfName, tfEmail, tfPassword, tfConfirmPassword, tfGender, tfRole;
    private DatePicker dpDOB;
    private Button btnAdd, btnUpdate, btnClear, btnRefresh, btnBack;
    private UserController controller = new UserController();
    private Runnable backAction;
    private Stage stage;
    
    public ManageEmployeeView(Stage stage) {
    	this.stage = stage;
    	init();
    	
    	
    }

    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }

    public void init() {
        stage.setTitle("Manage Employees");

        // LEFT FORM
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(20));
        formBox.setPrefWidth(300);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setStyle("-fx-background-color: #f1f1f1;");

        Label lblTitle = new Label("Employee Form");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tfName = new TextField();
        tfName.setPromptText("Name");

        tfEmail = new TextField();
        tfEmail.setPromptText("Email");

        tfPassword = new TextField();
        tfPassword.setPromptText("Password");

        tfConfirmPassword = new TextField();
        tfConfirmPassword.setPromptText("Confirm Password");

        tfGender = new TextField();
        tfGender.setPromptText("Gender (Male/Female)");

        dpDOB = new DatePicker();
        dpDOB.setPromptText("Date of Birth");

        tfRole = new TextField();
        tfRole.setPromptText("Role (Admin/Receptionist/Laundry Staff)");

        btnAdd = new Button("Add");
        btnUpdate = new Button("Update");
        btnClear = new Button("Clear");
        btnRefresh = new Button("Refresh");
        btnBack = new Button("Back");

        btnAdd.setPrefWidth(200);
        btnUpdate.setPrefWidth(200);
        btnClear.setPrefWidth(200);
        btnRefresh.setPrefWidth(200);
        btnBack.setPrefWidth(200);

        formBox.getChildren().addAll(lblTitle, tfName, tfEmail, tfPassword, tfConfirmPassword,
                tfGender, dpDOB, tfRole,
                btnAdd, btnUpdate, btnClear, btnRefresh, btnBack);

        // RIGHT TABLE
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Employee, Integer> colID = new TableColumn<>("ID");
        colID.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getUserID()).asObject());

        TableColumn<Employee, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserName()));

        TableColumn<Employee, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserEmail()));

        TableColumn<Employee, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserRole()));

        TableColumn<Employee, String> colGender = new TableColumn<>("Gender");
        colGender.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserGender()));

        TableColumn<Employee, String> colDOB = new TableColumn<>("DOB");
        colDOB.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserDOB().toString()));

        table.getColumns().addAll(colID, colName, colEmail, colRole, colGender, colDOB);
        table.setPrefWidth(600);

        table.setOnMouseClicked(e -> fillFormFromTable());

        refreshTable();

        // BUTTON ACTIONS
        btnAdd.setOnAction(e -> addEmployee());
        btnUpdate.setOnAction(e -> updateEmployee());
        btnClear.setOnAction(e -> clearForm());
        btnRefresh.setOnAction(e -> refreshTable());
        btnBack.setOnAction(e -> {
            Stage stg = (Stage) btnBack.getScene().getWindow();
            stg.close();
            if (backAction != null) backAction.run();
        });

        HBox root = new HBox(formBox, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 1000, 500);
        stage.setScene(scene);

    }
    
    public void show() {
        stage.show();
    }

    private void fillFormFromTable() {
        Employee emp = table.getSelectionModel().getSelectedItem();
        if (emp == null) return;

        tfName.setText(emp.getUserName());
        tfEmail.setText(emp.getUserEmail());
        tfGender.setText(emp.getUserGender());
        dpDOB.setValue(emp.getUserDOB());
        tfRole.setText(emp.getUserRole());
        tfPassword.clear();
        tfConfirmPassword.clear();
    }

    private void addEmployee() {
        String msg = controller.validateAddEmployee(
                tfName.getText(),
                tfEmail.getText(),
                tfPassword.getText(),
                tfConfirmPassword.getText(),
                tfGender.getText(),
                dpDOB.getValue(),
                tfRole.getText()
        );

        if (msg.equalsIgnoreCase("employee registered")) {
            controller.addEmployee(tfName.getText(), tfEmail.getText(), tfPassword.getText(),
                    tfGender.getText(), dpDOB.getValue(), tfRole.getText());
            showInfoAlert("Employee added successfully!");
            refreshTable();
            clearForm();
        } else {
            showErrorAlert(msg);
        }
    }

    private void updateEmployee() {
        Employee emp = table.getSelectionModel().getSelectedItem();
        if (emp == null) {
            showErrorAlert("Select an employee first!");
            return;
        }
        String password = tfPassword.getText().isBlank() ? emp.getUserPassword() : tfPassword.getText();
        String confirmPassword = tfConfirmPassword.getText().isBlank() ? password : tfConfirmPassword.getText();

        String msg = controller.validateAddEmployee(
                tfName.getText(),
                tfEmail.getText(),
                password,
                confirmPassword,
                tfGender.getText(),
                dpDOB.getValue(),
                tfRole.getText()
        );

        if (msg.equalsIgnoreCase("employee registered")) {
            controller.addEmployee(tfName.getText(), tfEmail.getText(), password,
                    tfGender.getText(), dpDOB.getValue(), tfRole.getText());
            showInfoAlert("Employee updated successfully!");
            refreshTable();
            clearForm();
        } else {
            showErrorAlert(msg);
        }
    }

    private void refreshTable() {
        ObservableList<Employee> list = controller.getAllEmployees();
        table.setItems(FXCollections.observableArrayList(list));
    }

    private void clearForm() {
        tfName.clear();
        tfEmail.clear();
        tfPassword.clear();
        tfConfirmPassword.clear();
        tfGender.clear();
        dpDOB.setValue(null);
        tfRole.clear();
        table.getSelectionModel().clearSelection();
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
