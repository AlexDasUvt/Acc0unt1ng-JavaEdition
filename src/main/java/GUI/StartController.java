package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainPage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Acc0unt1ng-JavaEdition");
        stage.setScene(scene);
        stage.show();
    }

}
