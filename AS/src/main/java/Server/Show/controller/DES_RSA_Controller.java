package Server.Show.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;


public class DES_RSA_Controller implements Initializable {
    @FXML
    private ScrollPane scroll_pane;

    VBox Show_Vbox_Pane = new VBox();

    final Double short_Width = 250d;
    final Double long_Width = 4000d;
    final Double fixed_Height = 20d;
    final Double fixed_X = 10d;

    public static EC_Show_Queue ec_show_queue = new EC_Show_Queue();

    public static void EC_Show_Appendent(Boolean is_DES, Boolean is_Encrypt, String DES_Key, String RSA_Pkey, String RSA_Skey, String original_Text, String encrypted_Text) {
        ec_show_queue.appendent(is_DES, is_Encrypt, DES_Key, RSA_Pkey, RSA_Skey, original_Text, encrypted_Text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Runnable EC_Show_Thread_Task = new Runnable() {
            @Override
            public void run() {
                try {
                    Show_Monitor_Thread_Run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread EC_Show_Background_Thread = new Thread(EC_Show_Thread_Task);
        EC_Show_Background_Thread.setDaemon(true);
        EC_Show_Background_Thread.start();
    }

    public void Show_Monitor_Thread_Run() throws InterruptedException {//变化监视进程
        int left = 0;
        Boolean is_DES = false;
        Boolean is_Encrypt = false;
        String DES_Key = "";
        String RSA_PKey = "";
        String RSA_SKey = "";
        String original_Text = "";
        String encrypted_Text = "";
        while (true) {
            int get_Left = ec_show_queue.get_Left();
            if (get_Left != left) {
                int turn = get_Left - left;
                for (int i = 0; i < turn; i++) {
                    String[] show = ec_show_queue.get();
                    if (show[0].equals("true")) {//des
                        is_DES = true;
                    } else {//RSA
                        is_DES = false;
                    }
                    if (show[1].equals("true")) {
                        is_Encrypt = true;
                    } else {
                        is_Encrypt = false;
                    }
                    DES_Key = show[2];
                    RSA_PKey = show[3];
                    RSA_SKey = show[4];
                    original_Text = show[5];
                    encrypted_Text = show[6];
                    show_One_Content(is_DES, is_Encrypt, DES_Key, RSA_PKey, RSA_SKey, original_Text, encrypted_Text);
                    left = get_Left;
                }
            }
            Thread.sleep(200);
        }
    }


    public void show_One_Content(Boolean is_DES, Boolean is_Encrypt, String DES_Key, String RSA_Pkey, String RSA_Skey, String original_Text, String encrypted_Text) {
        Pane Show_One_Pane = new Pane();
        Double current_Show_Pane_Height = 0d;
        String type_Show = "";
        if (is_Encrypt) {
            if (is_DES) {
                type_Show = "DES-加密";
            } else {
                type_Show = "RSA-加密";
            }
        } else {
            if (is_DES) {
                type_Show = "DES-解密";
            } else {
                type_Show = "RSA-解密";
            }
        }
        //默认字体设置
        Font default_Show_Font = new Font("Microsoft YaHei", 14.5);
        //加密类型段
        Label type_Show_Label = new Label();
        type_Show_Label.setFont(default_Show_Font);//字体
        type_Show_Label.setText("加密类型：\t" + type_Show);//文字
        type_Show_Label.setPrefWidth(short_Width);//宽度
        type_Show_Label.setPrefHeight(fixed_Height);//高度
        type_Show_Label.setLayoutX(fixed_X);//x位置
        type_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
        current_Show_Pane_Height += fixed_Height;//y位置增加
        Show_One_Pane.getChildren().add(type_Show_Label);

        if (is_DES) {
            //密钥段
            Label key_Show_Label = new Label();
            key_Show_Label.setFont(default_Show_Font);//字体
            key_Show_Label.setText("密钥：\t" + DES_Key);//文字
            key_Show_Label.setPrefWidth(long_Width);//宽度
            key_Show_Label.setPrefHeight(fixed_Height);//高度
            key_Show_Label.setLayoutX(fixed_X);//x位置
            key_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
            current_Show_Pane_Height += fixed_Height;//y位置增加
            Show_One_Pane.getChildren().add(key_Show_Label);
        } else {
            //密钥段
            Label pkey_Show_Label = new Label();
            pkey_Show_Label.setFont(default_Show_Font);//字体
            pkey_Show_Label.setText("公钥：\t" + RSA_Pkey);//文字
            pkey_Show_Label.setPrefWidth(long_Width);//宽度
            pkey_Show_Label.setPrefHeight(fixed_Height);//高度
            pkey_Show_Label.setLayoutX(fixed_X);//x位置
            pkey_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
            current_Show_Pane_Height += fixed_Height;//y位置增加
            Show_One_Pane.getChildren().add(pkey_Show_Label);

            Label skey_Show_Label = new Label();
            skey_Show_Label.setFont(default_Show_Font);//字体
            skey_Show_Label.setText("私钥：\t" + RSA_Skey);//文字
            skey_Show_Label.setPrefWidth(long_Width);//宽度
            skey_Show_Label.setPrefHeight(fixed_Height);//高度
            skey_Show_Label.setLayoutX(fixed_X);//x位置
            skey_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
            current_Show_Pane_Height += fixed_Height;//y位置增加
            Show_One_Pane.getChildren().add(skey_Show_Label);
        }
        //明文段
        Label origion_Text_Show_Label = new Label();
        origion_Text_Show_Label.setFont(default_Show_Font);//字体
        origion_Text_Show_Label.setText("明文：\t" + original_Text);//文字
        origion_Text_Show_Label.setPrefWidth(long_Width);//宽度
        origion_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        origion_Text_Show_Label.setLayoutX(fixed_X);//x位置
        origion_Text_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
        current_Show_Pane_Height += fixed_Height;//y位置增加
        Show_One_Pane.getChildren().add(origion_Text_Show_Label);

        //密文段
        Label encrypted_Text_Show_Label = new Label();
        encrypted_Text_Show_Label.setFont(default_Show_Font);//字体
        encrypted_Text_Show_Label.setText("密文：\t" + encrypted_Text);//文字
        encrypted_Text_Show_Label.setPrefWidth(long_Width);//宽度
        encrypted_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        encrypted_Text_Show_Label.setLayoutX(fixed_X);//x位置
        encrypted_Text_Show_Label.setLayoutY(current_Show_Pane_Height);//y位置
        current_Show_Pane_Height += fixed_Height;//y位置增加
        Show_One_Pane.getChildren().add(encrypted_Text_Show_Label);

        //分隔符
        Separator separat = new Separator();
        separat.setPrefWidth(long_Width);
        separat.setPrefHeight(fixed_Height);
        separat.setLayoutX(fixed_X);
        separat.setLayoutY(current_Show_Pane_Height);
        current_Show_Pane_Height += fixed_Height;
        Show_One_Pane.getChildren().add(separat);

        //宽高设置
        Show_One_Pane.setPrefHeight(current_Show_Pane_Height);
        Show_One_Pane.setPrefWidth(long_Width);

        //current_Vbox_Height += current_Show_Pane_Height;
        //将自己添加进Vbox

        Platform.runLater(new Runnable() {//异步更新UI
            @Override
            public void run() {
                Show_Vbox_Pane.getChildren().add(Show_One_Pane);
                scroll_pane.setContent(Show_Vbox_Pane);
            }
        });
    }
}

