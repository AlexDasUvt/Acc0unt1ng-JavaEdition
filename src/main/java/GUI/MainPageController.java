package GUI;

import API.HTTPSender;
import DBObjects.RecordData;
import DBObjects.ResultData;
import DBScripts.Read;
import DBScripts.SPVconf;
import Enums.ReadCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
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
    private Label MainInfoLabel;

    @FXML
    private Button MenuBalanceButton;

    @FXML
    private Button MenuHistoryButton;

    @FXML
    private Button MenuMainButton;

    @FXML
    private Button MenuTransferButton;

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
                List<String> cat = spVconf.readSPVList(mode);
                catList = FXCollections.observableArrayList(cat); // Convert to ObservableList
                break;
            case "subcat":
                List<String> subcat = spVconf.readSPVList(mode);
                subcatList = FXCollections.observableArrayList(subcat); // Convert to ObservableList
                break;
            case "PB":
                ResultData rd = Read.ReadDB(ReadCode.inits);
                List<Map<String, Object>> data = rd.getMap();
                List<String> PB = new ArrayList<>();
                for (Map<String, Object> userMap : data) {
                    // Check if the userMap contains the key for 'person-bank'
                    if (userMap.containsKey("person_bank")) {
                        // Get the value for the 'person-bank' column
                        Object personBankValue = userMap.get("person_bank");
                        if (personBankValue != null) {
                            PB.add(personBankValue.toString()); // Convert to String and add to the list
                        }
                    }
                }
                PBList = FXCollections.observableArrayList(PB); // Convert to ObservableList
                break;
            case "curr":
                List<String> curr = spVconf.readSPVList(mode);
                currList = FXCollections.observableArrayList(curr); // Convert to ObservableList
                break;
        }
    }

    @FXML
    void SubmitMainRecord(MouseEvent event) {
        if (MainDate.getValue() == null) {
            return;
        }
        String date = MainDate.getValue().toString();
        if (MainCatCombo.getValue() == null) {
            return;
        }
        String cat = MainSubcatCombo.getSelectionModel().getSelectedItem();
        if (MainDate.getValue() == null) {
            return;
        }
        String subcat = MainPBCombo.getSelectionModel().getSelectedItem();
        if (MainDate.getValue() == null) {
            return;
        }
        String PB = MainPBCombo.getSelectionModel().getSelectedItem();
        double sum = 0;
        try {
            sum = Double.parseDouble(MainSumText.getText());
        } catch (NumberFormatException e) {
            MainInfoLabel.setText("Invalid Sum! Sum must be an integer that is not 0.");
            MainInfoLabel.setTextFill(Color.RED);
            MainInfoLabel.setVisible(true);
            return;
        }
        if (MainCurrCombo.getValue() == null) {
            return;
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
            HTTPSender sender = null;
            try {
                sender = new HTTPSender();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            JSON = getJSON(transaction);

            int result;
            result = sender.AddRecordHTTP("main", JSON);
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

    @FXML
    void MenuBalanceButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/BalancePage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void MenuHistoryButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/HistoryPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void MenuTransferButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/TransferPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private String getJSON(RecordData transaction) {
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