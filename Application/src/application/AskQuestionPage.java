package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AskQuestionPage {
    private final DatabaseHelper db;
    private final String currentUser;

    public AskQuestionPage(DatabaseHelper db, String currentUser) {
        this.db = db;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        TextField titleField = new TextField();
        titleField.setPromptText("Question title");
        TextArea textField = new TextArea();
        textField.setPromptText("Describe your question...");
        textField.setPrefRowCount(8);

        Label status = new Label();
        status.setStyle("-fx-text-fill: #d33;");

        Button submit = new Button("Post Question");
        submit.setOnAction(e -> {
            String title = titleField.getText();
            String text = textField.getText();
            if (title == null || title.isBlank() || text == null || text.isBlank()) {
                status.setText("Title and text are required.");
                return;
            }
            try {
                db.addQuestion(title.trim(), text.trim(), currentUser);
                new StudentHomePage(db, currentUser).show(primaryStage);
            } catch (SQLException ex) {
                status.setText("Failed to post: " + ex.getMessage());
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> new StudentHomePage(db, currentUser).show(primaryStage));

        VBox root = new VBox(10, new Label("Ask a Question"), titleField, textField, submit, cancel, status);
        root.setPadding(new Insets(16));

        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.setTitle("Ask Question");
        primaryStage.show();
    }
}
