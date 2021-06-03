package fx.Client.Controller;

import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;


public class Login_Controller implements Initializable {
    @FXML
    private Button login_Button;
    @FXML
    private Button register_Button;
    @FXML
    private Label logo_Label;

    @FXML
    private void login_Button_Choosed() {

    }

    @FXML
    private void register_Button_Choosed() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL url = getClass().getClassLoader().getResource("resources/img/logo.png");
        ImageView logo_Image = new ImageView(url.toExternalForm());
        logo_Image.setFitHeight(48);
        logo_Image.setFitWidth(48);
        logo_Label.setGraphic(logo_Image);
    }
}
