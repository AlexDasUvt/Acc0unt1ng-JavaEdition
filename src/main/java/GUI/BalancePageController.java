package GUI;

import API.HTTPSender;
import DBObjects.ResultData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalancePageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ObservableList<BalanceData> balanceData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<BalanceData, String> BalCurrCol;

    @FXML
    private TableColumn<BalanceData, String> BalPBCol;

    @FXML
    private TableColumn<BalanceData, String> BalSumCol;

    @FXML
    private TableView<BalanceData> MainTable;

    @FXML
    private Button MenuHistoryButton;

    @FXML
    private Button MenuMainButton;

    @FXML
    private Button MenuTransferButton;

    @FXML
    public void initialize() {
        BalSumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));
        BalCurrCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        BalPBCol.setCellValueFactory(new PropertyValueFactory<>("pb"));

        loadBalanceData();
    }

    private void loadBalanceData() {
        HTTPSender httpSender;
        try {
            httpSender = new HTTPSender();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String json = toJSON();
        ResultData resultData;
        resultData = httpSender.ReadDBHTTP(json);

        if (resultData != null && resultData.isValid()) {
            List<Map<String, Object>> resultList = resultData.getMap();

            for (Map<String, Object> row : resultList) {
                String sum = row.get("total_balance").toString();
                String currency = row.get("currency").toString();
                String pb = row.get("person_bank").toString();

                balanceData.add(new BalanceData(pb, sum, currency));
            }

            MainTable.setItems(balanceData);
        }
    }

    private String toJSON() {
        String key = "command";

        Map<String, String> map = new HashMap<>();
        map.put(key, "bal");

        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
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

    @FXML
    void MenuTransferButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/TransferPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
