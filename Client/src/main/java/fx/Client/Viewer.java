package fx.Client;

import fx.Client.Controller.Login_Controller;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Viewer extends Application {
    Scene Login_Scene;//登录
    Scene User_Scene;//用户状态
    private static Scene current_Scene;//当前

    @FXML
    private Label logo_Label;
    @Override
    public void start(Stage stage) throws Exception {
        //new Scene(loadFXML("DES_RSA"));
//        ImageView login_Image = new ImageView(new Image("file:main/java/img/logo.png"));

        Login_Scene = new Scene(loadFXML("DES_RSA"));
        current_Scene = Login_Scene;
        stage.setScene(current_Scene);
        stage.show();
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Viewer.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void setRoot(String fxml) throws IOException {
        current_Scene.setRoot(loadFXML(fxml));
    }

}
