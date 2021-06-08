package App.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Random;


public class DES_RSA_Controller {
    @FXML
    private Button testButton;

    @FXML
    private ScrollPane scroll_pane;

    //Double current_Height = 0d;
    //Pane Show_Pane = new Pane();
    VBox Show_Vbox_Pane = new VBox();

    //Double current_Vbox_Height = 0d;
    final Double short_Width = 250d;
    final Double long_Width = 4000d;
    final Double fixed_Height = 20d;
    final Double fixed_X = 10d;
    //final int MaxTime = 350;
    //int time = 0;
    int test_press = 0;
    //EC_Show_ArrayList Show_Content;

    @FXML
    public void add_des() {
        Runnable Button_Change_Thread_Task = new Runnable() {
            @Override
            public void run() {
                Button_Thread();
            }
        };

        Thread Button_Change_Thread = new Thread(Button_Change_Thread_Task);
        Button_Change_Thread.setDaemon(true);
        Button_Change_Thread.start();


        Runnable EC_Show_Thread_Task = new Runnable() {
            @Override
            public void run() {
                test_Run();
            }
        };
        Thread EC_Show_Background_Thread = new Thread(EC_Show_Thread_Task);
        EC_Show_Background_Thread.setDaemon(true);
        EC_Show_Background_Thread.start();
    }


    private void test_Run() {
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            Boolean is_DES = r.nextBoolean();
            Boolean is_Encrypt = r.nextBoolean();
            String front_text;
            if (is_DES) {
                front_text = i + "\tDES\t";
            } else {
                front_text = i + "\tRSA\t";
            }
            String DES_Key = front_text + "DES_Key\t\t\t" + r.nextLong();
            String RSA_PKey = front_text + "RSA_PKey\t\t\t" + r.nextLong();
            String RSA_SKey = front_text + "RSA_SKey\t\t\t" + r.nextLong();
            String original_Text = front_text + "original Text\t\t" + r.nextLong();
            String encrypted_Text = front_text + "encrypted Text\t" + String.valueOf(r.nextLong());
            Show_Thread_Run(is_DES, is_Encrypt, DES_Key, RSA_PKey, RSA_SKey, original_Text, encrypted_Text);
        }
    }

    public void Button_Thread() {
        test_press++;
        final String test_pressed_Text = String.valueOf(test_press) + "pressed!";
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                testButton.setText(test_pressed_Text);
            }
        });
    }

    public void Show_Thread_Run(Boolean is_DES, Boolean is_Encrypt, String DES_Key, String RSA_PKey, String RSA_SKey, String original_Text, String encrypted_Text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                show_One_Content(is_DES, is_Encrypt, DES_Key, RSA_PKey, RSA_SKey, original_Text, encrypted_Text);
            }
        });
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
        Show_Vbox_Pane.getChildren().add(Show_One_Pane);
        scroll_pane.setContent(Show_Vbox_Pane);
    }

}

//    public void update_Show_All() {
//        Show_Vbox_Pane = null;//设空
//        current_Vbox_Height = 0d;//初始化
//
//        //管理需要显示传入的数据
//
//
//        Show_Vbox_Pane.setPrefHeight(current_Vbox_Height);
//    }

