package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Simplified and cleaned AdminHomePage.
 * Provides user listing, role management, and simple admin actions.
 */
public class AdminHomePage {

    private final DatabaseHelper databaseHelper;
    private final String currentUserName;

    public AdminHomePage(DatabaseHelper databaseHelper, String currentUserName) {
        this.databaseHelper = databaseHelper;
        this.currentUserName = currentUserName;
    }

    public void show(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));

        Label title = new Label("Admin Console");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
        root.setTop(title);

        VBox center = new VBox(10);
        center.setPadding(new Insets(10));

        Button btnListUsers = new Button("List Users");
        btnListUsers.setOnAction(e -> openUserListWindow());

        Button btnAddRole = new Button("Add Role");
        btnAddRole.setOnAction(e -> handleAddRole());

        Button btnRemoveRole = new Button("Remove Role");
        btnRemoveRole.setOnAction(e -> handleRemoveRole());

        Button btnRemoveUser = new Button("Remove User");
        btnRemoveUser.setOnAction(e -> handleRemoveUser());

        Button btnTempPass = new Button("Set Temp Password");
        btnTempPass.setOnAction(e -> handleSetTemporaryPassword());

        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new UserLoginPage(databaseHelper).show(new Stage());
        });

        HBox actions = new HBox(8, btnListUsers, btnAddRole, btnRemoveRole, btnRemoveUser, btnTempPass, btnLogout);
        center.getChildren().addAll(actions);

        root.setCenter(center);

        Scene scene = new Scene(root, 900, 140);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Home - " + currentUserName);
        primaryStage.show();
    }

    private void openUserListWindow() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("All Users");

        TableView<User> table = new TableView<>();
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cd -> cd.getValue().nameProperty());

        TableColumn<User, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(cd -> cd.getValue().userNameProperty());

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cd -> cd.getValue().emailProperty());

        TableColumn<User, String> roleCol = new TableColumn<>("Roles");
        roleCol.setCellValueFactory(cd -> cd.getValue().roleProperty());

        table.getColumns().addAll(nameCol, userCol, emailCol, roleCol);

        try {
            List<User> users = databaseHelper.getAllUsers();
            table.getItems().addAll(users);
        } catch (SQLException e) {
            showError("Could not load users: " + e.getMessage());
        }

        VBox box = new VBox(8, table);
        box.setPadding(new Insets(10));
        Scene s = new Scene(box, 800, 400);
        dialog.setScene(s);
        dialog.show();
    }

    private void handleAddRole() {
        String user = prompt("Add Role", "Username:");
        if (isEmpty(user)) return;
        String role = prompt("Add Role", "Role to add:");
        if (isEmpty(role)) return;
        try {
            databaseHelper.addRoleToUser(user, role);
            showInfo("Role added.");
        } catch (SQLException e) {
            showError("Failed to add role: " + e.getMessage());
        }
    }

    private void handleRemoveRole() {
        String user = prompt("Remove Role", "Username:");
        if (isEmpty(user)) return;
        String role = prompt("Remove Role", "Role to remove:");
        if (isEmpty(role)) return;
        if (user.equals(currentUserName) && role.equalsIgnoreCase("admin")) {
            showError("You cannot remove admin role from your own account.");
            return;
        }
        try {
            if (role.equalsIgnoreCase("admin")) {
                int admins = databaseHelper.countAdminUsers();
                if (admins <= 1) { showError("At least one admin must remain."); return; }
            }
            boolean ok = databaseHelper.removeRoleFromUser(user, role);
            showInfo(ok ? "Role removed." : "Role not removed (maybe not present).");
        } catch (SQLException e) {
            showError("Failed to remove role: " + e.getMessage());
        }
    }

    private void handleRemoveUser() {
        String user = prompt("Remove User", "Username:");
        if (isEmpty(user)) return;
        String role = databaseHelper.getUserRole(user);
        if (role != null && role.equalsIgnoreCase("admin")) { showError("Cannot remove admin account."); return; }
        if (confirm("Delete user?", "Really delete user '" + user + "'")) {
            boolean deleted = databaseHelper.deleteUser(user);
            showInfo(deleted ? "User deleted." : "User not deleted.");
        }
    }

    private void handleSetTemporaryPassword() {
        String user = prompt("Temporary Password", "Username:");
        if (isEmpty(user)) return;
        User u = databaseHelper.getUser(user);
        if (u == null) { showError("User not found."); return; }
        String tmp = generateTemporaryPassword();
        u.setPassword(tmp);
        u.setTemporaryPassword(true);
        try {
            boolean ok = databaseHelper.updateUser(u);
            showInfo(ok ? "Temp password set: " + tmp : "Failed to set password.");
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private String prompt(String title, String prompt) {
        TextInputDialog d = new TextInputDialog();
        d.setTitle(title); d.setHeaderText(null); d.setContentText(prompt);
        return d.showAndWait().orElse(null);
    }

    private boolean confirm(String title, String message) {
        return AlertUtils.confirm(title, message);
    }

    private void showError(String msg) { AlertUtils.error(msg); }
    private void showInfo(String msg) { AlertUtils.info(msg); }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) sb.append(chars.charAt((int)(Math.random()*chars.length())));
        return sb.toString();
    }
}

