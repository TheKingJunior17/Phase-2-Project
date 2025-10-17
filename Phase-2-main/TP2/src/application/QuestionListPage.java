package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestionListPage {
    private final DatabaseHelper db;
    private final String currentUser;
    private List<Map<String, Object>> allQuestions = new ArrayList<>();

    public QuestionListPage(DatabaseHelper db, String currentUser) {
        this.db = db;
        this.currentUser = currentUser;
    }

    public void show(Stage stage) {
        TextField search = new TextField();
        search.setPromptText("Search questions...");

        ListView<Map<String, Object>> list = new ListView<>();
        list.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else {
                    String title = String.valueOf(item.get("title"));
                    String author = String.valueOf(item.get("author"));
                    setText(title + "  —  by " + author);
                }
            }
        });

        Button view = new Button("View");
        view.setOnAction(e -> {
            Map<String, Object> sel = list.getSelectionModel().getSelectedItem();
            if (sel != null) {
                int qid = (int) sel.get("question_id");
                new QuestionDetailsPage(db, currentUser, qid).show(stage);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> new StudentHomePage(db, currentUser).show(stage));

        Button ask = new Button("Ask Question");
        ask.setOnAction(e -> new AskQuestionPage(db, currentUser).show(stage));

        search.textProperty().addListener((obs, o, n) -> filter(list, n));

        BorderPane root = new BorderPane();
        HBox top = new HBox(8, new Label("Questions"), search, ask);
        top.setPadding(new Insets(10));
        HBox.setHgrow(search, Priority.ALWAYS);
        root.setTop(top);
        root.setCenter(list);
        HBox bottom = new HBox(8, view, back);
        bottom.setPadding(new Insets(10));
        root.setBottom(bottom);

        refresh(list);

        stage.setScene(new Scene(root, 800, 500));
        stage.setTitle("Browse Questions");
        stage.show();
    }

    private void refresh(ListView<Map<String, Object>> list) {
        try {
            allQuestions = db.getAllQuestions();
            list.getItems().setAll(allQuestions);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load questions: " + e.getMessage()).showAndWait();
        }
    }

    private void filter(ListView<Map<String, Object>> list, String term) {
        if (term == null || term.isBlank()) {
            list.getItems().setAll(allQuestions);
        } else {
            String t = term.toLowerCase();
            list.getItems().setAll(
                allQuestions.stream()
                    .filter(q -> String.valueOf(q.get("title")).toLowerCase().contains(t)
                               || String.valueOf(q.get("text")).toLowerCase().contains(t)
                               || String.valueOf(q.get("author")).toLowerCase().contains(t))
                    .collect(Collectors.toList())
            );
        }
    }
}
