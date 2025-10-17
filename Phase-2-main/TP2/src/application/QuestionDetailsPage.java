package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Answer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QuestionDetailsPage {
    private final DatabaseHelper db;
    private final String currentUser;
    private final int questionId;

    public QuestionDetailsPage(DatabaseHelper db, String currentUser, int questionId) {
        this.db = db;
        this.currentUser = currentUser;
        this.questionId = questionId;
    }

    public void show(Stage stage) {
        Label title = new Label();
        Label meta = new Label();
        TextArea questionText = new TextArea();
        questionText.setEditable(false);
        questionText.setWrapText(true);

        ListView<Answer> answersList = new ListView<>();
        answersList.setCellFactory(v -> new ListCell<>(){
            @Override protected void updateItem(Answer a, boolean empty){
                super.updateItem(a, empty);
                if (empty || a == null) setText(null);
                else setText((a.isCorrect()?"[ACCEPTED] ":"") + a.getAuthor()+": "+a.getAnswerText());
            }
        });

        TextArea newAnswerField = new TextArea();
        newAnswerField.setPromptText("Propose an answer...");
        newAnswerField.setPrefRowCount(4);
        Button submitAnswer = new Button("Submit Answer");
        submitAnswer.setOnAction(e -> {
            String text = newAnswerField.getText();
            if (text == null || text.isBlank()) return;
            try {
                db.addAnswer(questionId, text.trim(), currentUser);
                newAnswerField.clear();
                refresh(answersList, title, questionText, meta);
            } catch (SQLException ex) {
                AlertUtils.error("Failed to add answer: "+ex.getMessage());
            }
        });

        TextField clarificationField = new TextField();
        clarificationField.setPromptText("Ask/suggest clarification about the question...");
        Button addClarification = new Button("Add Clarification");
        addClarification.setOnAction(e -> {
            String clar = clarificationField.getText();
            if (clar == null || clar.isBlank()) return;
            try {
                db.addQuestionClarification(questionId, clar.trim());
                clarificationField.clear();
                AlertUtils.info("Clarification submitted.");
            } catch (SQLException ex) {
                AlertUtils.error("Failed to add clarification: "+ex.getMessage());
            }
        });

        Button clarifyAnswer = new Button("Clarify Selected Answer");
        clarifyAnswer.setOnAction(e -> {
            Answer sel = answersList.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            TextInputDialog d = new TextInputDialog();
            d.setTitle("Clarify Answer"); d.setHeaderText(null); d.setContentText("Clarification:");
            d.showAndWait().ifPresent(text -> {
                if (text != null && !text.isBlank()) {
                    try { db.addAnswerClarification(sel.getAnswerId(), text.trim()); AlertUtils.info("Clarification submitted."); }
                    catch (SQLException ex){ AlertUtils.error("Failed: "+ex.getMessage()); }
                }
            });
        });

        Button markAccepted = new Button("Mark Selected as Accepted");
        markAccepted.setOnAction(e -> {
            Answer sel = answersList.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            try {
                // Unmark all then mark selected
                List<Answer> all = db.getAnswerVerifiedForQuestion(questionId);
                for (Answer a : all) db.updateCorrectAnswer(a.getAnswerId(), false);
                db.updateCorrectAnswer(sel.getAnswerId(), true);
                refresh(answersList, title, questionText, meta);
            } catch (SQLException ex) {
                AlertUtils.error("Failed to mark accepted: "+ex.getMessage());
            }
        });

        Button editQuestion = new Button("Edit Question");
        editQuestion.setOnAction(e -> {
            // Only author can edit
            Map<String,Object> q;
            try { q = db.getQuestionById(questionId); }
            catch (SQLException ex){ AlertUtils.error("Load failed: "+ex.getMessage()); return; }
            String author = (String) q.get("author");
            if (!currentUser.equals(author)) { AlertUtils.error("Only the author can edit the question."); return; }
            TextInputDialog t = new TextInputDialog(String.valueOf(q.get("title")));
            t.setTitle("Edit Question"); t.setHeaderText(null); t.setContentText("Title:");
            var titleRes = t.showAndWait();
            TextInputDialog body = new TextInputDialog(String.valueOf(q.get("text")));
            body.setTitle("Edit Question"); body.setHeaderText(null); body.setContentText("Text:");
            var bodyRes = body.showAndWait();
            if (titleRes.isPresent() && bodyRes.isPresent()) {
                try { db.updateQuestion(questionId, titleRes.get(), bodyRes.get()); refresh(answersList, title, questionText, meta); }
                catch (SQLException ex){ AlertUtils.error("Update failed: "+ex.getMessage()); }
            }
        });

        Button editAnswer = new Button("Edit Selected Answer");
        editAnswer.setOnAction(e -> {
            Answer sel = answersList.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            if (!currentUser.equals(sel.getAuthor())) { AlertUtils.error("You can only edit your own answers."); return; }
            TextInputDialog d = new TextInputDialog(sel.getAnswerText());
            d.setTitle("Edit Answer"); d.setHeaderText(null); d.setContentText("Answer text:");
            d.showAndWait().ifPresent(newText -> {
                try { db.updateAnswer(sel.getAnswerId(), newText, currentUser); refresh(answersList, title, questionText, meta); }
                catch (SQLException ex){ AlertUtils.error("Update failed: "+ex.getMessage()); }
            });
        });

        Button back = new Button("Back");
        back.setOnAction(e -> new QuestionListPage(db, currentUser).show(stage));

        BorderPane root = new BorderPane();
        VBox top = new VBox(4, title, meta);
        top.setPadding(new Insets(8));
        root.setTop(top);

        VBox center = new VBox(10, new Label("Question"), questionText, new Label("Answers"), answersList);
        center.setPadding(new Insets(8));
        root.setCenter(center);

        HBox answerBox = new HBox(8, newAnswerField, submitAnswer);
        HBox clarBox = new HBox(8, clarificationField, addClarification, clarifyAnswer, markAccepted);
        HBox editBox = new HBox(8, editQuestion, editAnswer, back);
        VBox bottom = new VBox(8, answerBox, clarBox, editBox);
        bottom.setPadding(new Insets(8));
        root.setBottom(bottom);

        refresh(answersList, title, questionText, meta);

        stage.setScene(new Scene(root, 900, 650));
        stage.setTitle("Question Details");
        stage.show();
    }

    private void refresh(ListView<Answer> answersList, Label title, TextArea questionText, Label meta) {
        try {
            Map<String,Object> q = db.getQuestionById(questionId);
            if (q == null || q.isEmpty()) { AlertUtils.error("Question not found."); return; }
            title.setText(String.valueOf(q.get("title")));
            questionText.setText(String.valueOf(q.get("text")));
            meta.setText("Asked by "+q.get("author"));
            List<Answer> answers = db.getAnswerVerifiedForQuestion(questionId);
            answersList.getItems().setAll(answers);
        } catch (SQLException e) {
            AlertUtils.error("Load failed: "+e.getMessage());
        }
    }
}
