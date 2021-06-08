package App;


import App.Controller.User_Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class Starter extends Application {
    Scene Login_Scene;//登录
    Scene User_Scene;//用户状态
    Scene EC_Scene;//加解密展示
    private static Scene current_Scene;//当前
    private static Stage current_Stage;//当前窗口
    private static Stage EC_Stage;//加解密窗口
    private static final Logger logger = Logger.getLogger(Starter.class);

    @Override
    public void start(Stage stage) throws Exception {
        //log
        logger.debug("打开程序");
        //登录窗口
        Login_Scene = new Scene(loadFXML("Login"));
        current_Scene = Login_Scene;
        current_Stage = stage;
        Starter.setRoot("Login", "登录 | 瓜娃子云盘", 420, 512, 420, 512, 420, 512);
        current_Stage.setScene(current_Scene);
        current_Stage.show();
        Starter.setStageIcon(current_Stage);

        Runnable EC_Show_Thread_Task = new Runnable() {
            @Override
            public void run() {
                EC_Show_Thread_Run();
            }
        };
        Thread EC_Show_Background_Thread = new Thread(EC_Show_Thread_Task);
        EC_Show_Background_Thread.setDaemon(true);
        EC_Show_Background_Thread.start();
    }

    public void EC_Show_Thread_Run() {
        try {
            EC_Scene = new Scene(loadFXML("DES_RSA"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                EC_Stage = new Stage();
                EC_Stage.setScene(EC_Scene);
                EC_Stage.setMinWidth(300);
                EC_Stage.setMinHeight(400);
                EC_Stage.setTitle("加解密展示 | 瓜娃子云盘");
                EC_Stage.setX(0);
                EC_Stage.setY(0);
                EC_Stage.show();
                Starter.setStageIcon(EC_Stage);
            }
        });
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage get_Current_Stage() {
        return current_Stage;
    }

    public static void setRoot(String fxml, String title, int width, int height) throws IOException {
        current_Stage.close();
        current_Scene.setRoot(loadFXML(fxml));
        current_Stage.setMinHeight(0);
        current_Stage.setMinWidth(0);
        current_Stage.setMaxHeight(10000);
        current_Stage.setMaxWidth(10000);
        setStage(current_Stage, title, width, height);
        current_Stage.centerOnScreen();
        current_Stage.show();
    }


    public static void setRoot(String fxml, String title, int width, int height, int min_Width, int min_Height) throws IOException {
        current_Stage.close();
        current_Scene.setRoot(loadFXML(fxml));
        int test;
        current_Stage.setMinHeight(min_Height);
        current_Stage.setMinWidth(min_Width);
        current_Stage.setMaxHeight(10000);
        current_Stage.setMaxWidth(10000);
        setStage(current_Stage, title, width, height);
        logger.debug("进入" + fxml + ".fxml");
        current_Stage.centerOnScreen();
        current_Stage.show();
        current_Stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            User_Controller.stage_Changed(current_Stage.getWidth());
        });
    }

    public static void setRoot(String fxml, String title, int width, int height, int min_Width, int min_Height, int max_Width, int max_Height) throws IOException {
        current_Stage.close();
        current_Scene.setRoot(loadFXML(fxml));
        current_Stage.setMinHeight(min_Height);
        current_Stage.setMinWidth(min_Width);
        current_Stage.setMaxWidth(max_Width);
        current_Stage.setMinHeight(max_Height);
        setStage(current_Stage, title, width, height);
        logger.debug("进入" + fxml + ".fxml");
        current_Stage.centerOnScreen();
        current_Stage.show();
    }

    static void setStage(Stage stage, String title) {
        stage.setTitle(title);
    }

    static void setStage(Stage stage, String title, int width, int height) {
        stage.setTitle(title);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    static void setStageIcon(Stage stage) {
        URL icon_Url = Starter.class.getResource("img/logo.png");
        stage.getIcons().add(new Image(icon_Url.toExternalForm()));
    }

}
