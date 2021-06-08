package App.Controller;

import App.Starter;
import DES.DES_des;
import RSA.GenerateKey;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class Login_Controller implements Initializable {
    @FXML
    private Button login_Pane_Button;
    @FXML
    private Button register_Pane_Button;
    @FXML
    private Label logo_Label;
    @FXML
    private Pane container_Pane;
    @FXML
    private GridPane login_Grid_Pane;
    @FXML
    private GridPane register_Grid_Pane;
    @FXML
    private AnchorPane root_Layout;
    @FXML
    private Button test_Button;
    @FXML
    private Button login_Button;
    @FXML
    private TextField login_User_Id_TextField;
    @FXML
    private PasswordField login_User_PassWD_Field;
    @FXML
    private Button register_Button;
    @FXML
    private TextField register_User_Id_TextField;
    @FXML
    private PasswordField register_User_PassWD_Field;
    @FXML
    private PasswordField register_User_PassWD_Confirm_Field;

    private static final Logger logger = Logger.getLogger(Starter.class);

    //style
    private final String style_Login_Pane_Button_Choosed = "-fx-text-fill: rgba(0, 0, 0,.75);-fx-background-color: #ffffff;-fx-background-radius: 10px 0px 0px 0px;-fx-border-width: 0;";
    private final String style_Login_Pane_Button_NotChoosed = "-fx-text-fill: rgb(241,241,241);-fx-background-color: #787878;-fx-background-radius: 10px 0px 0px 0px;-fx-border-width: 0;";
    private final String style_Register_Pane_Button_Choosed = "-fx-text-fill: rgba(0, 0, 0,.75);-fx-background-color: #ffffff;-fx-background-radius: 0px 10px 0px 0px;-fx-border-width: 0;";
    private final String style_Login_Pane_Button_Entered = "-fx-text-fill: rgba(49,49,49,0.8);-fx-background-color: #d2d2d2;-fx-background-radius: 10px 0px 0px 0px ;-fx-border-width: 0;";
    private final String style_Register_Pane_Button_NotChoosed = "-fx-text-fill: rgb(241,241,241);-fx-background-color: #787878;-fx-background-radius: 0px 10px 0px 0px ;-fx-border-width: 0;";
    private final String style_Register_Pane_Button_Entered = "-fx-text-fill: rgba(49,49,49,0.8);-fx-background-color: #d2d2d2;-fx-background-radius: 0px 10px 0px 0px ;-fx-border-width: 0;";
    private Boolean login_Pane_Button_Activated = false;
    private Boolean register_Pane_Button_Activated = false;

    /**
     * 警告框
     *
     * @param title       警告标题
     * @param headText    警告内容
     * @param contentText 警告详细内容
     */
    private void show_Error_Alerter(String title, String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * 输入检测
     *
     * @param text 输入的文字
     * @param type 种类： 1：账号；2：密码；3: 注册时的密码
     * @return 是否通过
     */
    private Boolean insert_Check(String text, int type) {
        int text_length = text.length();
        if (type == 1) {
            if (text_length > 20) {
                show_Error_Alerter("输入错误", "检测到输入错误", "输入的账号过长，请重新输入！");
                return false;
            } else {
                if (!text.matches("^[a-z0-9A-Z]+$")) {
                    show_Error_Alerter("输入错误", "检测到输入错误", "输入的账号格式不正确，请检查后重新输入！");
                    return false;
                }
            }
        } else if (type == 2) {
            if (text_length > 20) {
                show_Error_Alerter("输入错误", "检测到输入错误", "输入的密码过长，请重新输入！");
                return false;
            } else {
                if (!text.matches("^[a-z0-9A-Z._+!@#$%\\^\\[\\]<>?]+$")) {
                    show_Error_Alerter("输入错误", "检测到输入错误", "输入的密码中含有非法字符！\n密码仅支持数字\t0-9\n英文字母\ta-z(含大小写)\n符号\t. _ + ! @ # $ % ^ [ ] < > ?\n\n请检查后重新输入！");
                    return false;
                }
            }
        } else if (type == 3) {
            if (!(6 <= text_length && text_length <= 20)) {
                show_Error_Alerter("输入错误", "检测到输入错误", "密码长度不足，请输入6-20位密码！\n密码仅支持数字\t0-9\n英文字母\ta-z(含大小写)\n符号\t. _ + ! @ # $ % ^ [ ] < > ?\n\n请检查后重新输入！");
                return false;
            } else {
                if (!text.matches("^[a-z0-9A-Z._+!@#$%\\^\\[\\]<>?]+$")) {
                    show_Error_Alerter("输入错误", "检测到输入错误", "输入的密码中含有非法字符！\n密码仅支持数字\t0-9\n英文字母\ta-z(含大小写)\n符号\t. _ + ! @ # $ % ^ [ ] < > ?\n\n请检查后重新输入！");
                    return false;
                }
            }
        } else {
            logger.error("输入检测调用异常！传入非 1 / 2");
            show_Error_Alerter("程序异常", "输入检测调用异常", "输入检测调用异常！请尝试重试！");
            return false;
        }
        return true;
    }

    /**
     * 登录时的网络通信
     *
     * @param ID     账号
     * @param PassWD 密码
     * @return 登录状态
     */
    private int login(String ID, String PassWD) {

        return 0;
    }

    /**
     * 注册时的网络通信
     *
     * @param ID     zhanghao
     * @param PassWD 密码
     * @return 注册状态
     */
    private int register(String ID, String PassWD) throws IOException {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // 和服务器创建连接
            socket = new Socket("localhost", 8888);
            logger.debug("尝试连接服务器");

            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码


            RSA.RSA.GenerateCA("client-fx", "client1_CA");
            //发送证书

            File f = new File("client-fx/target/client1_CA.txt");
            if (!f.exists()) {
                logger.error("RSA错误！");
                return 2;
            }
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            StringBuffer sb = new StringBuffer();
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
            String str = sb.toString();
            JSONObject obj = new JSONObject();
            obj.put("id", 1);
            obj.put("CA", str);
            obj.put("CAHash", Integer.toString(str.hashCode()));
            String message_CA = obj.toJSONString();
            logger.debug("发送证书\t" + message_CA);
            //发送消息
            pw.write(message_CA + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message = br.readLine();
            logger.debug("接收到的服务器消息" + server_Message);


        } catch (Exception e) {
            logger.error("服务端已经断开连接\n");
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
                os.close();
                pw.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return 0;
    }

    @FXML
    private void do_Login() {
        String ID = login_User_Id_TextField.getText();
        String PassWD = login_User_PassWD_Field.getText();
        logger.debug("输入ID： " + ID + "\t\tPassWD: " + PassWD);
        if (ID.equals("") || (PassWD.equals(""))) {//输入状态检测
            //是否输入账号密码
            String alert_Content = "";
            if (ID.equals("")) {
                alert_Content += "用户名 ";
            }
            if (PassWD.equals("")) {
                alert_Content += "密码 ";
            }
            show_Error_Alerter("输入错误", "检测到有未输入的内容", "请检查并且输入以下内容：" + alert_Content);
        } else {
            //输入合法性检测
            if (insert_Check(ID, 1) && insert_Check(PassWD, 2)) {//输入检测通过
                logger.debug("开始登录：\tID： " + ID + "\tPassWD: " + PassWD);

                //应当用新线程做
                int login_Result = login(ID, PassWD);

                switch (login_Result) {
                    case 0:
                        show_Error_Alerter("错误", "用户名或者密码错误", "请重新输入用户名或者密码!");
                        break;
                    case 1:

                }

            }
        }
    }


    @FXML
    private void do_Register() throws IOException {
        String ID = register_User_Id_TextField.getText();
        String PassWD = register_User_PassWD_Field.getText();
        String PassWD_Confirm = register_User_PassWD_Confirm_Field.getText();
        logger.debug("输入ID： " + ID + "\t\tPassWD: " + PassWD);
        if (ID.equals("") || (PassWD.equals("")) || PassWD_Confirm.equals("")) {//输入状态检测
            String alert_Content = "";
            if (ID.equals("")) {
                alert_Content += "用户名 ";
            }
            if (PassWD.equals("")) {
                alert_Content += "密码 ";
            }
            if (PassWD_Confirm.equals("")) {
                alert_Content += "确认密码 ";
            }
            show_Error_Alerter("输入错误", "检测到有未输入的内容", "请检查并且输入以下内容：" + alert_Content);
        } else {
            if (!PassWD.equals(PassWD_Confirm)) {
                show_Error_Alerter("密码错误", "两次输入的密码不一致", "请检查并尝试在密码栏和确认密码栏输入正确的内容！");
            } else {
                //输入合法性检测
                if (insert_Check(ID, 1) && insert_Check(PassWD, 3) && insert_Check(PassWD_Confirm, 3)) {//输入检测通过
                    logger.debug("开始登录：\tID： " + ID + "\tPassWD: " + PassWD);

                    //应该用新的线程去做
                    int register_Result = register(ID, PassWD);

                    switch (register_Result) {
                        case 0:
                            show_Error_Alerter("错误", "", "");
                            break;
                        case 1:

                        case 2:
                            show_Error_Alerter("错误", "加密IO错误", "请重试");
                    }

                }
            }
        }
    }

    @FXML
    private void test_button_pressed() throws IOException {
        Starter.setRoot("User", "瓜娃子云盘", 1280, 800, 980, 600);
    }

    @FXML
    private void login_Pane_Button_Choosed() {
        login_Pane_Button_Init();
    }

    private void login_Pane_Button_Init() {
        //设置登录按钮的样式参数
        login_Pane_Button.setStyle(style_Login_Pane_Button_Choosed);
        login_Pane_Button_Activated = true;

        //注册按钮样式参数
        register_Pane_Button.setStyle(style_Register_Pane_Button_NotChoosed);
        register_Pane_Button_Activated = false;

        //登录面板显示
        login_Grid_Pane.setVisible(true);
        register_Grid_Pane.setVisible(false);
    }

    @FXML
    private void register_Pane_Button_Choosed() {
        register_Pane_Button_Init();
    }

    private void register_Pane_Button_Init() {
        //设置登录按钮的样式参数
        login_Pane_Button.setStyle(style_Login_Pane_Button_NotChoosed);
        login_Pane_Button_Activated = false;

        //注册按钮样式参数
        register_Pane_Button.setStyle(style_Register_Pane_Button_Choosed);
        register_Pane_Button_Activated = true;

        //登录面板隐藏
        login_Grid_Pane.setVisible(false);
        register_Grid_Pane.setVisible(true);
    }

    //未激活的时候才会改变状态
    @FXML
    private void login_Pane_Button_Mouse_Entered() {
        if (!login_Pane_Button_Activated) {
            login_Pane_Button.setStyle(style_Login_Pane_Button_Entered);
        }
    }

    //未激活的时候才会改变状态
    @FXML
    private void login_Pane_Button_Mouse_Exited() {
        if (!login_Pane_Button_Activated) {
            login_Pane_Button.setStyle(style_Login_Pane_Button_NotChoosed);
        }
    }

    //未激活的时候才会改变状态
    @FXML
    private void register_Pane_Button_Mouse_Entered() {
        if (!register_Pane_Button_Activated) {
            register_Pane_Button.setStyle(style_Register_Pane_Button_Entered);
        }
    }

    //未激活的时候才会改变状态
    @FXML
    private void register_Pane_Button_Mouse_Exited() {
        if (!register_Pane_Button_Activated) {
            register_Pane_Button.setStyle(style_Register_Pane_Button_NotChoosed);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //加载图片
        URL image_Url = Starter.class.getResource("img/logo.png");
        ImageView logo_Image = new ImageView(image_Url.toExternalForm());
        logo_Image.setFitHeight(48);
        logo_Image.setFitWidth(48);
        logo_Label.setGraphic(logo_Image);

        //设置边框的样式参数
        container_Pane.setStyle("-fx-text-fill: rgb(0, 0, 0, .0);" +
                "-fx-background-color: rgb(255,255,255);" +
                "-fx-background-radius: 10;" +
                "-fx-border-width: 0;");

        login_Pane_Button_Init();//初始化面板样式为等待登录
    }
}
