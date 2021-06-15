package App.Controller;

import App.Main;
import App.Starter;
import DES.DES_des;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class User_Controller implements Initializable {

    public static User_Show_All_File_ArrayList all_files_List = new User_Show_All_File_ArrayList();
    public static User_Show_Download_Task_List download_List = new User_Show_Download_Task_List();
    public static User_Show_Upload_Task_List upload_List = new User_Show_Upload_Task_List();
    private static final Logger logger = Logger.getLogger(User_Controller.class);
    private Boolean download_Worker_is_started = false;
    private Boolean upload_Worker_is_started = false;

    @FXML
    private Label logo_Label;
    @FXML
    private Label file_IMG_Label;
    @FXML
    private Label uploading_IMG_Label;
    @FXML
    private Label downloading_IMG_Label;
    @FXML
    private Label completed_IMG_Label;
    @FXML
    private Label user_IMG_Label;

    @FXML
    private Button file_Button;
    @FXML
    private Button downloading_Button;
    @FXML
    private Button uploading_Button;
    @FXML
    private Button completed_Button;

    @FXML
    private Label title_Label;
    @FXML
    private Label progress_Label;
    @FXML
    private Label upload_IMG_Label;
    //    @FXML
//    private Label search_IMG_Label;
    @FXML
    private Label update_IMG_Label;
    @FXML
    private Label download_IMG_Label;
    @FXML
    private Label delete_File_IMG_Label;
    @FXML
    private Label delete_Download_Task_IMG_Label;
    @FXML
    private Label delete_Upload_Task_IMG_Label;
    @FXML
    private ScrollPane show_File_ScrollPane;
    @FXML
    private Label restart_Download_Task_IMG_Label;
    @FXML
    private Label restart_Upload_Task_IMG_Label;

    //    VBox show_All_File_VBox = new VBox();
    static AnchorPane show_All_File_AnchorPane = new AnchorPane();
    static AnchorPane show_Download_Task_AnchorPane = new AnchorPane();
    static AnchorPane show_Upload_Task_AnchorPane = new AnchorPane();
    static AnchorPane show_finished_Task_AnchorPane = new AnchorPane();

    final int small_Img_Size = 26;

    private final String style_Left_Button_Radius = "7";
    private final String style_Left_Button_Entered = "-fx-border-width:0;-fx-background-color: #E8E8EB;-fx-background-radius:" + style_Left_Button_Radius + ";-fx-border-radius:" + style_Left_Button_Radius + ";";
    private final String style_Left_Button_Choosed = "-fx-border-width:0;-fx-background-color: #D7D7D9;-fx-background-radius:" + style_Left_Button_Radius + ";-fx-border-radius:" + style_Left_Button_Radius + ";";
    private final String style_Left_Button_Origin = "-fx-border-width:0;-fx-background-color: transparent;-fx-background-radius:" + style_Left_Button_Radius + ";-fx-border-radius:" + style_Left_Button_Radius + ";";

    private Boolean file_Button_Activated = false;
    private Boolean downloading_Button_Activated = false;
    private Boolean uploading_Button_Activated = false;
    private Boolean completed_Button_Activated = false;
    private static double scroll_Pane_Anchor_Width = 965d;


    @FXML
    private void file_Button_Clicked() throws IOException {
        file_Button_Pane_Init();
    }

    @FXML
    private void file_Button_Enter() {
        if (!file_Button_Activated) {
            file_Button.setStyle(style_Left_Button_Entered);
        }
    }

    @FXML
    private void file_Button_Exit() {
        if (!file_Button_Activated) {
            file_Button.setStyle(style_Left_Button_Origin);
        }
    }

    //弹框
    private void show_Info_Alerter(String title, String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.show();
    }

    private void show_Error_Alerter(String title, String headText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    //文件列表面板更新
    private void file_Button_Pane_Init() throws IOException {
        //按钮状态设置
        file_Button_Activated = true;
        downloading_Button_Activated = false;
        uploading_Button_Activated = false;
        completed_Button_Activated = false;

        //按钮样式更新
        file_Button.setStyle(style_Left_Button_Choosed);
        downloading_Button.setStyle(style_Left_Button_Origin);
        uploading_Button.setStyle(style_Left_Button_Origin);
        completed_Button.setStyle(style_Left_Button_Origin);

        //窗口更新
        title_Label.setText("文件");
        update_IMG_Label.setVisible(true);
//        search_IMG_Label.setVisible(true);
        upload_IMG_Label.setVisible(true);
        download_IMG_Label.setVisible(true);
        delete_File_IMG_Label.setVisible(true);
        progress_Label.setVisible(false);
        delete_Download_Task_IMG_Label.setVisible(false);
        delete_Upload_Task_IMG_Label.setVisible(false);

        restart_Download_Task_IMG_Label.setVisible(false);
        restart_Upload_Task_IMG_Label.setVisible(false);
        file_List_Update_And_Show();
    }

    //更新并且显示文件列表中的文件
    private void file_List_Update_And_Show() throws IOException {
        int status = update_Files_List();
        switch (status) {
            case 0:
                show_Info_Alerter("文件列表更新状态", "文件列表更新失败", "服务端异常断开连接，请稍后重试");
                break;
            case 1:
                logger.debug("文件列表更新成功！");
                if (file_Button_Activated) {//用户在文件列表上才更新UI
                    Platform.runLater(new Runnable() {//异步更新UI
                        @Override
                        public void run() {
                            show_File_ScrollPane.setContent(null);
                            show_File_ScrollPane.setContent(show_All_File_AnchorPane);
                        }
                    });
                }
                break;
            case 7:
                show_Info_Alerter("文件列表更新状态", "文件列表更新失败", "登录状态异常，请重新登录！");
                logger.debug("票据验证失败，定位到login重新登录");

                if (file_Button_Activated) {
                    Platform.runLater(new Runnable() {//异步更新UI
                        @Override
                        public void run() {
                            try {
                                Starter.setRoot("Login", "登录 | 瓜娃子云盘", 420, 512, 420, 512, 420, 512);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                break;
            case 10:
                show_Info_Alerter("文件列表更新状态", "文件列表更新失败", "服务端异常，请稍后重试");
                break;
            case 15:
                show_Info_Alerter("文件列表更新状态", "文件列表更新失败", "系统错误，请稍后重试");
                break;
            case 20:
                logger.debug("没有文件");
                Platform.runLater(new Runnable() {//异步更新UI
                    @Override
                    public void run() {
                        show_All_File_AnchorPane = null;
                        show_All_File_AnchorPane = new AnchorPane();
                        show_File_ScrollPane.setContent(null);
                        show_File_ScrollPane.setContent(show_All_File_AnchorPane);
                    }
                });
                break;
            case 100:
                show_Info_Alerter("文件列表更新状态", "文件列表更新失败", "未知错误");
                break;
        }
    }

    //显示文件列表，将子面板添加到锚面板中
    private void view_Files_List() {
        show_All_File_AnchorPane = null;
        show_All_File_AnchorPane = new AnchorPane();
        show_All_File_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);

        //show files in file list
        int t = all_files_List.get_Total();
        double height = 0d;
        for (int i = 0; i < t; i++) {
            show_All_File_AnchorPane.getChildren().add(all_files_List.child_Pane.get(i).pane);
            show_All_File_AnchorPane.setTopAnchor(all_files_List.child_Pane.get(i).pane, height);
            show_All_File_AnchorPane.setLeftAnchor(all_files_List.child_Pane.get(i).pane, 0d);
            show_All_File_AnchorPane.setRightAnchor(all_files_List.child_Pane.get(i).pane, 0d);
            height += 35d;
        }

    }

    //动态改变展示锚面板列表的宽度
    public static void stage_Changed(double width) {
        scroll_Pane_Anchor_Width = width - 315d;
        show_All_File_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);
        show_Download_Task_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);
        show_Upload_Task_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);
        show_finished_Task_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);
    }

    //更新文件列表
    private int update_Files_List() {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;

        try {
            socket = new Socket(Main.down_Load_Server_IP, Main.down_Load_Server_Port);
            logger.debug("尝试连接服务器");
            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码

            JSONObject message_9_Au_Json = new JSONObject();
            message_9_Au_Json.put("id", 9);
            message_9_Au_Json.put("Ticket_v", Main.ticket_DOWN1);
            Date TS5 = new Date();
            JSONObject au_Origin = new JSONObject();
            au_Origin.put("IDc", Main.User_ID);
            au_Origin.put("ADc", Main.ADc);
            au_Origin.put("TS5", TS5);
            String au_Origin_String = au_Origin.toJSONString();
            String au_Encrypt_String = DES_des.Encrypt_Text(au_Origin_String, Main.K_C_DOWN1);

            message_9_Au_Json.put("Authenticator_c", au_Encrypt_String);

            pw.write(message_9_Au_Json + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message_10 = br.readLine();

            JSONObject msg_10_Json = JSON.parseObject(server_Message_10);
            if (msg_10_Json.getInteger("id") == 10) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TS5);
                calendar.add(calendar.HOUR, 1);
                Date TS5_TEST = calendar.getTime();
                String TS5_TEST_String = DES_des.Encrypt_Text(TS5_TEST.toString(), Main.K_C_DOWN1);
                if (msg_10_Json.getString("ACK").equals(TS5_TEST_String)) {
                    logger.debug("下载端身份验证成功！");

                    logger.debug("开始获取文件列表");
                    JSONObject msg_13_Get_File_List_Json = new JSONObject();
                    msg_13_Get_File_List_Json.put("id", 13);
                    msg_13_Get_File_List_Json.put("IDc", Main.User_ID);
                    String msg_13 = msg_13_Get_File_List_Json.toJSONString();
                    logger.debug("发送报文" + msg_13);
                    pw.write(msg_13 + "\n");
                    pw.flush();

                    //接收消息
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String msg_13_Reply_File_List = br.readLine();
                    JSONObject msg_13_Reply_Json = JSON.parseObject(msg_13_Reply_File_List);
                    if (msg_13_Reply_Json.getInteger("id") == 13) {
                        logger.debug("服务端返回文件列表");
                        String encrypted_File_Name_List_Json = msg_13_Reply_Json.getString("data");
                        String decrypted_File_Name_List_Json = DES_des.Decrypt_Text(encrypted_File_Name_List_Json, Main.K_C_DOWN1);
                        JSONObject msg_13_File_Json = JSON.parseObject(decrypted_File_Name_List_Json);
                        logger.debug("Json文件列表解密尝试" + decrypted_File_Name_List_Json);
                        int file_Num = msg_13_File_Json.getInteger("num");
                        all_files_List = null;
                        all_files_List = new User_Show_All_File_ArrayList();
                        if (file_Num != 0) {
                            //获取文件列表
                            List<String> file_Name = msg_13_File_Json.getJSONArray("filename").toJavaList(String.class);
                            for (int i = 0; i < file_Num; i++) {
                                all_files_List.add_New_File(file_Name.get(i));

                            }
                            view_Files_List();//显示
                            return 1;
                        } else {
                            logger.debug("没有文件！");
                            return 20;
                        }
                    } else if (msg_13_Reply_Json.getInteger("id") == 0) {
                        if (msg_13_Reply_Json.getInteger("status") == 15) {
                            return 15;//系统错误 稍后重试
                        } else {
                            return 100;//未知错误
                        }
                    }
                } else {
                    return 10;//服务端异常
                }
            } else if (msg_10_Json.getInteger("id") == 0) {
                if (msg_10_Json.getInteger("status") == 7) {
                    return 7;//Ticket_Download_Server认证失败，重新登录
                } else {
                    return 100;//未知错误
                }
            } else {
                return 100;//未知错误
            }
            return 100;//未知错误
        } catch (Exception e) {
            logger.error("服务端已经断开连接\n");
            e.printStackTrace();
            return 0;//与服务器异常断开连接
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
                return 100;
            }
        }
    }

    @FXML
    private void downloading_Button_Clicked() {
        downloading_Button_Pane_Init();
    }

    @FXML
    private void downloading_Button_Enter() {
        if (!downloading_Button_Activated) {
            downloading_Button.setStyle(style_Left_Button_Entered);
        }
    }

    @FXML
    private void downloading_Button_Exit() {
        if (!downloading_Button_Activated) {
            downloading_Button.setStyle(style_Left_Button_Origin);
        }
    }

    //下载列表面板更新
    private void downloading_Button_Pane_Init() {
        //按钮状态设置
        file_Button_Activated = false;
        downloading_Button_Activated = true;
        uploading_Button_Activated = false;
        completed_Button_Activated = false;

        //按钮样式更新
        file_Button.setStyle(style_Left_Button_Origin);
        downloading_Button.setStyle(style_Left_Button_Choosed);
        uploading_Button.setStyle(style_Left_Button_Origin);
        completed_Button.setStyle(style_Left_Button_Origin);

        //窗口更新
        title_Label.setText("下载中");
        update_IMG_Label.setVisible(false);
//        search_IMG_Label.setVisible(false);
        upload_IMG_Label.setVisible(false);
        download_IMG_Label.setVisible(false);
        delete_File_IMG_Label.setVisible(false);
        progress_Label.setVisible(true);
        delete_Download_Task_IMG_Label.setVisible(true);
        delete_Upload_Task_IMG_Label.setVisible(false);

        restart_Download_Task_IMG_Label.setVisible(true);
        restart_Upload_Task_IMG_Label.setVisible(false);
        update_and_view_Downloading_Pane();
    }

    //刷新并且更新面板
    private void update_and_view_Downloading_Pane() {
        Platform.runLater(new Runnable() {//异步更新UI
            @Override
            public void run() {
                if (downloading_Button_Activated) {
                    show_File_ScrollPane.setContent(null);
                }
            }
        });
        view_DownLoad_Task_List();
        Platform.runLater(new Runnable() {//异步更新UI
            @Override
            public void run() {
                if (downloading_Button_Activated) {
                    show_File_ScrollPane.setContent(show_Download_Task_AnchorPane);
                }
            }
        });
    }


    //显示下载列表，将子面板添加到锚面板中
    private void view_DownLoad_Task_List() {
        show_Download_Task_AnchorPane = null;
        show_Download_Task_AnchorPane = new AnchorPane();
        show_Download_Task_AnchorPane.setPrefWidth(scroll_Pane_Anchor_Width);

        //添加子面板
        int t = download_List.get_Total();
        double height = 0d;
        for (int i = 0; i < t; i++) {
            if (!download_List.is_NULL.get(i)) {//判断任务是否删除
                if (download_List.child_Pane.get(i).is_Downloading) {//先添加正在下载的
                    int finalI = i;
                    double finalHeight = height;
                    Platform.runLater(new Runnable() {//异步更新UI
                        @Override
                        public void run() {
                            show_Download_Task_AnchorPane.getChildren().add(download_List.child_Pane.get(finalI).pane);
                            show_Download_Task_AnchorPane.setTopAnchor(download_List.child_Pane.get(finalI).pane, finalHeight);
                            show_Download_Task_AnchorPane.setLeftAnchor(download_List.child_Pane.get(finalI).pane, 0d);
                            show_Download_Task_AnchorPane.setRightAnchor(download_List.child_Pane.get(finalI).pane, 0d);
                        }
                    });
                    height += 35d;
                }
            }
        }
        for (int i = 0; i < t; i++) {
            if (!download_List.is_NULL.get(i)) {//判断任务是否删除
                if (!download_List.child_Pane.get(i).is_Downloading) {//添加正在等待的
                    int finalI = i;
                    double finalHeight1 = height;
                    Platform.runLater(new Runnable() {//异步更新UI
                        @Override
                        public void run() {
                            show_Download_Task_AnchorPane.getChildren().add(download_List.child_Pane.get(finalI).pane);
                            show_Download_Task_AnchorPane.setTopAnchor(download_List.child_Pane.get(finalI).pane, finalHeight1);
                            show_Download_Task_AnchorPane.setLeftAnchor(download_List.child_Pane.get(finalI).pane, 0d);
                            show_Download_Task_AnchorPane.setRightAnchor(download_List.child_Pane.get(finalI).pane, 0d);
                        }
                    });
                    height += 35d;
                }
            }
        }
    }

    //删除下载中的任务
    @FXML
    private void delete_Download_Task_Clicked() {
        int t = download_List.get_Total();
        logger.debug("准备删除任务");
        String delete_Task_Name = "";
        String delete_Failed = "";
        for (int i = 0; i < t; i++) {
            if (!download_List.is_NULL.get(i)) {//判断任务是否删除
                if (download_List.child_Pane.get(i).is_Checked) {//选中
                    if (!download_List.child_Pane.get(i).is_Downloading) {//先添加正在等待的
                        delete_Task_Name += download_List.get_File_Name(i) + "\n";
                        logger.debug("删除任务: " + download_List.get_File_Name(i));
                        download_List.delete(i);//从列表中删除
                    } else {
                        delete_Failed += download_List.get_File_Name(i) + "\n";
                        logger.debug("删除任务错误: 任务正在下载中" + download_List.get_File_Name(i));
                    }
                }
            }
        }
        if (delete_Failed.equals("")) {
            show_Info_Alerter("提示", "以下任务已经删除\n", delete_Task_Name);
        } else {
            show_Info_Alerter("提示", "以下任务已经删除\n" + delete_Task_Name, "以下任务正在下载中，无法删除\n" + delete_Failed);
        }

        delete_Task_Name = null;
        delete_Failed = null;
        downloading_Button_Pane_Init();//更新面板取消选中
    }

    //重启下载中的出错的任务
    @FXML
    private void restart_Download_Task_Clicked() {
        int t = download_List.get_Total();
        logger.debug("准备重启任务");
        String restart_Task_Name = "";
        String restart_Failed = "";
        for (int i = 0; i < t; i++) {
            if (!download_List.is_NULL.get(i)) {//判断任务是否删除
                if (download_List.child_Pane.get(i).is_Checked) {//任务是否选中
                    if (download_List.is_In_Error.get(i)) {//判断是否出错
                        restart_Task_Name += download_List.get_File_Name(i) + "\n";
                        logger.debug("重启任务: " + download_List.get_File_Name(i));
                        download_List.show_Restart(i);//设置重启
                    } else {
                        restart_Failed += download_List.get_File_Name(i) + "\n";
                        logger.debug("重启任务错误: 任务未发生错误" + download_List.get_File_Name(i));
                    }
                }
            }
        }

        if (restart_Failed.equals("")) {
            show_Info_Alerter("提示", "以下任务已经重启\n", restart_Task_Name);
        } else {
            show_Info_Alerter("提示", "以下任务已经重启\n" + restart_Task_Name, "以下任务未发生错误，无法重启\n" + restart_Failed);
        }


        restart_Task_Name = null;
        restart_Failed = null;
        downloading_Button_Pane_Init();//更新面板取消选中
    }

    @FXML
    private void uploading_Button_Clicked() {
        uploading_Button_Pane_Init();
    }

    @FXML
    private void uploading_Button_Enter() {
        if (!uploading_Button_Activated) {
            uploading_Button.setStyle(style_Left_Button_Entered);
        }
    }

    @FXML
    private void uploading_Button_Exit() {
        if (!uploading_Button_Activated) {
            uploading_Button.setStyle(style_Left_Button_Origin);
        }
    }

    //上传列表面板更新
    private void uploading_Button_Pane_Init() {
        //按钮状态设置
        file_Button_Activated = false;
        downloading_Button_Activated = false;
        uploading_Button_Activated = true;
        completed_Button_Activated = false;

        //按钮样式更新
        file_Button.setStyle(style_Left_Button_Origin);
        downloading_Button.setStyle(style_Left_Button_Origin);
        uploading_Button.setStyle(style_Left_Button_Choosed);
        completed_Button.setStyle(style_Left_Button_Origin);

        //窗口更新
        title_Label.setText("上传中");
        update_IMG_Label.setVisible(false);
//       search_IMG_Label.setVisible(false);
        upload_IMG_Label.setVisible(false);
        download_IMG_Label.setVisible(false);
        delete_File_IMG_Label.setVisible(false);
        progress_Label.setVisible(true);
        delete_Download_Task_IMG_Label.setVisible(false);
        delete_Upload_Task_IMG_Label.setVisible(true);
        restart_Download_Task_IMG_Label.setVisible(false);
        restart_Upload_Task_IMG_Label.setVisible(true);

    }

    @FXML
    private void completed_Button_Clicked() {
        completed_Button_Pane_Init();
    }

    @FXML
    private void completed_Button_Enter() {
        if (!completed_Button_Activated) {
            completed_Button.setStyle(style_Left_Button_Entered);
        }
    }

    @FXML
    private void completed_Button_Exit() {
        if (!completed_Button_Activated) {
            completed_Button.setStyle(style_Left_Button_Origin);
        }
    }

    //已经完成的列表面板更新
    private void completed_Button_Pane_Init() {
        //按钮状态设置
        file_Button_Activated = false;
        downloading_Button_Activated = false;
        uploading_Button_Activated = false;
        completed_Button_Activated = true;

        //按钮样式更新
        file_Button.setStyle(style_Left_Button_Origin);
        downloading_Button.setStyle(style_Left_Button_Origin);
        uploading_Button.setStyle(style_Left_Button_Origin);
        completed_Button.setStyle(style_Left_Button_Choosed);

        //窗口更新
        title_Label.setText("传输完成");
        update_IMG_Label.setVisible(false);
//        search_IMG_Label.setVisible(false);
        upload_IMG_Label.setVisible(false);
        download_IMG_Label.setVisible(false);
        delete_File_IMG_Label.setVisible(false);
        progress_Label.setVisible(false);
        delete_Download_Task_IMG_Label.setVisible(false);
        delete_Upload_Task_IMG_Label.setVisible(false);

        restart_Download_Task_IMG_Label.setVisible(false);
        restart_Upload_Task_IMG_Label.setVisible(false);
    }

    //选择文件上传
    @FXML
    private void upload_Label_Cliecked() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择需要上传的文件");
        File file = fileChooser.showOpenDialog(Starter.get_Current_Stage());
        upload_List.add_NewFile(file);
        if (!upload_Worker_is_started) {
            Runnable upload_Thread_Task = new Runnable() {
                @Override
                public void run() {
                    try {
                        upload_Worker();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread upload_worker_Thread = new Thread(upload_Thread_Task);
            upload_worker_Thread.setDaemon(true);
            upload_worker_Thread.start();
            upload_Worker_is_started = true;
            all_files_List.uncheck_All();//取消选中
        }
    }


    //上传线程
    private void upload_Worker() throws InterruptedException {

    }


//    //上传线程
//    private void upload_Worker() throws InterruptedException {
//        while (true) {
//            if (upload_List.latestFile() != -1) {
//                Socket socket = null;
//                OutputStream os = null;
//                PrintWriter pw = null;
//                InputStream is = null;
//                BufferedReader br = null;
//
//                try {
//                    socket = new Socket(Main.up_Load_Server_IP, Main.up_Load_Server_Port);
//                    logger.debug("尝试连接服务器");
//                    os = socket.getOutputStream();//字节流(二进制)
//                    pw = new PrintWriter(os);//字符编码
//
//                    JSONObject message_9_Au_Json = new JSONObject();
//                    message_9_Au_Json.put("id", 9);
//                    message_9_Au_Json.put("Ticket_v", Main.ticket_UP1);
//                    Date TS5 = new Date();
//                    JSONObject au_Origin = new JSONObject();
//                    au_Origin.put("IDc", Main.User_ID);
//                    au_Origin.put("ADc", Main.ADc);
//                    au_Origin.put("TS5", TS5);
//                    String au_Origin_String = au_Origin.toJSONString();
//                    String au_Encrypt_String = DES_des.Encrypt_Text(au_Origin_String, Main.K_C_UP1);
//                    message_9_Au_Json.put("Authenticator_c", au_Encrypt_String);
//
//                    pw.write(message_9_Au_Json + "\n");
//                    pw.flush();
//
//                    //接收消息
//                    is = socket.getInputStream();
//                    br = new BufferedReader(new InputStreamReader(is));
//                    String server_Message_10 = br.readLine();
//
//                    JSONObject msg_10_Json = JSON.parseObject(server_Message_10);
//                    if (msg_10_Json.getInteger("id") == 10) {
//                        Calendar calendar = new GregorianCalendar();
//                        calendar.setTime(TS5);
//                        calendar.add(calendar.HOUR, 1);
//                        Date TS5_TEST = calendar.getTime();
//                        String TS5_TEST_String = DES_des.Encrypt_Text(TS5_TEST.toString(), Main.K_C_UP1);
//                        if (msg_10_Json.getString("ACK").equals(TS5_TEST_String)) {
//                            logger.debug("上传端身份验证成功！");
//
//                            //获取最新的一个文件名
//                            int upload_File_Index = upload_List.latestFile();
//                            if (upload_File_Index != -1) {
//                                File file_To_Upload = upload_List.get_File(upload_File_Index);
//                                if (file_To_Upload.exists()) {
//                                    String file_Content = null;
//                                    try {
//                                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file_To_Upload));
//                                        byte[] bytes = in.readAllBytes();//按字节全部读出
//                                        //file_Content = Base64.getEncoder().encodeToString(bytes);//转换为string
//                                        JSONObject msg_11_data = new JSONObject();
//
//                                        msg_11_data.put("filename", file_To_Upload.getName());
//                                        //Key
//                                        String content_Key = file_To_Upload.getName() + Main.User_ID;
//                                        content_Key = Integer.toString(content_Key.hashCode());
//                                        byte[] em = DES_des.Encrypt_Text(bytes, content_Key);
//                                        //sig
//                                        String Hash_Code = String.valueOf(Base64.getEncoder().encodeToString(em).hashCode());
//                                        File rsa_file = new File("client-fx/target/" + Main.User_ID + "_RSA_Key.txt");
//                                        FileInputStream rsa_fip = new FileInputStream(rsa_file);
//                                        InputStreamReader rsa_reader = new InputStreamReader(rsa_fip, "UTF-8");
//                                        StringBuffer sb = new StringBuffer();
//                                        while (rsa_reader.ready()) {
//                                            sb.append((char) rsa_reader.read());
//                                        }
//                                        String rsa_String = sb.toString();
//                                        JSONObject rsa_Json = JSON.parseObject(rsa_String);
//                                        String pk_String = rsa_Json.getString("PK");
//                                        String sk_String = rsa_Json.getString("SK");
//                                        JSONObject pk_Json = JSON.parseObject(pk_String);
//                                        JSONObject sk_Json = JSON.parseObject(sk_String);
//                                        BigInteger c_d = new BigInteger(sk_Json.getString("d").getBytes());//私钥
//                                        BigInteger c_n = new BigInteger(sk_Json.getString("n").getBytes());//私钥
//                                        BigInteger c_e = new BigInteger(pk_Json.getString("e").getBytes());//公钥
//                                        String sig_String = RSA.RSA.Encrypt(Hash_Code, c_d, c_n);
//                                        msg_11_data.put("Sig", sig_String);
//                                        msg_11_data.put("Em", Base64.getEncoder().encodeToString(em));
//
//                                        JSONObject msg_11 = new JSONObject();
//                                        msg_11.put("id", 11);
//                                        msg_11.put("IDc", Main.User_ID);
//                                        String en_msg_11_data = DES_des.Encrypt_Text(msg_11_data.toJSONString(), Main.K_C_UP1);
//                                        msg_11.put("data", en_msg_11_data);
//
//                                        rsa_fip.close();
//                                        rsa_reader.close();
//
//                                        pw.write(msg_11 + "\n");
//                                        pw.flush();
//
//                                        //接收消息
//                                        is = socket.getInputStream();
//                                        br = new BufferedReader(new InputStreamReader(is));
//                                        String msg_0 = br.readLine();
//                                        JSONObject msg_0_Json = JSON.parseObject(msg_0);
//                                        if (msg_0_Json.getInteger("id") == 0) {
//                                            if (msg_0_Json.getInteger("status") == 11) {
//                                                logger.error("文件上传失败!");
//                                            } else {
//                                                logger.debug("文件上传成功");
//                                            }
//                                        } else {
//                                            logger.error("上传错误，未知错误");
//                                        }
//                                        upload_List.delete(upload_File_Index);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        logger.error("文件打开异常" + e);
//                                    }
//                                } else {
//                                    //文件不存在，上传失败
//                                    logger.error("文件不存在，上传失败");
//                                    upload_List.delete(upload_File_Index);//删除任务
//
//                                }
//                            } else {
//                                Thread.sleep(1000);
//                            }
//                        }else{
//
//                        }
//                    }else{
//
//                    }
//                } catch (Exception e) {
//                    logger.error("服务端已经断开连接\n");
//                    e.printStackTrace();
//                } finally {
//                    download_Worker_is_started = false;
//                    Thread.sleep(100);
//                    try {
//                        if (!(br == null)) {
//                            br.close();
//                        }
//                        if (!(is == null)) {
//                            is.close();
//                        }
//                        if (!(os == null)) {
//                            os.close();
//                        }
//                        if (!(pw == null)) {
//                            pw.close();
//                        }
//                        if (!(socket == null)) {
//                            socket.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else {
//                Thread.sleep(1000);
//            }
//        }
//    }

//    @FXML
//    prvate void search_Label_Clicked() {
//
//    }

    //更新文件列表
    @FXML
    private void update_Label_Clicked() throws IOException {
        //直接全部初始化面板
        file_Button_Pane_Init();
    }

    //点击下载按钮，下载选中任务
    @FXML
    private void download_Label_Clicked() {
        int t = all_files_List.get_Total();
        logger.debug("Download Clicked!");
        String download_File_Name = "";
        for (int i = 0; i < t; i++) {
            if (all_files_List.child_Pane.get(i).is_Checked) {
                download_File_Name += all_files_List.get_File_Name(i) + "\n";
                logger.debug("Download file: " + all_files_List.get_File_Name(i));
                download_List.add_NewFile(all_files_List.get_File_Name(i));
            }
        }
        //线程单独监控
        show_Info_Alerter("开始下载", "下载任务已经开始，正在下载以下文件", download_File_Name);
        if (!download_Worker_is_started) {
            //启动下载线程
            Runnable download_Thread_Task = new Runnable() {
                @Override
                public void run() {
                    try {
                        download_Worker();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        download_Worker_is_started = false;
                    }
                }
            };
            Thread download_worker_Thread = new Thread(download_Thread_Task);
            download_worker_Thread.setDaemon(true);
            download_worker_Thread.start();
            download_Worker_is_started = true;
        }
        download_File_Name = null;
        all_files_List.uncheck_All();//取消选中
    }

    //下载线程
    private void download_Worker() throws InterruptedException {
        while (true) {
            int download_File_Index = download_List.latestFile();
            if (download_List.latestFile() != -1) {
                Socket socket = null;
                OutputStream os = null;
                PrintWriter pw = null;
                InputStream is = null;
                BufferedReader br = null;
                download_List.show_Downloading(download_File_Index);
                update_and_view_Downloading_Pane();//更新

                try {
                    socket = new Socket(Main.down_Load_Server_IP, Main.down_Load_Server_Port);
                    logger.debug("尝试连接服务器");
                    os = socket.getOutputStream();//字节流(二进制)
                    pw = new PrintWriter(os);//字符编码

                    JSONObject message_9_Au_Json = new JSONObject();
                    message_9_Au_Json.put("id", 9);
                    message_9_Au_Json.put("Ticket_v", Main.ticket_DOWN1);
                    Date TS5 = new Date();
                    JSONObject au_Origin = new JSONObject();
                    au_Origin.put("IDc", Main.User_ID);
                    au_Origin.put("ADc", Main.ADc);
                    au_Origin.put("TS5", TS5);
                    String au_Origin_String = au_Origin.toJSONString();
                    String au_Encrypt_String = DES_des.Encrypt_Text(au_Origin_String, Main.K_C_DOWN1);

                    message_9_Au_Json.put("Authenticator_c", au_Encrypt_String);

                    pw.write(message_9_Au_Json + "\n");
                    pw.flush();


                    //接收消息
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String server_Message_10 = br.readLine();

                    JSONObject msg_10_Json = JSON.parseObject(server_Message_10);
                    if (msg_10_Json.getInteger("id") == 10) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(TS5);
                        calendar.add(calendar.HOUR, 1);
                        Date TS5_TEST = calendar.getTime();
                        String TS5_TEST_String = DES_des.Encrypt_Text(TS5_TEST.toString(), Main.K_C_DOWN1);
                        if (msg_10_Json.getString("ACK").equals(TS5_TEST_String)) {
                            logger.debug("下载端身份验证成功！");
                            //获取最新的一个文件名
                            //int total_Download_Num = download_List.get_Total();
                            if (download_File_Index != -1) {
                                String filename_To_Download = download_List.get_File_Name(download_File_Index);
                                JSONObject msg_12_data = new JSONObject();
                                msg_12_data.put("filename", filename_To_Download);
                                String Origion_msg_12_data = msg_12_data.toJSONString();
                                String Encrypt_msg_12_data = DES_des.Encrypt_Text(Origion_msg_12_data, Main.K_C_DOWN1);
                                JSONObject msg_12_Json = new JSONObject();
                                msg_12_Json.put("id", 12);
                                msg_12_Json.put("IDc", Main.User_ID);
                                msg_12_Json.put("data", Encrypt_msg_12_data);

                                pw.write(msg_12_Json + "\n");
                                pw.flush();

                                //接收消息
                                is = socket.getInputStream();
                                br = new BufferedReader(new InputStreamReader(is));
                                String server_Message_12 = br.readLine();
                                JSONObject msg_12_server_Json = JSON.parseObject(server_Message_12);
                                if (msg_12_server_Json.getInteger("id") == 12) {//服务器是否返回12号文件报文
                                    String data_encrypted = msg_12_server_Json.getString("data");
                                    String data_decrypted = DES_des.Decrypt_Text(data_encrypted, Main.K_C_DOWN1);
                                    JSONObject data_server = JSON.parseObject(data_decrypted);
                                    logger.debug("12号报文data解密成功\t" + data_decrypted);
                                    if (data_server.getString("filename").equals(filename_To_Download)) {//判断文件是否下载正确
                                        String em = data_server.getString("Em");
                                        String Hash_Code = String.valueOf(em.hashCode());
                                        File rsa_file = new File("client-fx/target/" + Main.User_ID + "_RSA_Key.txt");
                                        FileInputStream rsa_fip = new FileInputStream(rsa_file);
                                        InputStreamReader rsa_reader = new InputStreamReader(rsa_fip, "UTF-8");
                                        StringBuffer sb = new StringBuffer();
                                        while (rsa_reader.ready()) {
                                            sb.append((char) rsa_reader.read());
                                        }
                                        String rsa_String = sb.toString();
                                        JSONObject rsa_Json = JSON.parseObject(rsa_String);
                                        String pk_String = rsa_Json.getString("PK");
                                        String sk_String = rsa_Json.getString("SK");
                                        JSONObject pk_Json = JSON.parseObject(pk_String);
                                        JSONObject sk_Json = JSON.parseObject(sk_String);
                                        BigInteger c_d = new BigInteger(sk_Json.getString("d").getBytes());//私钥
                                        BigInteger c_n = new BigInteger(sk_Json.getString("n").getBytes());//私钥
                                        BigInteger c_e = new BigInteger(pk_Json.getString("e").getBytes());//公钥
                                        String sig_String = RSA.RSA.Encrypt(Hash_Code, c_d, c_n);

                                        rsa_fip.close();
                                        rsa_reader.close();

                                        if (sig_String.equals(data_server.getString("Sig"))) {
                                            logger.debug("签名验证成功");
                                            String content_Key = filename_To_Download + Main.User_ID;
                                            content_Key = Integer.toString(content_Key.hashCode());
                                            byte[] encrypted_File_Bytes = Base64.getDecoder().decode(em);
                                            byte[] decrypted_File_Bytes = DES_des.Decrypt_Text(encrypted_File_Bytes, content_Key);
                                            String encrypted_File_String = new String(encrypted_File_Bytes);
                                            String decrypted_File_String = new String(decrypted_File_Bytes);
                                            encrypted_File_String = encrypted_File_String.replace("\n", "").replace("\r", "").substring(0, 300);
                                            decrypted_File_String = decrypted_File_String.replace("\n", "").replace("\r", "").substring(0, 300);
                                            logger.debug("em解密成功！");
                                            File new_File = new File("./我的云盘/" + filename_To_Download);
                                            if (new_File.exists()) {
                                                logger.debug("文件已经存在，正在覆盖写");
                                            }
                                            rsa_file.createNewFile();//创建文件
                                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new_File));
                                            out.write(decrypted_File_Bytes);
                                            out.flush();
                                            out.close();
                                            logger.debug("下载完成");
                                            download_List.delete(download_File_Index);//下载完成删除任务

                                        } else {
                                            logger.debug("签名验证失败");
                                            download_List.show_Error_Downloading(download_File_Index);//签名认证错误，下载失败暂停任务
                                        }
                                    } else {//返回的文件不一样
                                        logger.debug("返回的文件不同");
                                        download_List.show_Error_Downloading(download_File_Index);//返回文件不同，下载失败暂停任务
                                    }
                                } else {//没有回复12号报文
                                    logger.debug("回复的不是12号文件报文");
                                    download_List.show_Error_Downloading(download_File_Index);//回复的不是12号报文，下载失败暂停任务
                                }
                            } else {//下载队列没有文件需要继续进行下载
                                Thread.sleep(1000);
                            }
                        } else {
                            logger.debug("身份验证出错");
                            download_List.show_Error_Downloading(download_File_Index);
                        }
                    } else {
                        logger.debug("服务端验证身份错误，服务端没有返回10号报文");
                        download_List.show_Error_Downloading(download_File_Index);
                    }
                } catch (Exception e) {
                    logger.error("服务端异常断开连接\n");
                    e.printStackTrace();
                    download_List.show_Error_Downloading(download_File_Index);//中断错误任务
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
                    } finally {
                        update_and_view_Downloading_Pane();//更新
                        Thread.sleep(100);
                    }
                }
            } else {
                Thread.sleep(1000);
            }
        }
    }

    //删除云盘中的文件
    @FXML
    private void delete_File_Label_Clicked() throws IOException {
        int t = all_files_List.get_Total();
        logger.debug("delete Clicked!");
        List<String> delete_filename_List = new ArrayList<>();
        String delete_File_Name = "";
        for (int i = 0; i < t; i++) {
            if (all_files_List.child_Pane.get(i).is_Checked) {
                delete_File_Name = all_files_List.get_File_Name(i);
                logger.debug("Download file: " + all_files_List.get_File_Name(i));
                delete_filename_List.add(delete_File_Name);//将需要删除的文件放进队列中
            }
        }

        //创建连接
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;

        try {
            socket = new Socket(Main.down_Load_Server_IP, Main.down_Load_Server_Port);
            logger.debug("尝试连接服务器");
            os = socket.getOutputStream();//字节流(二进制)
            pw = new PrintWriter(os);//字符编码

            JSONObject message_9_Au_Json = new JSONObject();
            message_9_Au_Json.put("id", 9);
            message_9_Au_Json.put("Ticket_v", Main.ticket_DOWN1);
            Date TS5 = new Date();
            JSONObject au_Origin = new JSONObject();
            au_Origin.put("IDc", Main.User_ID);
            au_Origin.put("ADc", Main.ADc);
            au_Origin.put("TS5", TS5);
            String au_Origin_String = au_Origin.toJSONString();
            String au_Encrypt_String = DES_des.Encrypt_Text(au_Origin_String, Main.K_C_DOWN1);

            message_9_Au_Json.put("Authenticator_c", au_Encrypt_String);

            pw.write(message_9_Au_Json + "\n");
            pw.flush();

            //接收消息
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String server_Message_10 = br.readLine();

            JSONObject msg_10_Json = JSON.parseObject(server_Message_10);
            if (msg_10_Json.getInteger("id") == 10) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TS5);
                calendar.add(calendar.HOUR, 1);
                Date TS5_TEST = calendar.getTime();
                String TS5_TEST_String = DES_des.Encrypt_Text(TS5_TEST.toString(), Main.K_C_DOWN1);
                if (msg_10_Json.getString("ACK").equals(TS5_TEST_String)) {
                    logger.debug("下载端身份验证成功！");
                    JSONObject msg_14_data_Json = new JSONObject();
                    msg_14_data_Json.put("num", delete_filename_List.size());
                    msg_14_data_Json.put("filename", delete_filename_List);
                    //进行des加密
                    String msg_14_data_encrypted = DES_des.Encrypt_Text(msg_14_data_Json.toJSONString(), Main.K_C_DOWN1);

                    JSONObject msg_14_Json = new JSONObject();
                    msg_14_Json.put("id", 14);
                    msg_14_Json.put("IDc", Main.User_ID);
                    msg_14_Json.put("data", msg_14_data_encrypted);

                    pw.write(msg_14_Json + "\n");
                    pw.flush();

                    //接收消息
                    is = socket.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String server_Message_0 = br.readLine();
                    JSONObject msg_0_Status = JSON.parseObject(server_Message_0);
                    if (msg_0_Status.getInteger("id") == 0) {
                        if (msg_0_Status.getInteger("status") == 13) {
                            show_Error_Alerter("删除状态", "文件删除失败", "可能有部分文件删除失败，请重试！");
                        } else if (msg_0_Status.getInteger("status") == 12) {
                            show_Info_Alerter("删除状态", "文件删除成功", "文件已经成功删除！");
                        } else {
                            show_Error_Alerter("删除状态", "未知错误", "出现未知错误，请重试！");
                        }
                    } else {//收到的不是状态码，失败
                        show_Error_Alerter("删除状态", "未知错误", "出现未知错误，请重试！");
                    }
                }
            } else {
                show_Error_Alerter("删除状态", "删除失败", "身份验证失败，请重新登录！");
                Starter.setRoot("Login", "登录 | 瓜娃子云盘", 420, 512, 420, 512, 420, 512);
            }
        } catch (Exception e) {
            logger.error("服务端已经断开连接\n");
            e.printStackTrace();
            show_Error_Alerter("删除状态", "未知错误", "服务器异常断开连接！请重试");
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
            } finally {
                file_List_Update_And_Show();
                logger.debug("刷新文件列表");

            }
        }
    }


    //删除上传中的任务
    @FXML
    private void delete_Upload_Task_Clicked() {

    }

    //重启上传中出错的任务
    @FXML
    private void restart_Upload_Task_Clicked() {

    }

    //初始化
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //加载图片
        URL image_Url = Starter.class.getResource("img/logo.png");
        ImageView logo_Image = new ImageView(image_Url.toExternalForm());
        logo_Image.setFitHeight(29);
        logo_Image.setFitWidth(29);
        logo_Label.setGraphic(logo_Image);
        file_IMG_Label.setGraphic(set_User_Img("file_IMG_Label"));
        uploading_IMG_Label.setGraphic(set_User_Img("uploading_IMG_Label"));
        downloading_IMG_Label.setGraphic(set_User_Img("downloading_IMG_Label"));
        completed_IMG_Label.setGraphic(set_User_Img("completed_IMG_Label"));
        user_IMG_Label.setGraphic(set_User_Img("user_IMG_Label"));
        upload_IMG_Label.setGraphic(set_User_Img("upload_IMG_Label"));
        //search_IMG_Label.setGraphic(set_User_Img("search_IMG_Label"));
        update_IMG_Label.setGraphic(set_User_Img("update_IMG_Label"));
        download_IMG_Label.setGraphic(set_User_Img("download_IMG_Label"));
        delete_File_IMG_Label.setGraphic(set_User_Img("delete_File_IMG_Label"));
        delete_Download_Task_IMG_Label.setGraphic(set_User_Img("delete_Download_Task_IMG_Label"));
        delete_Upload_Task_IMG_Label.setGraphic(set_User_Img("delete_Upload_Task_IMG_Label"));
        restart_Download_Task_IMG_Label.setGraphic(set_User_Img("update_IMG_Label"));
        restart_Upload_Task_IMG_Label.setGraphic(set_User_Img("update_IMG_Label"));
        //加载文件场景
        try {
            file_Button_Pane_Init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载图片
    private ImageView set_User_Img(String file_Name) {
        URL image_Url = Starter.class.getResource("img/User/" + file_Name + ".png");
        ImageView image = new ImageView(image_Url.toExternalForm());
        image.setFitHeight(small_Img_Size);
        image.setFitWidth(small_Img_Size);
        return image;
    }
}
