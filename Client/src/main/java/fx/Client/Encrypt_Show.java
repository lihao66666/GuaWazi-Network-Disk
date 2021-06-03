package fx.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


import java.io.IOException;

public class Encrypt_Show {
    @FXML
    private void test() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Encrypt_Show.fxml"));
        //Button currentButton = (Button)root.lookup("testButton");
        //currentButton.setText("戳了");
        Label test = new Label();
        test.setLayoutX(30);
        test.setLayoutY(100);
        test.setText("测试");
        test.setVisible(true);

    }

}
