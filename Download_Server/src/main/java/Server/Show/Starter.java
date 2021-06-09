package Server.Show;

import Server.SocketServer;
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
import java.sql.SQLException;

public class Starter extends Application {
    Scene Login_Scene;//登录
    Scene EC_Scene;//加解密展示
    private static Scene current_Scene;//当前
    private static Stage current_Stage;//当前窗口
    private static Stage EC_Stage;//加解密窗口
    private static final Logger logger = Logger.getLogger(Starter.class);

    @Override
    public void start(Stage stage) throws Exception {
        //log
        logger.debug("监控开启");
        //登录窗口
        Login_Scene = new Scene(loadFXML("do_Not_Delete"));
        current_Scene = Login_Scene;
        current_Stage = stage;
        Starter.setRoot("do_Not_Delete", "", 0, 0, 0, 0, 0, 0);
        current_Stage.setX(10);
        current_Stage.setY(10);
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

        Runnable main_Server_Thread_Task = new Runnable() {
            @Override
            public void run() {
                try {
                    Server_Main_App();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread main_Server_Thread = new Thread(main_Server_Thread_Task);
        main_Server_Thread.setDaemon(true);
        main_Server_Thread.start();

    }

    public void EC_Show_Thread_Run() {
        try {
            EC_Scene = new Scene(loadFXML("DES_RSA"));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    EC_Stage = new Stage();
                    EC_Stage.setScene(EC_Scene);
                    EC_Stage.setMinWidth(300);
                    EC_Stage.setMinHeight(400);
                    EC_Stage.setTitle("加解密展示 | Download Server端 | 瓜娃子云盘");
                    EC_Stage.setX(0);
                    EC_Stage.setY(0);
                    EC_Stage.show();
                    Starter.setStageIcon(EC_Stage);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
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

    static void setStage(Stage stage, String title, int width, int height) {
        stage.setTitle(title);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    static void setStageIcon(Stage stage) {
        URL icon_Url = Starter.class.getResource("img/logo.png");
        stage.getIcons().add(new Image(icon_Url.toExternalForm()));
    }


    public void Server_Main_App() throws SQLException, ClassNotFoundException {
        SocketServer server=new SocketServer(8891);
        server.ServerListener();
    }


}
