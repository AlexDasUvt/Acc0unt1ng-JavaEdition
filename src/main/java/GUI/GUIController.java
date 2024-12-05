package GUI;

import DBObjects.RecordData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.MalformedURLException;

public class GUIController extends Application {
    @FXML
    private ListView<?> MainCatList;

    @FXML
    private TextField MainCommentText;

    @FXML
    private ListView<?> MainCurrList;

    @FXML
    private DatePicker MainDate;

    @FXML
    private ListView<?> MainPBList;

    @FXML
    private ListView<?> MainSubcatList;

    @FXML
    private Button MainSubmitButton;

    @FXML
    private TextField MainSumText;

    @FXML
    private Button MenuBalanceButton;

    @FXML
    private Button MenuMainButton;

    @FXML
    private Button MenuHistoryButton;

    @FXML
    private Button MenuTransferButton;

    @FXML
    private Label MainInfoLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/FXML/MainPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Acc0unt1ng-JavaEdition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void MenuBalanceButton(MouseEvent event) {

    }

    @FXML
    void MenuHistoryButton(MouseEvent event) {

    }

    @FXML
    void MenuMainButton(MouseEvent event) {

    }

    @FXML
    void MenuTransferButton(MouseEvent event) {

    }

    @FXML
    void SubmitMainRecord(MouseEvent event) {
        String date = MainDate.getValue().toString();
        String cat = MainCatList.getSelectionModel().getSelectedItem().toString();
        String subcat = MainPBList.getSelectionModel().getSelectedItem().toString();
        String PB = MainPBList.getSelectionModel().getSelectedItem().toString();
        double sum = 0;
        try {
            sum = Double.parseDouble(MainSumText.getText());
        } catch (NumberFormatException e) {
            MainSumText.setText("Invalid Sum! Sum must be an integer that is not 0.");
            MainInfoLabel.setTextFill(Color.RED);
            MainInfoLabel.setVisible(true);
        }
        String curr = MainCurrList.getSelectionModel().getSelectedItem().toString();
        String comment = MainCommentText.getText();

        RecordData transaction = new RecordData(date, cat, subcat, PB, null, sum, curr, comment);

        if (!transaction.isValid()) {
            MainInfoLabel.setText("You have to select date, category, Person-bank, sum and currency!");
            MainInfoLabel.setTextFill(Color.RED);
            MainInfoLabel.setVisible(true);
        } else {

            String JSON;
            HTTPSender sender = new HTTPSender();
            JSON = getJSON(transaction);

            int result = 0;
            try {
                result = sender.SendHTTP("main", JSON);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            if (result == 200) {
                MainInfoLabel.setText("Record write successful!");
                MainInfoLabel.setTextFill(Color.GREEN);
                MainInfoLabel.setVisible(true);
            } else {
                MainInfoLabel.setText("Record write failed! Code: " + result);
                MainInfoLabel.setTextFill(Color.RED);
                MainInfoLabel.setVisible(true);
            }
        }

    }

    private static String getJSON(RecordData transaction) {
        String JSON;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSON = objectMapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return JSON;
    }

}