//
//    /**
//     * 显示DES的加解密
//     *
//     * @param is_Encrypt     ture:显示“DES-加密” false:显示“DES-解密”
//     * @param key            密钥
//     * @param origion_Text   明文
//     * @param encrypted_Text 密文
//     */
//    public void DES_Appendent(Boolean is_Encrypt, String key, String origion_Text, String encrypted_Text) {
//        Pane Show_DES = new Pane();
//        //限制最大350
//        time++;
//        if (time <= MaxTime) {
//            Show_DES.getChildren().add(Show_Pane);
//        } else {
//            Show_Pane = null;
//            Show_Pane = new Pane();
//            time = 0;
//            current_Height = 0d;
//        }
//        String type_Show = "";
//        if (is_Encrypt) {
//            type_Show = "DES-加密";
//        } else {
//            type_Show = "DES-解密";
//        }
//        //默认字体设置
//        Font default_Show_Font = new Font("Microsoft YaHei", 14.5);
//        //加密类型段
//        Label type_Show_Label = new Label();
//        type_Show_Label.setFont(default_Show_Font);//字体
//        type_Show_Label.setText("加密类型：\t" + type_Show);//文字
//        type_Show_Label.setPrefWidth(short_Width);//宽度
//        type_Show_Label.setPrefHeight(fixed_Height);//高度
//        type_Show_Label.setLayoutX(fixed_X);//x位置
//        type_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//        //密钥段
//        Label key_Show_Label = new Label();
//        key_Show_Label.setFont(default_Show_Font);//字体
//        key_Show_Label.setText("密钥：\t" + key);//文字
//        key_Show_Label.setPrefWidth(short_Width);//宽度
//        key_Show_Label.setPrefHeight(fixed_Height);//高度
//        key_Show_Label.setLayoutX(fixed_X);//x位置
//        key_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//
//        //明文段
//        Label origion_Text_Show_Label = new Label();
//        origion_Text_Show_Label.setFont(default_Show_Font);//字体
//        origion_Text_Show_Label.setText("明文：\t" + origion_Text);//文字
//        origion_Text_Show_Label.setPrefWidth(long_Width);//宽度
//        origion_Text_Show_Label.setPrefHeight(fixed_Height);//高度
//        origion_Text_Show_Label.setLayoutX(fixed_X);//x位置
//        origion_Text_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//        //密文段
//        Label encrypted_Text_Show_Label = new Label();
//        encrypted_Text_Show_Label.setFont(default_Show_Font);//字体
//        encrypted_Text_Show_Label.setText("密文：\t" + encrypted_Text);//文字
//        encrypted_Text_Show_Label.setPrefWidth(long_Width);//宽度
//        encrypted_Text_Show_Label.setPrefHeight(fixed_Height);//高度
//        encrypted_Text_Show_Label.setLayoutX(fixed_X);//x位置
//        encrypted_Text_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//
//        //分隔符
//        Separator separat = new Separator();
//        separat.setPrefWidth(long_Width);
//        separat.setPrefHeight(fixed_Height);
//        separat.setLayoutX(fixed_X);
//        separat.setLayoutY(current_Height);
//        current_Height += fixed_Height;
//
//        Show_DES.getChildren().add(type_Show_Label);
//        Show_DES.getChildren().add(key_Show_Label);
//        Show_DES.getChildren().add(origion_Text_Show_Label);
//        Show_DES.getChildren().add(encrypted_Text_Show_Label);
//        Show_DES.getChildren().add(separat);
//        Show_DES.setPrefHeight(current_Height);
//        Show_DES.setPrefWidth(long_Width);
//        scroll_pane.setContent(Show_DES);
//        Show_Pane = Show_DES;
//    }
//
//    public void RSA_Appendent(Boolean is_Encrypt, String public_key, String private_key, String origion_Text, String encrypted_Text) {
//        Pane Show_RSA = new Pane();
//        //限制最大350
//        time++;
//        if (time <= MaxTime) {
//            Show_RSA.getChildren().add(Show_Pane);
//        } else {
//            Show_Pane = null;
//            Show_Pane = new Pane();
//            time = 0;
//            current_Height = 0d;
//        }
//        String type_Show = "";
//        if (is_Encrypt) {
//            type_Show = "RSA-加密";
//        } else {
//            type_Show = "RSA-解密";
//        }
//        //默认字体设置
//        Font default_Show_Font = new Font("Microsoft YaHei", 14.5);
//        //加密类型段
//        Label type_Show_Label = new Label();
//        type_Show_Label.setFont(default_Show_Font);//字体
//        type_Show_Label.setText("加密类型：\t" + type_Show);//文字
//        type_Show_Label.setPrefWidth(short_Width);//宽度
//        type_Show_Label.setPrefHeight(fixed_Height);//高度
//        type_Show_Label.setLayoutX(fixed_X);//x位置
//        type_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//        //密钥段
//        Label pkey_Show_Label = new Label();
//        pkey_Show_Label.setFont(default_Show_Font);//字体
//        pkey_Show_Label.setText("密钥：\t" + public_key);//文字
//        pkey_Show_Label.setPrefWidth(long_Width);//宽度
//        pkey_Show_Label.setPrefHeight(fixed_Height);//高度
//        pkey_Show_Label.setLayoutX(fixed_X);//x位置
//        pkey_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//
//        Label skey_Show_Label = new Label();
//        skey_Show_Label.setFont(default_Show_Font);//字体
//        skey_Show_Label.setText("密钥：\t" + private_key);//文字
//        skey_Show_Label.setPrefWidth(long_Width);//宽度
//        skey_Show_Label.setPrefHeight(fixed_Height);//高度
//        skey_Show_Label.setLayoutX(fixed_X);//x位置
//        skey_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//
//        //明文段
//        Label origion_Text_Show_Label = new Label();
//        origion_Text_Show_Label.setFont(default_Show_Font);//字体
//        origion_Text_Show_Label.setText("明文：\t" + origion_Text);//文字
//        origion_Text_Show_Label.setPrefWidth(long_Width);//宽度
//        origion_Text_Show_Label.setPrefHeight(fixed_Height);//高度
//        origion_Text_Show_Label.setLayoutX(fixed_X);//x位置
//        origion_Text_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//        //密文段
//        Label encrypted_Text_Show_Label = new Label();
//        encrypted_Text_Show_Label.setFont(default_Show_Font);//字体
//        encrypted_Text_Show_Label.setText("密文：\t" + encrypted_Text);//文字
//        encrypted_Text_Show_Label.setPrefWidth(long_Width);//宽度
//        encrypted_Text_Show_Label.setPrefHeight(fixed_Height);//高度
//        encrypted_Text_Show_Label.setLayoutX(fixed_X);//x位置
//        encrypted_Text_Show_Label.setLayoutY(current_Height);//y位置
//        current_Height += fixed_Height;//y位置增加
//
//        //分隔符
//        Separator separat = new Separator();
//        separat.setPrefWidth(long_Width);
//        separat.setPrefHeight(fixed_Height);
//        separat.setLayoutX(fixed_X);
//        separat.setLayoutY(current_Height);
//        current_Height += fixed_Height;
//
//
//        Show_RSA.getChildren().add(type_Show_Label);
//        Show_RSA.getChildren().add(pkey_Show_Label);
//        Show_RSA.getChildren().add(skey_Show_Label);
//        Show_RSA.getChildren().add(origion_Text_Show_Label);
//        Show_RSA.getChildren().add(encrypted_Text_Show_Label);
//        Show_RSA.getChildren().add(separat);
//        Show_RSA.setPrefHeight(current_Height);
//        Show_RSA.setPrefWidth(long_Width);
//        scroll_pane.setContent(Show_RSA);
//        Show_Pane = Show_RSA;
//
//    }
//


//
//    //新建线程
//    public void Show_Thread_Run() {
//        //Show_Content = new EC_Show_ArrayList();
//
//        for (int i = 1; i <= 300; i++) {
//            if (i % 2 == 0) {
//                final String count = String.valueOf(i);
//                int finalI = i;
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println(count);
//                        show_One_Content(true, false, finalI + "DES KEY", "", "", finalI + "DES ORIGIONAL", finalI + "DES ENCRYPTED");
//                        //DES_Appendent(false, count + "asfduiniuadnfiu", count + "bdasfiuhiuadfiubiusadbifubasiu", count + "dabfiasbiubfasuibdiasub");
//                    }
//                });
//            } else {
//                final String count = String.valueOf(i);
//                int finalI = i;
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println(count);
//                        show_One_Content(false, false, "", finalI + "RSA P KEY", finalI + "RSA S KEY", finalI + "RSA ORIGIONAL", finalI + "RSA ENCRYPTED");
//
//                        //RSA_Appendent(false, count + "anidfuianfdiunasiufn", count + "asfduiniuadnfiu", count + "bdasfiuhiuadfiubiusadbifubasiu", count + "dabfiasbiubfasuibdiasub");
//                    }
//                });
//            }
//        }
//    }
//}
