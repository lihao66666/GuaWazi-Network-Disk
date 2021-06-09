package App.Controller;

import App.Main;
import App.Starter;
import DES.DES_des;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

public class User_Controller implements Initializable {

    public static User_Show_All_File_ArrayList all_files_List = new User_Show_All_File_ArrayList();
    public static User_Show_Download_Task_List download_List = new User_Show_Download_Task_List();
    private static final Logger logger = Logger.getLogger(User_Controller.class);

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
    private void file_Button_Clicked() {
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
    private void file_Button_Pane_Init() {
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

        update_Files_List();
        view_Files_List();
        show_File_ScrollPane.setContent(show_All_File_AnchorPane);
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

            JSONObject au_Origin = new JSONObject();
            au_Origin.put("IDc", Main.User_ID);
            au_Origin.put("ADc", Main.ADc);
            au_Origin.put("TS5", new Date());
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
                //TODO 验证ACK


            } else if (msg_10_Json.getInteger("id") == 0) {
                if (msg_10_Json.getInteger("status") == 7) {
                    return 7;//Ticket_Download_Server认证失败
                } else {
                    return 100;//未知错误
                }
            } else {
                return 100;//未知错误
            }
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

        view_DownLoad_Task_List();
        show_File_ScrollPane.setContent(show_Download_Task_AnchorPane);
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
                    show_Download_Task_AnchorPane.getChildren().add(download_List.child_Pane.get(i).pane);
                    show_Download_Task_AnchorPane.setTopAnchor(download_List.child_Pane.get(i).pane, height);
                    show_Download_Task_AnchorPane.setLeftAnchor(download_List.child_Pane.get(i).pane, height);
                    show_Download_Task_AnchorPane.setRightAnchor(download_List.child_Pane.get(i).pane, height);
                    height += 35d;
                }
            }
        }
        for (int i = 0; i < t; i++) {
            if (!download_List.is_NULL.get(i)) {//判断任务是否删除
                if (!download_List.child_Pane.get(i).is_Downloading) {//先添加正在等待的
                    show_Download_Task_AnchorPane.getChildren().add(download_List.child_Pane.get(i).pane);
                    show_Download_Task_AnchorPane.setTopAnchor(download_List.child_Pane.get(i).pane, height);
                    show_Download_Task_AnchorPane.setLeftAnchor(download_List.child_Pane.get(i).pane, 0d);
                    show_Download_Task_AnchorPane.setRightAnchor(download_List.child_Pane.get(i).pane, 0d);
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
                if (!download_List.child_Pane.get(i).is_Downloading) {//先添加正在等待的
                    if (download_List.child_Pane.get(i).is_Checked) {
                        delete_Task_Name += download_List.get_File_Name(i) + "\n";
                        logger.debug("删除任务: " + download_List.get_File_Name(i));
                        download_List.delete(i);//从列表中删除
                    }
                } else {
                    delete_Failed += download_List.get_File_Name(i) + "\n";
                    logger.debug("删除任务错误: 任务正在下载中" + download_List.get_File_Name(i));
                }
            }
        }
        if (delete_Failed.equals("")) {
            show_Info_Alerter("提示", "以下任务已经删除\n", delete_Task_Name);
        } else {
            show_Info_Alerter("提示", "以下任务已经删除\n" + delete_Task_Name, "以下任务正在下载中，无法删除\n" + delete_Failed);
        }

        delete_Task_Name = null;
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
    }

    //选择文件上传
    @FXML
    private void upload_Label_Cliecked() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择需要上传的文件");
        File file = fileChooser.showOpenDialog(Starter.get_Current_Stage());
        if (file != null) {
            try {
                InputStream file_Input = new FileInputStream(file);
                int size = file_Input.available();
                //文件打开

                for (int i = 0; i < size; i++) {
                    System.out.print((char) file_Input.read());
                }
                file_Input.close();
            } catch (IOException e) {
                show_Error_Alerter("错误", "文件打开错误！", "Exception" + e);
                logger.error("Exception" + e);
            }
        }
    }

    //上传线程
    private void uploader() {

    }

//    @FXML
//    private void search_Label_Clicked() {
//
//    }

    //更新文件列表
    @FXML
    private void update_Label_Clicked() {
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

        show_Info_Alerter("开始下载", "下载任务已经开始，正在下载以下文件", download_File_Name);
        download_File_Name = null;
        all_files_List.uncheck_All();//取消选中

    }

    //下载线程
    private void downloader() {

    }

    //删除云盘中的文件
    @FXML
    private void delete_File_Label_Clicked() {

    }


    //删除上传中的任务
    @FXML
    private void delete_Upload_Task_Clicked() {

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

        //加载文件场景
        file_Button_Pane_Init();
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
