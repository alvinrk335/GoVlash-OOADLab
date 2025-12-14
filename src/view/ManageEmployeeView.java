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
import model.User;

/**
 * Tampilan untuk manajemen employee (Admin only).
 * Menyediakan form untuk menambah employee baru dan tabel untuk menampilkan employee yang ada.
 */
public class ManageEmployeeView {

    private TableView<Employee> table;
    private TextField tfName, tfEmail, tfPassword, tfConfirmPassword, tfGender, tfRole;
    private DatePicker dpDOB;
    private Button btnAdd, btnRefresh, btnBack;
    private UserController controller = new UserController();
    private Runnable backAction;
    private Stage stage;
    private User currentUser;

    /**
     * Konstruktor.
     * @param stage Stage utama aplikasi
     * @param currentUser User saat ini (digunakan untuk menentukan apakah admin)
     */
    public ManageEmployeeView(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        init();
    }

    /**
     * Menetapkan aksi tombol back
     * @param backAction Runnable yang dijalankan saat back ditekan
     */
    public void setBackAction(Runnable backAction) {
        this.backAction = backAction;
    }

    /**
     * Inisialisasi komponen tampilan
     */
    public void init() {
        stage.setTitle("Manage Employees");

        boolean isAdmin = currentUser.getUserRole().equalsIgnoreCase("admin");

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

        btnBack = new Button("Back");
        btnBack.setPrefWidth(200);
        btnBack.setOnAction(e -> {
            if (backAction != null) backAction.run();
        });

        formBox.getChildren().add(lblTitle);

        // Jika admin, tampilkan form tambah employee
        if (isAdmin) {
            btnAdd = new Button("Add");
            btnAdd.setPrefWidth(200);
            formBox.getChildren().addAll(tfName, tfEmail, tfPassword, tfConfirmPassword,
                    tfGender, dpDOB, tfRole, btnAdd);
            btnAdd.setOnAction(e -> addEmployee());
        }

        btnRefresh = new Button("Refresh");
        btnRefresh.setPrefWidth(200);
        btnRefresh.setOnAction(e -> refreshTable());
        formBox.getChildren().addAll(btnRefresh, btnBack);

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

        refreshTable();

        HBox root = new HBox(formBox, table);
        HBox.setHgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 1000, 500);
        stage.setScene(scene);
    }

    /**
     * Menampilkan view
     */
    public void show() {
        stage.show();
    }

    /**
     * Menambahkan employee baru melalui form
     */
    private void addEmployee() {
        String msg = controller.validateAddEmployee(
                tfName.getText(), tfEmail.getText(),
                tfPassword.getText(), tfConfirmPassword.getText(),
                tfGender.getText(), dpDOB.getValue(), tfRole.getText());

        if (msg.equalsIgnoreCase("employee registered")) {
            controller.addEmployee(tfName.getText(), tfEmail.getText(), tfPassword.getText(),
                    tfGender.getText(), dpDOB.getValue(), tfRole.getText());
            showInfoAlert("Employee added successfully!");
            refreshTable();
            clearForm();
        } else showErrorAlert(msg);
    }

    /**
     * Menyegarkan tabel employee
     */
    private void refreshTable() {
        ObservableList<Employee> list = controller.getAllEmployees();
        table.setItems(FXCollections.observableArrayList(list));
    }

    /**
     * Membersihkan form input
     */
    private void clearForm() {
        tfName.clear();
        tfEmail.clear();
        tfPassword.clear();
        tfConfirmPassword.clear();
        tfGender.clear();
        dpDOB.setValue(null);
        tfRole.clear();
    }

    /**
     * Menampilkan alert informasi
     * @param msg Pesan informasi
     */
    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Menampilkan alert error
     * @param msg Pesan error
     */
    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
