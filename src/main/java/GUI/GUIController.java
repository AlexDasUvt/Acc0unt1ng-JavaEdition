package GUI;

import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.Read;
import DBScripts.SPVconf;
import Enums.ReadCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIController extends Application {
    private ObservableList<String> catList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<String> subcatList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<String> PBList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<String> currList = FXCollections.observableList(new ArrayList<>());

    @FXML
    private ComboBox<String> MainCatCombo;

    @FXML
    private TextField MainCommentText;

    @FXML
    private ComboBox<String> MainCurrCombo;

    @FXML
    private DatePicker MainDate;

    @FXML
    private ComboBox<String> MainPBCombo;

    @FXML
    private ComboBox<String> MainSubcatCombo;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Acc0unt1ng-JavaEdition");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        // Initialize and populate the ListViews after FXML fields are injected
        populateList("cat");
        MainCatCombo.setItems(catList);

        populateList("subcat");
        MainSubcatCombo.setItems(subcatList);

        populateList("PB");
        MainPBCombo.setItems(PBList);

        populateList("curr");
        MainCurrCombo.setItems(currList);
    }

    private void populateList(String mode) {
        SPVconf spVconf = new SPVconf(false);
        switch (mode) {
            case "cat":
                List<String> cats = (List<String>) spVconf.readSPVList(mode);
                catList = FXCollections.observableArrayList(cats); // Convert to ObservableList
                break;
            case "subcat":
                List<String> subcats = (List<String>) spVconf.readSPVList(mode);
                subcatList = FXCollections.observableArrayList(subcats); // Convert to ObservableList
                break;
            case "PB":
                // TODO Fix combo population.
                ResultData rd = Read.ReadDB(ReadCode.inits);
                List<Map<String, Object>> data = rd.getMap();
                List<String> PB = new ArrayList<>();
                for (Map<String, Object> userMap : data) {
                    for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                        PB.add(entry.getKey());
                    }
                }
                PBList = FXCollections.observableArrayList(PB); // Convert to ObservableList
                break;
            case "curr":
                List<String> currencies = (List<String>) spVconf.readSPVList(mode);
                currList = FXCollections.observableArrayList(currencies); // Convert to ObservableList
                break;
        }
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
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/FXML/TransferPage.fxml"));
        // Parent root = loader.load();
        // Scene scene = new Scene(root, 800, 500);
        // primaryStage.setTitle("Acc0unt1ng-JavaEdition");
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }

    @FXML
    void SubmitMainRecord(MouseEvent event) {
        String date = MainDate.getValue().toString();
        String cat = MainCatCombo.getSelectionModel().getSelectedItem();
        String subcat = MainPBCombo.getSelectionModel().getSelectedItem();
        String PB = MainPBCombo.getSelectionModel().getSelectedItem();
        double sum = 0;
        try {
            sum = Double.parseDouble(MainSumText.getText());
        } catch (NumberFormatException e) {
            MainSumText.setText("Invalid Sum! Sum must be an integer that is not 0.");
            MainInfoLabel.setTextFill(Color.RED);
            MainInfoLabel.setVisible(true);
        }
        String curr = MainCurrCombo.getSelectionModel().getSelectedItem();
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

            int result;
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