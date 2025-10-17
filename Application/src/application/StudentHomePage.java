package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Minimal Student home page used by WelcomeLoginPage.
 */
public class StudentHomePage {

    private final DatabaseHelper databaseHelper;
    private final String userName;

    public StudentHomePage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
    VBox layout = new VBox(20);
    layout.setStyle("-fx-alignment: center; -fx-padding: 40;");

    Label userLabel = new Label("Welcome, " + userName + " (Student)");
    userLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Button askQuestionButton = new Button("Ask a Question");
    askQuestionButton.setOnAction(e -> new AskQuestionPage(databaseHelper, userName).show(primaryStage));

    Button viewQuestionsButton = new Button("View/Search Questions");
    viewQuestionsButton.setOnAction(e -> new QuestionListPage(databaseHelper, userName).show(primaryStage));

    Button proposeAnswerButton = new Button("Propose an Answer");
    proposeAnswerButton.setOnAction(e -> new QuestionListPage(databaseHelper, userName).show(primaryStage));

    Button clarifyButton = new Button("Ask/Suggest Clarification");
    clarifyButton.setOnAction(e -> new QuestionListPage(databaseHelper, userName).show(primaryStage));

    Button updateButton = new Button("Update Question/Answer");
    updateButton.setOnAction(e -> new QuestionListPage(databaseHelper, userName).show(primaryStage));

    Button markAcceptedButton = new Button("Mark Answer as Accepted");
    markAcceptedButton.setOnAction(e -> new QuestionListPage(databaseHelper, userName).show(primaryStage));

    Button logoutButton = new Button("Log Out");
    logoutButton.setOnAction(a -> new UserLoginPage(databaseHelper).show(primaryStage));

    layout.getChildren().addAll(userLabel, askQuestionButton, viewQuestionsButton, proposeAnswerButton, clarifyButton, updateButton, markAcceptedButton, logoutButton);

    Scene scene = new Scene(layout, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Student Dashboard");
    primaryStage.show();
    }
}
