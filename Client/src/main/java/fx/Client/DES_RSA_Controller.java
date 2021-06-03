package fx.Client;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class DES_RSA_Controller {
    @FXML
    private Button testButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private AnchorPane show_pane;
    @FXML
    private ScrollPane scrow_pane;

//
//    private List

    public void DES_Appendent(int is_Encrypt, String key, String origion_Text, String encrypted_Text) {

    }

    @FXML
    public void testPressed() {
        mainPane.setPrefHeight(1000);
        mainPane.setStyle("-fx-background-color:#1E90FF;");
        //Scene scene=new Scene(mainPane);

//        t4.setText("this");
//        String t = t4.getText();
//        System.out.println(t);
////
////        Alert alert;
////        alert = new Alert(Alert.AlertType.ERROR);
////        alert.setContentText("账号密码错误！");
////        alert.show();
////        t2.setText("pressed!");
        for (int i = 0; i < 20; i++) {
            Label testL = new Label();
            testL.setLayoutX(10);
            testL.setLayoutY(100 * i);
            testL.setPrefHeight(20);
            testL.setPrefWidth(200);
            testL.setText("test\t\tx: " + testL.getLayoutX() + "\ty: " + testL.getLayoutY());
            show_pane.setMaxHeight(Double.MAX_VALUE);
            show_pane.setPrefHeight(show_pane.getHeight() + 100d);
//            //show_pane.set
//            show_pane.setMinSize(show_pane.getWidth(),(show_pane.getHeight() + 100));
//            show_pane.setPrefSize(show_pane.getWidth(),(show_pane.getHeight() + 100));
//            show_pane.setMaxSize(show_pane.getWidth(),(show_pane.getHeight() + 100));

            show_pane.getChildren().add(testL);
            System.out.println(testL.getLayoutY() + "\t" + show_pane.getHeight());
        }
    }
}
