package App.Controller;

import App.ClientApp;
import App.Starter;
import DES.DES_des;
import RSA.RSA;
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
import java.util.Date;
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


    private BigInteger KDC_d;
    private BigInteger KDC_n;

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

    private void show_Info_Alerter(String title, String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.show();
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
     * @param user_ID     账号
     * @param user_PassWD 密码
     * @return 登录状态
     */
    private int login(String user_ID, String user_PassWD) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;

        try {
            // 和服务器创建连接
            socket = new Socket(ClientApp.AS_IP, ClientApp.AS_Port);
            logger.debug("尝试连接服务器");

            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码

            String RSA_Type = "client-fx";
            String RSA_ID = user_ID;
            String message_CA;
            try {
                message_CA = Message_1_CA_Exchange(RSA_Type, RSA_ID);
            } catch (IOException e) {
                return 2;
            }
            logger.debug("发送证书\t" + message_CA);
            //发送消息
            pw.write(message_CA + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message = br.readLine();
            logger.debug("接收到的服务器消息" + server_Message);
            JSONObject msg = JSON.parseObject(server_Message);//转换为Json对象
            if (msg.getInteger("id") == 2) {//报文是回复的证书
                //进行证书验证
                logger.debug("收到AS的2号报文");
                if (VerifyLicense(server_Message)) {
                    GetSK_KDC();
                    //解密
                    String CA = msg.getString("CA");//获取客户端证书
                    String DecryptedCA = RSA.Decrypt(CA, KDC_d, KDC_n);//证书使用KDC私钥解密
                    DES_RSA_Controller.EC_Show_Appendent(false, false, "", "证书解密,未使用公钥", "d:" + KDC_d + "\tn:" + KDC_n, DecryptedCA, CA);
                    logger.debug("解密结果=" + DecryptedCA);

                    //获取json具体内容
                    JSONObject ca = JSON.parseObject(DecryptedCA);//将解密的结果转换为一个Json对象

                    String Server_ID = ca.getString("user_ID");//获取服务端ID
                    String PKc = ca.getString("PK");//获取服务端公钥

                    logger.debug("服务ID=" + Server_ID);
                    JSONObject pk = JSONObject.parseObject(PKc);
                    String cn = pk.getString("n");
                    String ce = pk.getString("e");

                    //AS服务器公钥
                    BigInteger C_n = new BigInteger(cn);
                    BigInteger C_e = new BigInteger(ce);

                    String message_3_Id_PassWD_String = Message_3_ID_PassWD(user_ID, user_PassWD, C_e, C_n, true);

                    //发送消息
                    pw.write(message_3_Id_PassWD_String + "\n");
                    pw.flush();

                    //接收消息
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String server_Message_0 = br.readLine();
                    logger.debug("接收到的服务器消息" + server_Message_0);
                    JSONObject msg_0 = JSON.parseObject(server_Message_0);//转换为Json对象
                    if (msg_0.getInteger("id") == 0) {//报文是回复的证书
                        if (msg_0.getInteger("status") == 4) {
                            String message_5_Ticket_Tgs_String = message_5_Ticket_tgs(user_ID);
                            //发送消息
                            pw.write(message_5_Ticket_Tgs_String + "\n");
                            pw.flush();

                            //接收消息
                            is = socket.getInputStream();
                            br = new BufferedReader(new InputStreamReader(is));

                            String server_Message_6 = br.readLine();
                            logger.debug("接收到的服务器消息" + server_Message_6);
                            JSONObject msg_6 = JSON.parseObject(server_Message_6);
                            if (msg_6.getInteger("id") == 6) {
                                String msg_6_en_String = msg_6.getString("encryptedTicket");
                                String msg_6_de_String = DES_des.Decrypt_Text(msg_6_en_String, Integer.toString(user_ID.hashCode()));
                                DES_RSA_Controller.EC_Show_Appendent(true, false, Integer.toString(user_ID.hashCode()), "", "", msg_6_de_String, msg_6_en_String);
                                JSONObject msg_6_Json = JSONObject.parseObject(msg_6_de_String);
                                ClientApp.ticket_TGS = msg_6_Json.getString("Ticket_tgs");
                                ClientApp.K_C_TGS = msg_6_Json.getString("Kc_tgs");
                                logger.debug("获取到Ticket-tgs：" + ClientApp.ticket_TGS);

                                ClientApp.ADc = socket.getInetAddress().getHostAddress();
                                int status = get_TicketV(user_ID, ClientApp.up_Load_Server_ID);
                                if (status == 1) {
                                    status = get_TicketV(user_ID, ClientApp.down_Load_Server_ID);
                                }
                                return status;
                            } else {
                                return 7;
                            }
                        } else if (msg_0.getInteger("status") == 2) {
                            return 6;
                        } else if (msg_0.getInteger("status") == 3) {
                            return 6;
                        } else {
                            return 0;
                        }
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }
            } else {
                return 4;
            }

        } catch (Exception e) {
            logger.error("服务端已经断开连接\n");
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (!(br == null)) {
                    br.close();
                }
                if (!(is == null)) {
                    is.close();
                }
                if (!(os == null)) {
                    os.close();
                }
                if (!(pw == null)) {
                    pw.close();
                }
                if (!(socket == null)) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int get_TicketV(String user_ID, String id_V) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;

        try {
            socket = new Socket(ClientApp.TGS_IP, ClientApp.TGS_Port);
            logger.debug("尝试连接服务器");

            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码
            String au_Key = String.valueOf((user_ID + ClientApp.TGS_ID).hashCode());

            JSONObject au_Origin = new JSONObject();
            au_Origin.put("IDc", user_ID);
            au_Origin.put("ADc", ClientApp.ADc);
            au_Origin.put("TS3", new Date());
            String au_Origin_String = au_Origin.toJSONString();
            String au_Encrypt_String = DES_des.Encrypt_Text(au_Origin_String, au_Key);
            DES_RSA_Controller.EC_Show_Appendent(true, true, au_Key, "", "", au_Origin_String, au_Encrypt_String);
            JSONObject json_Message_7 = new JSONObject();
            json_Message_7.put("id", 7);
            json_Message_7.put("IDv", id_V);
            json_Message_7.put("Ticket_TGS", ClientApp.ticket_TGS);
            json_Message_7.put("Authenticator_c", au_Encrypt_String);
            String message_7 = json_Message_7.toJSONString();


            logger.debug("发送报文7\t" + message_7);
            //发送消息
            pw.write(message_7 + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message_8 = br.readLine();
            logger.debug("接收到的服务器消息" + server_Message_8);

            JSONObject msg_8 = JSON.parseObject(server_Message_8);//转换为Json对象
            if (msg_8.getInteger("id") == 8) {
                if (id_V.equals(ClientApp.up_Load_Server_ID)) {
                    String msg_8_en_String = msg_8.getString("TGS_C");
                    String msg_8_de_String = DES_des.Decrypt_Text(msg_8_en_String, ClientApp.K_C_TGS);
                    DES_RSA_Controller.EC_Show_Appendent(true, false, ClientApp.K_C_TGS, "", "", msg_8_de_String, msg_8_en_String);
                    JSONObject msg_8_Json = JSON.parseObject(msg_8_de_String);
                    ClientApp.ticket_UP1 = msg_8_Json.getString("Ticket_V");
                    ClientApp.K_C_UP1 = msg_8_Json.getString("Kc_v");
                } else {
                    String msg_8_en_String = msg_8.getString("TGS_C");
                    String msg_8_de_String = DES_des.Decrypt_Text(msg_8_en_String, ClientApp.K_C_TGS);
                    DES_RSA_Controller.EC_Show_Appendent(true, false, ClientApp.K_C_TGS, "", "", msg_8_de_String, msg_8_en_String);
                    JSONObject msg_8_Json = JSON.parseObject(msg_8_de_String);
                    ClientApp.ticket_DOWN1 = msg_8_Json.getString("Ticket_V");
                    ClientApp.K_C_DOWN1 = msg_8_Json.getString("Kc_v");
                }
                return 1;//成功
            } else {
                return 11;//未知错误，TGS会话中遇到未知错误
            }
        } catch (Exception e) {
            logger.error("TGS 服务端已经断开连接\n");
            e.printStackTrace();
            return 10;//TGS异常断开连接
        } finally {
            try {
                if (!(br == null)) {
                    br.close();
                }
                if (!(is == null)) {
                    is.close();
                }
                if (!(os == null)) {
                    os.close();
                }
                if (!(pw == null)) {
                    pw.close();
                }
                if (!(socket == null)) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String message_5_Ticket_tgs(String User_ID) {
        final int id = 5;
        //报文
        JSONObject json_Message_5 = new JSONObject();
        json_Message_5.put("id", id);
        json_Message_5.put("user_ID", User_ID);
        json_Message_5.put("IDtgs", ClientApp.TGS_ID);
        Date TS = new Date();
        json_Message_5.put("TS1", TS);
        return json_Message_5.toJSONString();
    }

    /**
     * 注册时的网络通信
     *
     * @param user_ID     zhanghao
     * @param user_PassWD 密码
     * @return 注册状态
     */
    private int register(String user_ID, String user_PassWD) throws IOException {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            // 和服务器创建连接
            socket = new Socket(ClientApp.AS_IP, ClientApp.AS_Port);
            logger.debug("尝试连接服务器");

            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码

            String RSA_Type = "client-fx";
            String RSA_ID = user_ID;
            String message_CA;
            try {
                message_CA = Message_1_CA_Exchange(RSA_Type, RSA_ID);
            } catch (IOException e) {
                return 2;
            }
            logger.debug("发送证书\t" + message_CA);
            //发送消息
            pw.write(message_CA + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message = br.readLine();
            logger.debug("接收到的服务器消息" + server_Message);
            JSONObject msg = JSON.parseObject(server_Message);//转换为Json对象
            if (msg.getInteger("id") == 2) {//报文是回复的证书
                //进行证书验证
                logger.debug("收到AS的2号报文");
                if (VerifyLicense(server_Message)) {
                    GetSK_KDC();
                    //解密
                    String CA = msg.getString("CA");//获取客户端证书
                    String DecryptedCA = RSA.Decrypt(CA, KDC_d, KDC_n);//证书使用KDC私钥解密
                    DES_RSA_Controller.EC_Show_Appendent(false, false, "", "证书解密,未使用公钥", "d:" + KDC_d + "\tn:" + KDC_n, DecryptedCA, CA);
                    logger.debug("解密结果=" + DecryptedCA);


                    //获取json具体内容
                    JSONObject ca = JSON.parseObject(DecryptedCA);//将解密的结果转换为一个Json对象

                    String Server_ID = ca.getString("user_ID");//获取服务端ID
                    String PKc = ca.getString("PK");//获取服务端公钥

                    logger.debug("服务ID=" + Server_ID);
                    JSONObject pk = JSONObject.parseObject(PKc);
                    String cn = pk.getString("n");
                    String ce = pk.getString("e");

                    //AS服务器公钥
                    BigInteger C_n = new BigInteger(cn);
                    BigInteger C_e = new BigInteger(ce);

                    String message_3_Id_passWD_String = Message_3_ID_PassWD(user_ID, user_PassWD, C_e, C_n, false);

                    //发送消息
                    pw.write(message_3_Id_passWD_String + "\n");
                    pw.flush();

                    //接收消息
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String server_Message_0 = br.readLine();
                    logger.debug("接收到的服务器消息3" + server_Message_0);
                    JSONObject msg_0 = JSON.parseObject(server_Message_0);//转换为Json对象
                    if (msg_0.getInteger("id") == 0) {//报文是回复的证书
                        if (msg_0.getInteger("status") == 0) {
                            return 1;
                        } else if (msg_0.getInteger("status") == 1) {
                            return 7;
                        } else if (msg_0.getInteger("status") == 17) {
                            return 6;
                        } else {
                            return 0;
                        }
                    } else {
                        return 5;
                    }
                } else {
                    return 3;
                }
            } else {
                return 4;
            }

        } catch (Exception e) {
            logger.error("服务端已经断开连接\n");
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (!(br == null)) {
                    br.close();
                }
                if (!(is == null)) {
                    is.close();
                }
                if (!(os == null)) {
                    os.close();
                }
                if (!(pw == null)) {
                    pw.close();
                }
                if (!(socket == null)) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetSK_KDC() throws Exception {//获取KDC私钥
        logger.debug("获取KDC私钥");
        File f = new File("client-fx/target/KDC_Key.txt");
        FileInputStream fip = new FileInputStream(f);
        // 构建FileInputStream对象
        InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
        // 构建InputStreamReader对象,编码与写入相同
        StringBuffer sb = new StringBuffer();
        while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成char加到StringBuffer对象中
        }
        String str = sb.toString();
        logger.debug("str=" + str);
        JSONObject KDC_obj = JSON.parseObject(str);
        String sk = KDC_obj.getString("SK");
        JSONObject SK_obj = JSONObject.parseObject(sk);
        KDC_n = new BigInteger(SK_obj.getString("n"));//获取n
        KDC_d = new BigInteger(SK_obj.getString("d"));//获取d

        logger.debug("n" + KDC_n);
        logger.debug("d" + KDC_d);

        reader.close();// 关闭读取流
        fip.close();// 关闭输入流,释放系统资源
    }

    public boolean VerifyLicense(String message) {//验证证书函数
        logger.debug("验证服务端证书（1号报文）");
        JSONObject msg = JSON.parseObject(message);//转换为Json对象

        String CA = msg.getString("CA");//获取服务端证书
        String CAHash = msg.getString("CAHash");//获取服务端证书Hash值
        logger.debug("CA=" + CA);
        logger.debug("CAHash=" + CAHash);

        if (!CAHash.equals(new String(Integer.toString(CA.hashCode())))) {//验证证书内容是否正确
            logger.error("证书内容不正确");
            return false;
        } else {
            logger.debug("证书内容正确");
            return true;
        }
    }

    private String Message_1_CA_Exchange(String RSA_Type, String RSA_ID) throws IOException {
        final int id = 1;
        RSA.GenerateCA(RSA_Type, RSA_ID);
        //发送证书
        File f = new File(RSA_Type + "/target/" + RSA_ID + "_CA.txt");
        if (!f.exists()) {
            logger.error("RSA错误！");
            IOException e = new IOException("RSA异常");
            throw e;
        }
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
        StringBuffer sb = new StringBuffer();
        while (reader.ready()) {
            sb.append((char) reader.read());
        }
        String str = sb.toString();
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("CA", str);
        obj.put("CAHash", Integer.toString(str.hashCode()));
        return obj.toJSONString();
    }

    private String Message_3_ID_PassWD(String user_ID, String user_PassWD, BigInteger e, BigInteger n, Boolean is_login) {
        final int id;
        String data_send_head;
        if (is_login) {
            id = 4;
            data_send_head = "loginData";
        } else {
            id = 3;
            data_send_head = "registerData";
        }

        JSONObject Json_ID_PASSWD = new JSONObject();
        Json_ID_PASSWD.put("user_ID", user_ID);
        Json_ID_PASSWD.put("password", user_PassWD);
        String origin_ID_PASSWD = Json_ID_PASSWD.toJSONString();
        String encrypt_ID_PASSWD = RSA.Encrypt(origin_ID_PASSWD, e, n);
        DES_RSA_Controller.EC_Show_Appendent(false, true, "", "e:" + e + "\tn:" + n, "账号密码加密,未使用私钥", origin_ID_PASSWD, encrypt_ID_PASSWD);

        //报文
        JSONObject json_Message_3 = new JSONObject();
        json_Message_3.put("id", id);
        json_Message_3.put(data_send_head, encrypt_ID_PASSWD);
        return json_Message_3.toJSONString();
    }


    @FXML
    private void do_Login() throws IOException {
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
                        show_Error_Alerter("登录状态", "服务端错误", "服务端异常断开！请重试！");
                        break;
                    case 1:
                        ClientApp.User_ID = ID;
                        logger.debug("与UP1的通信密钥：" + ClientApp.K_C_UP1);
                        logger.debug("与DOWN1的通信密钥：" + ClientApp.K_C_DOWN1);
                        Starter.setRoot("User", "瓜娃子云盘", 1280, 800, 980, 600);//先进入再弹窗阻塞
                        show_Info_Alerter("登录状态", "登录成功", "正在进入云盘");
                        break;
                    case 2:
                        show_Error_Alerter("登录状态", "RSA加密IO错误", "请重试");
                        break;
                    case 3:
                        show_Error_Alerter("登录状态", "认证服务端错误", "请重试");
                        break;
                    case 4:
                        show_Error_Alerter("登录状态", "服务端连接失败", "请重试");
                        break;
                    case 5:
                        show_Error_Alerter("登录状态", "服务端出错", "请重试");
                        break;
                    case 6:
                        show_Error_Alerter("登录状态", "登录出错，用户名或密码错误", "请确认用户名或者密码正确后重试");
                        break;
                    case 7:
                        show_Error_Alerter("登录状态", "登录失败", "未接收到TGS票据");
                        break;
                    case 10:
                        show_Error_Alerter("登录状态", "登录失败", "TGS异常断开连接");
                        break;
                    case 11:
                        show_Error_Alerter("登录状态", "登录失败", "未知错误，TGS会话中遇到未知错误");
                        break;
                    default:
                        show_Error_Alerter("登录状态", "其他错误", "其他未知错误，请尝试重启应用程序");
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
                    logger.debug("开始注册：\tID： " + ID + "\tPassWD: " + PassWD);

                    //应该用新的线程去做
                    int register_Result = register(ID, PassWD);

                    switch (register_Result) {
                        case 0:
                            show_Error_Alerter("注册状态", "服务端错误", "服务端异常断开！请重试！");
                            break;
                        case 1:
                            show_Info_Alerter("注册状态", "注册成功", "请使用账号密码登录！");
                            break;
                        case 2:
                            show_Error_Alerter("注册状态", "RSA加密IO错误", "请重试");
                            break;
                        case 3:
                            show_Error_Alerter("注册状态", "认证服务端错误", "请重试");
                            break;
                        case 4:
                            show_Error_Alerter("注册状态", "服务端连接失败", "请重试");
                            break;
                        case 5:
                            show_Error_Alerter("注册状态", "服务端出错", "请重试");
                            break;

                        case 6:
                            show_Error_Alerter("注册状态", "注册出错，用户名已经存在", "请尝试使用其他用户名注册");
                            break;
                        case 7:
                            show_Error_Alerter("注册状态", "注册失败", "服务端表示注册错误，原因未知");
                            break;
                        default:
                            show_Error_Alerter("注册状态", "其他错误", "其他未知错误，请尝试重启应用程序");
                    }

                }
            }
        }
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
