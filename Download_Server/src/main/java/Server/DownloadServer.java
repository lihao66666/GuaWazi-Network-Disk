package Server;

import DES.DES_des;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class DownloadServer {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DownloadServer.class);
    private static DES_des DES = new DES_des();
    static final String Server_ID = "DOWN1";//服务器的ID(固定)
    static final String Kv = "12345678";//保存上传 Server 与 TGS 的密钥
    private String Client_ID;//客户端ID
    //    private String Client_AD="127.0.0.1";//客户端IP地址
    private String Client_AD;//客户端IP地址
    private Date TS5;//ticket签发时间
    private Date Lifetime4;//生存周期
    private String Kc_v = null;//记录访问客户端与服务端的密钥

    // public UploadServer(){}
    public DownloadServer(String Client_AD) {
        logger.debug("点对点UploadServer已启动,客户端IP:" + Client_AD);
        this.Client_AD = Client_AD;//处理该地址的server
    }

    public boolean check_CA(String message) throws Exception {//解析9号报文
        logger.debug("解析客户端证书(9号报文)");
        JSONObject msg = JSON.parseObject(message);//转换为Json对象

        String Ticket_v = msg.getString("Ticket_v");//获取Ticket
        /*DES Kv解密票据*///
        Ticket_v = DES.Decrypt_Text(Ticket_v, this.Kv);
        JSONObject ticket_v = JSON.parseObject(Ticket_v);
        this.Kc_v = ticket_v.getString("Kc_v");
        //System.out.println("\n票据"+ticket_v);
        String Authenticator_c = msg.getString("Authenticator_c");
        /*DES Kc_v解密票据*///
        //解密过程................
        Authenticator_c = DES.Decrypt_Text(Authenticator_c, this.Kc_v);
        JSONObject authenticator_c = JSON.parseObject(Authenticator_c);
        //System.out.println("\n认证"+authenticator_c);

        //验证
        if (!ticket_v.getString("IDv").equals(Server_ID))
            return false;

        if (!ticket_v.getString("IDc").equals(authenticator_c.getString("IDc")))
            return false;

        if (!ticket_v.getString("ADc").equals(authenticator_c.getString("ADc")) || !ticket_v.getString("ADc").equals(this.Client_AD))
            return false;
        // System.out.println("\n报文:"+msg);
//        Date date = new Date();
//        ticket_v.getDate("Lifetime4");
        if (ticket_v.getDate("Lifetime4").before(new Date()))
            return false;

        //验证成功
        this.Client_ID = ticket_v.getString("IDc");
        this.Lifetime4 = ticket_v.getDate("Lifetime4");
        this.TS5 = authenticator_c.getDate("TS5");
        return true;
    }

    public void creat_NetDisk() {
        File file = new File("./云盘/" + this.Client_ID + File.separator);
        if (file.exists() == false)
            file.mkdir();
    }

    public String return_CA() throws Exception {//生成10号报文
        logger.debug("生成客户端返回证书");
        JSONObject message = new JSONObject();
        message.put("id", 10);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(TS5);
        calendar.add(calendar.HOUR, 1); //把时间向后推迟1小时
        Date TS6 = calendar.getTime(); //这个时间就是日期往后推一天的结果
        String ACK = DES.Encrypt_Text(TS6.toString(), this.Kc_v);//TS6进行DES Kc_v加密
        message.put("ACK", ACK);
        return message.toJSONString();
    }

    public String status_message(int type) {
        logger.debug("生成状态报文");
        JSONObject message = new JSONObject();
        message.put("id", 0);
        message.put("status", type);
        return message.toJSONString();
    }

    public String Download_Handler(String message) throws Exception {
        logger.debug("解析文件下载请求报文(12号报文)");
        // System.out.println(message);
        JSONObject msg = JSON.parseObject(message);
        if (!msg.getString("IDc").equals(this.Client_ID)) {
            logger.error("非法错误");
            return null;
        }

        String Data = msg.getString("data");
        //Data进行DES解密 Kc_v...........
        Data = DES.Decrypt_Text(Data, this.Kc_v);
        JSONObject data = JSON.parseObject(Data);

        String filename = data.getString("filename");
        File file = new File("./云盘/" + this.Client_ID + File.separator + filename);
        String str = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = in.readAllBytes();//按字节全部读出
            str = new String(bytes);//转换为string
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("系统异常");
        }
        //String str=new String(bytes);//转换为string
        JSONObject filebuff = JSON.parseObject(str);
        JSONObject newData = new JSONObject();
        newData.put("filename", filename);
        newData.put("Sig", filebuff.getString("Sig"));
        newData.put("Em", filebuff.getString("Em"));
        //newData进行DES加密 Kc_v...........
        String newdata = DES.Encrypt_Text(newData.toJSONString(), this.Kc_v);
        JSONObject newmsg = new JSONObject();
        newmsg.put("id", 12);
        newmsg.put("IDc", this.Client_ID);
        newmsg.put("data", newdata);
        return newmsg.toJSONString();
    }

    public String Browse_Handler(String message) throws Exception {
        logger.debug("解析网盘浏览请求报文(13号报文)");
        System.out.println(message);
        JSONObject msg = JSON.parseObject(message);
        if (!msg.getString("IDc").equals(this.Client_ID)) {
            logger.error("非法错误");
            return null;
        }
        JSONObject newmsg = new JSONObject();
        newmsg.put("id", 12);
        newmsg.put("IDc", this.Client_ID);
        JSONObject newData = new JSONObject();
        File file = new File("./云盘/" + this.Client_ID + File.separator);
        if (file.exists() == false) {
            logger.error("系统错误");
            return null;
        }
        String[] str = file.list();
        List<String> filename = new ArrayList<String>();
        for (String s : str)
            filename.add(s);
        newData.put("num", filename.size());
        newData.put("filename", filename);
        //newData进行DES加密 Kc_v...........
        String newdata = DES.Encrypt_Text(newData.toJSONString(), this.Kc_v);
        newmsg.put("data", newdata);
        return newmsg.toJSONString();
    }

    public boolean Delete_Handler(String message) throws Exception {
        logger.debug("解析网盘删除请求报文(14号报文)");
        System.out.println(message);
        JSONObject msg = JSON.parseObject(message);
        if (!msg.getString("IDc").equals(this.Client_ID)) {
            logger.error("非法错误");
            return false;
        }
        String Data = msg.getString("data");
        //Data进行DES解密 Kc_v...........
        Data = DES.Decrypt_Text(Data, this.Kc_v);
        JSONObject data = JSON.parseObject(Data);
        //获取文件列表
        List<String> filename = data.getJSONArray("filename").toJavaList(String.class);
        for (String f : filename) {
            File file = new File("./云盘/" + this.Client_ID + File.separator + f);
            if (file.delete() == false) {
                logger.error("系统错误");
                return false;
            }
            logger.error("./云盘/" + this.Client_ID + File.separator + f + "删除成功");
        }
        return true;
    }

    public String creat_msg9() throws Exception {//用于测试
        // 生成认证报文
        logger.debug("9号报文生成");
        JSONObject ticket_v = new JSONObject();
        ticket_v.put("Kc_v", "keyc_v");
        ticket_v.put("IDc", 99);
        ticket_v.put("ADc", "127.0.0.1");
        ticket_v.put("IDv", 1);
        Date TS4 = new Date();
        ticket_v.put("TS4", TS4);
        //System.out.println(TS4);

        Date Lifetime4 = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(Lifetime4);
        calendar.add(calendar.HOUR, 1); //把时间向后推迟1小时
        Lifetime4 = calendar.getTime(); //这个时间就是日期往后推一天的结果
        ticket_v.put("Lifetime4", Lifetime4);
        //DES Kv加密
        String Ticket_v = DES.Encrypt_Text(ticket_v.toJSONString(), this.Kv);

        JSONObject authenticator_c = new JSONObject();
        authenticator_c.put("IDc", 99);
        authenticator_c.put("ADc", "127.0.0.1");
        Date TS5 = new Date();
        authenticator_c.put("TS5", TS5);
        //DES Kc_v加密
        String Authenticator_c = DES.Encrypt_Text(authenticator_c.toJSONString(), "keyc_v");

        JSONObject message = new JSONObject();
        message.put("id", 9);
        message.put("Ticket_v", Ticket_v);
        message.put("Authenticator_c", Authenticator_c);

        String MSG = message.toJSONString();
        return MSG;
    }


    public String creat_msg11() {
        JSONObject message = new JSONObject();
        message.put("id", 11);
        message.put("IDc", 99);
        JSONObject Data = new JSONObject();
        Data.put("filename", "lihao.txt");
        Data.put("Sig", "你是大手笔");
        Data.put("Em", "我爱你中国");
        //Data DES Kc_v加密............
        String data = DES.Encrypt_Text(Data.toJSONString(), "keyc_v");
        message.put("data", data);
        //System.out.println(message);
        return message.toJSONString();
    }

    public String creat_msg12() {
        JSONObject message = new JSONObject();
        message.put("id", 12);
        message.put("IDc", 99);
        JSONObject Data = new JSONObject();
        Data.put("filename", "lihao.txt");
        //Data DES Kc_v加密............
        String data = DES.Encrypt_Text(Data.toJSONString(), "keyc_v");
        message.put("data", data);
        //System.out.println(message);
        return message.toJSONString();
    }

    public String creat_msg13() {
        JSONObject message = new JSONObject();
        message.put("id", 13);
        message.put("IDc", 99);
        //System.out.println(message);
        return message.toJSONString();
    }

    public String creat_msg14() {
        JSONObject message = new JSONObject();
        message.put("id", 14);
        message.put("IDc", 99);
        JSONObject Data = new JSONObject();
        Data.put("num", 2);
        List<String> filename = new ArrayList<String>();
        filename.add("lihao.txt");
        // filename.add("liu.jpg");
        Data.put("filename", filename);
        //Data DEs kc_v加密
        String data = DES.Encrypt_Text(Data.toJSONString(), "keyc_v");
        message.put("data", data);
        //System.out.println(message);
        return message.toJSONString();
    }

    public static void main(String[] args) throws Exception {
        DownloadServer server = new DownloadServer("127.0.0.1");
        String str1 = server.creat_msg9();
        if (server.check_CA(str1))
            logger.info("认证成功");
        else
            logger.error("认证失败");
        System.out.println(server.return_CA());
        System.out.println(server.status_message(1));
        String str2 = server.creat_msg12();
        System.out.println(server.Download_Handler(str2));
        String str3 = server.creat_msg13();
        System.out.println(server.Browse_Handler(str3));
        String str4 = server.creat_msg14();
        System.out.println(server.Delete_Handler(str4));
    }
}
