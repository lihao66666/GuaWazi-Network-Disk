package fx.Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;


public class DES_RSA_Controller {
    @FXML
    private Button testButton;

    @FXML
    private ScrollPane scroll_pane;

    Double current_Height = 0d;
    Pane Show_Pane = new Pane();
    final Double short_Width = 250d;
    final Double long_Width = 4000d;
    final Double fixed_Height = 20d;
    final Double fixed_X = 10d;
    final int MaxTime = 350;
    int time = 0;

    /**
     * 显示DES的加解密
     *
     * @param is_Encrypt     ture:显示“DES-加密” false:显示“DES-解密”
     * @param key            密钥
     * @param origion_Text   明文
     * @param encrypted_Text 密文
     */
    public void DES_Appendent(boolean is_Encrypt, String key, String origion_Text, String encrypted_Text) {
        Pane Show_DES = new Pane();
        //限制最大350
        time++;
        if (time <= MaxTime) {
            Show_DES.getChildren().add(Show_Pane);
        } else {
            Show_Pane = null;
            Show_Pane = new Pane();
            time = 0;
            current_Height = 0d;
        }
        String type_Show = "";
        if (is_Encrypt) {
            type_Show = "DES-加密";
        } else {
            type_Show = "DES-解密";
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
        type_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加
        //密钥段
        Label key_Show_Label = new Label();
        key_Show_Label.setFont(default_Show_Font);//字体
        key_Show_Label.setText("密钥：\t" + key);//文字
        key_Show_Label.setPrefWidth(short_Width);//宽度
        key_Show_Label.setPrefHeight(fixed_Height);//高度
        key_Show_Label.setLayoutX(fixed_X);//x位置
        key_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加

        //明文段
        Label origion_Text_Show_Label = new Label();
        origion_Text_Show_Label.setFont(default_Show_Font);//字体
        origion_Text_Show_Label.setText("明文：\t" + origion_Text);//文字
        origion_Text_Show_Label.setPrefWidth(long_Width);//宽度
        origion_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        origion_Text_Show_Label.setLayoutX(fixed_X);//x位置
        origion_Text_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加
        //密文段
        Label encrypted_Text_Show_Label = new Label();
        encrypted_Text_Show_Label.setFont(default_Show_Font);//字体
        encrypted_Text_Show_Label.setText("密文：\t" + encrypted_Text);//文字
        encrypted_Text_Show_Label.setPrefWidth(long_Width);//宽度
        encrypted_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        encrypted_Text_Show_Label.setLayoutX(fixed_X);//x位置
        encrypted_Text_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加

        //分隔符
        Separator separat = new Separator();
        separat.setPrefWidth(long_Width);
        separat.setPrefHeight(fixed_Height);
        separat.setLayoutX(fixed_X);
        separat.setLayoutY(current_Height);
        current_Height += fixed_Height;


        Show_DES.getChildren().add(type_Show_Label);
        Show_DES.getChildren().add(key_Show_Label);
        Show_DES.getChildren().add(origion_Text_Show_Label);
        Show_DES.getChildren().add(encrypted_Text_Show_Label);
        Show_DES.getChildren().add(separat);
        Show_DES.setPrefHeight(current_Height);
        Show_DES.setPrefWidth(long_Width);
        scroll_pane.setContent(Show_DES);
        Show_Pane = Show_DES;
    }

    public void RSA_Appendent(boolean is_Encrypt, String public_key, String private_key, String origion_Text, String encrypted_Text) {
        Pane Show_RSA = new Pane();
        //限制最大350
        time++;
        if (time <= MaxTime) {
            Show_RSA.getChildren().add(Show_Pane);
        } else {
            Show_Pane = null;
            Show_Pane = new Pane();
            time = 0;
            current_Height = 0d;
        }
        String type_Show = "";
        if (is_Encrypt) {
            type_Show = "RSA-加密";
        } else {
            type_Show = "RSA-解密";
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
        type_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加
        //密钥段
        Label pkey_Show_Label = new Label();
        pkey_Show_Label.setFont(default_Show_Font);//字体
        pkey_Show_Label.setText("密钥：\t" + public_key);//文字
        pkey_Show_Label.setPrefWidth(long_Width);//宽度
        pkey_Show_Label.setPrefHeight(fixed_Height);//高度
        pkey_Show_Label.setLayoutX(fixed_X);//x位置
        pkey_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加

        Label skey_Show_Label = new Label();
        skey_Show_Label.setFont(default_Show_Font);//字体
        skey_Show_Label.setText("密钥：\t" + private_key);//文字
        skey_Show_Label.setPrefWidth(long_Width);//宽度
        skey_Show_Label.setPrefHeight(fixed_Height);//高度
        skey_Show_Label.setLayoutX(fixed_X);//x位置
        skey_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加

        //明文段
        Label origion_Text_Show_Label = new Label();
        origion_Text_Show_Label.setFont(default_Show_Font);//字体
        origion_Text_Show_Label.setText("明文：\t" + origion_Text);//文字
        origion_Text_Show_Label.setPrefWidth(long_Width);//宽度
        origion_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        origion_Text_Show_Label.setLayoutX(fixed_X);//x位置
        origion_Text_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加
        //密文段
        Label encrypted_Text_Show_Label = new Label();
        encrypted_Text_Show_Label.setFont(default_Show_Font);//字体
        encrypted_Text_Show_Label.setText("密文：\t" + encrypted_Text);//文字
        encrypted_Text_Show_Label.setPrefWidth(long_Width);//宽度
        encrypted_Text_Show_Label.setPrefHeight(fixed_Height);//高度
        encrypted_Text_Show_Label.setLayoutX(fixed_X);//x位置
        encrypted_Text_Show_Label.setLayoutY(current_Height);//y位置
        current_Height += fixed_Height;//y位置增加

        //分隔符
        Separator separat = new Separator();
        separat.setPrefWidth(long_Width);
        separat.setPrefHeight(fixed_Height);
        separat.setLayoutX(fixed_X);
        separat.setLayoutY(current_Height);
        current_Height += fixed_Height;


        Show_RSA.getChildren().add(type_Show_Label);
        Show_RSA.getChildren().add(pkey_Show_Label);
        Show_RSA.getChildren().add(skey_Show_Label);
        Show_RSA.getChildren().add(origion_Text_Show_Label);
        Show_RSA.getChildren().add(encrypted_Text_Show_Label);
        Show_RSA.getChildren().add(separat);
        Show_RSA.setPrefHeight(current_Height);
        Show_RSA.setPrefWidth(long_Width);
        scroll_pane.setContent(Show_RSA);
        Show_Pane = Show_RSA;
    }


    @FXML
    public void add_des() {
        for (int i = 1; i <= 350; i++) {
            if (i % 2 == 0) {
                System.out.println(String.valueOf(i));
                DES_Appendent(false, String.valueOf(i) + "asfduiniuadnfiu", String.valueOf(i) + "bdasfiuhiuadfiubiusadbifubasiu", String.valueOf(i) + "dabfiasbiubfasuibdiasub");
            } else {
                System.out.println(String.valueOf(i));
                RSA_Appendent(false, String.valueOf(i) + "anidfuianfdiunasiufn", String.valueOf(i) + "asfduiniuadnfiu", String.valueOf(i) + "bdasfiuhiuadfiubiusadbifubasiu", String.valueOf(i) + "dabfiasbiubfasuibdiasub");
            }
        }
    }
}
