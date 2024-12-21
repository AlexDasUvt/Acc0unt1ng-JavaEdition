package GUI;

import DBObjects.ResultData;
import DBScripts.Read;
import Enums.ReadCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HistoryPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ObservableList<HistoryMainData> historyMainData = FXCollections.observableArrayList();
    private ObservableList<HistoryTransferData> historyTransferData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<HistoryMainData, String> MainCatCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainCommentCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainCurrCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainDateCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainIdCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainPBCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainSubcatCol;

    @FXML
    private TableColumn<HistoryMainData, String> MainSumCol;

    @FXML
    private TableView<HistoryMainData> MainTable;

    @FXML
    private Button MenuBalanceButton;

    @FXML
    private Button MenuHistoryButton;

    @FXML
    private Button MenuMainButton;

    @FXML
    private Button MenuTransferButton;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferCommentCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferCurrCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferDateCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferIdCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferReceiverCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferSenderCol;

    @FXML
    private TableColumn<HistoryTransferData, String> TransferSumCol;

    @FXML
    private TableView<HistoryTransferData> TransferTable;

    @FXML
    public void initialize() {
        MainIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        MainSumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));
        MainCurrCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        MainCatCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        MainSubcatCol.setCellValueFactory(new PropertyValueFactory<>("subcategory"));
        MainCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));
        MainPBCol.setCellValueFactory(new PropertyValueFactory<>("pb"));

        loadHistoryData("main");

        TransferIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TransferDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TransferSumCol.setCellValueFactory(new PropertyValueFactory<>("sum"));
        TransferCurrCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        TransferSenderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        TransferReceiverCol.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        TransferCommentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        loadHistoryData("transfer");
    }

    private void loadHistoryData(String code) {
        if (Objects.equals(code, "main")) {
            ResultData resultData = Read.ReadDB(ReadCode.allm);

            if (resultData != null && resultData.isValid()) {
                List<Map<String, Object>> resultList = resultData.getMap();

                for (Map<String, Object> row : resultList) {
                    String id = row.get("id").toString();
                    String date = row.get("date").toString();
                    String sum = row.get("sum").toString();
                    String currency = row.get("currency").toString();
                    String category = row.get("category").toString();
                    String subcategory = row.get("subcategory").toString();
                    String comment = row.get("comment").toString();
                    String pb = row.get("pb").toString();

                    historyMainData.add(new HistoryMainData(id, date, sum, currency, category, subcategory, comment, pb));
                }

                MainTable.setItems(historyMainData);
            }
        } else if (Objects.equals(code, "transfer")) {
            ResultData resultData = Read.ReadDB(ReadCode.tran);

            if (resultData != null && resultData.isValid()) {
                List<Map<String, Object>> resultList = resultData.getMap();

                for (Map<String, Object> row : resultList) {
                    String id = row.get("id").toString();
                    String date = row.get("date").toString();
                    String sum = row.get("sum").toString();
                    String currency = row.get("currency").toString();
                    String pbTo = row.get("person_bank_to").toString();
                    String pbFrom = row.get("person_bank_from").toString();
                    String comment = row.get("comment").toString();

                    historyTransferData.add(new HistoryTransferData(id, date, sum, currency, pbTo, pbFrom, comment));
                }

                TransferTable.setItems(historyTransferData);
            }
        }
    }

    @FXML
    void MenuBalanceButton(MouseEvent event) {

    }

    @FXML
    void MenuTransferButton(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/FXML/TransferPage.fxml"));
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
}
