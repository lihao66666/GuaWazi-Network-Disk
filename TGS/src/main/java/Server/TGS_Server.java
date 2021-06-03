package Server;

import DES.DES_des;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TGS_Server {
    private static final String URL = "jdbc:mysql://47.117.190.99:3306/gwz_db";//连接到的数据库
    private static final String NAME = "GWZ_DB";//用户名
    private static final String PASSWORD = "3hfYLRaCmyfKMWEH";//密码
    public static Connection conn = null;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TGS_Server.class);
    static final String Server_ID = "TGS1";//服务器的ID(固定)
    private String Client_ID;//客户端ID
    private String Client_AD = "127.0.0.1";//客户端IP地址
    private Date TS4;//ticket签发时间
    private Date LifeTime4;//生存周期
    private String Kc_tgs;
    private String IDv;

    public TGS_Server(String Client_AD) {//构造函数
        logger.debug("点对点UploadServer已启动,客户端IP:" + Client_AD);
        this.Client_AD = Client_AD;//处理该地址的server
    }

    public static Boolean ConnectToDB() throws ClassNotFoundException, SQLException {//连接到数据库，保存连接句柄
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("Database connection established");
            if (conn == null) {
                logger.error("连接数据库失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }


    }


    public String StatusMessage(int type) {//状态报文函数
        logger.debug("生成状态报文");
        JSONObject message = new JSONObject();
        message.put("id", 0);
        message.put("status", type);
        return message.toJSONString();
    }

    public int VerifyTicketTGS(String message) throws SQLException {
        try {
            JSONObject obj = JSON.parseObject(message);
            IDv = obj.getString("IDv");
            String Ticket_TGS = obj.getString("Ticket_TGS");
            Statement stmt = conn.createStatement();
            String Ktgs = null;
            ResultSet rs = stmt.executeQuery("select * from `Key_AS-TGS`");//从数据库中获取所有用户ID
            while (rs.next()) {//如果对象中有数据，就会循环打印出来
                //if (rs.getString("AS_ID").equals(Server_ID)) {//判断该ID是否已经存在数据库中
                    if (rs.getString("TGS_ID").equals(Server_ID))
                        Ktgs = rs.getString("TGS-AS_Key");

                //}
            }
            Ticket_TGS = DES_des.Decrypt_Text(Ticket_TGS, Ktgs);
            logger.debug("Ticket_Tgs====="+Ticket_TGS);
            JSONObject Ticket_obj = JSON.parseObject(Ticket_TGS);
            Kc_tgs = Ticket_obj.getString("Kc_tgs");
            Client_ID = Ticket_obj.getString("IDc");
            Date now = new Date();
            if (Ticket_obj.getDate("Lifetime2").before(now)) {
                logger.debug("TGS票据过期");
                return 9;
            } else {
                logger.debug("TGS票据验证成功");
                return -1;
            }
        } catch (Exception e) {
            logger.debug("TGS验证失败");
            return 6;
        }

    }

    public String GenerateTicketVMessage(String message) throws SQLException {
        JSONObject TicketV_obj = new JSONObject();
        TicketV_obj.put("Kc_v", Integer.toString((Client_ID + IDv).hashCode()));
        TicketV_obj.put("IDc", Client_ID);
        TicketV_obj.put("ADc", Client_AD);
        TicketV_obj.put("IDv", IDv);
        TS4 = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(TS4);
        calendar.add(calendar.SECOND, 1); //把时间向后推迟1秒
        LifeTime4 = calendar.getTime(); //这个时间就是日期往后推一天的结果
        TicketV_obj.put("TS4",TS4);
        TicketV_obj.put("LifeTime4",LifeTime4);
        String TicketV=TicketV_obj.toJSONString();
        Statement stmt = conn.createStatement();
        String Ktgs_v = null;
        ResultSet rs = stmt.executeQuery("select * from `Key_V-TGS`");//从数据库中获取所有用户ID
        while (rs.next()) {//如果对象中有数据，就会循环打印出来
            if (rs.getString("V_ID").equals(IDv)) {//判断该ID是否已经存在数据库中
                if (rs.getString("TGS_ID").equals(Server_ID))
                    Ktgs_v = rs.getString("TGS-V_Key");

            }
        }
        TicketV=DES_des.Encrypt_Text(TicketV,Ktgs_v);
        JSONObject TGS_C_obj=new JSONObject();
        TGS_C_obj.put("Kc_v", Integer.toString((Client_ID + IDv).hashCode()));
        TGS_C_obj.put("IDv", IDv);
        TGS_C_obj.put("TS4",TS4);
        TGS_C_obj.put("Ticket_V",TicketV);
        String TGS_C=TGS_C_obj.toJSONString();
        TGS_C= DES_des.Encrypt_Text(TGS_C,Kc_tgs);
        JSONObject msg=new JSONObject();
        msg.put("id",6);
        msg.put("TGS_C",TGS_C);
        return msg.toJSONString();
    }
}