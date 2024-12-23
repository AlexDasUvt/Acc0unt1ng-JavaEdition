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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TransferPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ObservableList<String> PBList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<String> currList = FXCollections.observableList(new ArrayList<>());

    @FXML
    private Label TransferInfoLabel;

    @FXML
    private TextField TransferCommentText;

    @FXML
    private ComboBox<String> TransferCurrCombo;

    @FXML
    private DatePicker TransferDate;

    @FXML
    private ComboBox<String> TransferPBFCombo;

    @FXML
    private ComboBox<String> TransferPBTCombo;

    @FXML
    private Button TransferSubmitButton;

    @FXML
    private TextField TransferSumText;

    @FXML
    public void initialize() {
        // Initialize and populate the ListViews after FXML fields are injected
        populateList("PB");
        TransferPBFCombo.setItems(PBList);
        TransferPBTCombo.setItems(PBList);

        populateList("curr");
        TransferCurrCombo.setItems(currList);
    }

    private void populateList(String mode) {
        SPVconf spVconf = new SPVconf(false);
        switch (mode) {
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
    void SubmitTransferRecord(MouseEvent event) {
        if (TransferDate.getValue() == null) {
            return;
        }
        String date = TransferDate.getValue().toString();
        if (TransferPBTCombo.getValue() == null) {
            return;
        }
        String PBTo = TransferPBTCombo.getSelectionModel().getSelectedItem();
        if (TransferPBFCombo.getValue() == null) {
            return;
        }
        String PBFrom = TransferPBFCombo.getSelectionModel().getSelectedItem();

        if (Objects.equals(PBFrom, PBTo)) {
            TransferInfoLabel.setText("Invalid Receiver! Receiver and Sender should not be equal.");
            TransferInfoLabel.setTextFill(Color.RED);
            TransferInfoLabel.setVisible(true);
            return;
        }
        double sum = 0;
        try {
            sum = Double.parseDouble(TransferSumText.getText());
        } catch (NumberFormatException e) {
            TransferInfoLabel.setText("Invalid Sum! Sum must be an integer that is not 0.");
            TransferInfoLabel.setTextFill(Color.RED);
            TransferInfoLabel.setVisible(true);
            return;
        }
        if (TransferCurrCombo.getValue() == null) {
            return;
        }
        String curr = TransferCurrCombo.getSelectionModel().getSelectedItem();
        String comment = TransferCommentText.getText();

        RecordData transaction = new RecordData(date, null, null, PBTo, PBFrom, sum, curr, comment);

        if (!transaction.isValid()) {
            TransferInfoLabel.setText("You have to select date, category, Person-bank, sum and currency!");
            TransferInfoLabel.setTextFill(Color.RED);
            TransferInfoLabel.setVisible(true);
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
            result = sender.AddRecordHTTP("transfer", JSON);
            if (result == 200) {
                TransferInfoLabel.setText("Record write successful!");
                TransferInfoLabel.setTextFill(Color.GREEN);
                TransferInfoLabel.setVisible(true);
            } else {
                TransferInfoLabel.setText("Record write failed! Code: " + result);
                TransferInfoLabel.setTextFill(Color.RED);
                TransferInfoLabel.setVisible(true);
            }
        }

    }

    @FXML
    void MenuBalanceButton(MouseEvent event) {

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
    void MenuMainButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/MainPage.fxml"));
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
