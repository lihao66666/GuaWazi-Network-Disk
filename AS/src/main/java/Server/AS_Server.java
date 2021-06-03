package Server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.sql.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import RSA.RSA;

public class AS_Server {
    private static final String URL = "jdbc:mysql://47.117.190.99:3306/gwz_db";//连接到的数据库
    private static final String NAME = "GWZ_DB";//用户名
    private static final String PASSWORD = "3hfYLRaCmyfKMWEH";//密码
    Connection conn;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AS_Server.class);
    static final String Server_ID = "1";//服务器的ID(固定)
    static final String Kv = "key";//保存上传 Server 与 TGS 的密钥
    private String Client_ID;//客户端ID
    private String Client_AD = "127.0.0.1";//客户端IP地址
    private Date TS2;//ticket签发时间
    //private Date Lifetime2;//生存周期
    private String Kc_v = null;//记录访问客户端与服务端的密钥
    private String PKc = null;//记录连接的客户端的公钥
    private BigInteger KDC_d;
    private BigInteger KDC_n;
    private BigInteger C_d;
    private BigInteger C_n;

    public AS_Server(String Client_AD) {//构造函数
        logger.debug("点对点UploadServer已启动,客户端IP:" + Client_AD);
        this.Client_AD = Client_AD;//处理该地址的server
    }

    public Boolean ConnectToDB() throws ClassNotFoundException, SQLException {//连接到数据库，保存连接句柄
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(URL, NAME, PASSWORD);
        if(conn==null)return false;
        return true;
    }

    public void GetSK_KDC() throws Exception {//获取KDC私钥
        File f = new File("./KDC_Key.txt");
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
        String[] strs = str.split("\r\n");

        String n = strs[0];
        String e = strs[1];
        String d = strs[2];

        KDC_n = new BigInteger(Base64.getDecoder().decode(n));//获取n
        KDC_d = new BigInteger(Base64.getDecoder().decode(d));//获取d

        reader.close();// 关闭读取流
        fip.close();// 关闭输入流,释放系统资源
    }

    public boolean VerifyLicense(String message) throws Exception {//验证证书函数
        logger.debug("验证客户端证书（1号报文）");
        JSONObject msg = JSON.parseObject(message);//转换为Json对象

        String CA = msg.getString("CA");//获取客户端证书
        String CAHash = msg.getString("CAHash");//获取客户端证书Hash值

        if (!CAHash.equals(new String(Integer.toString(CA.hashCode())))) {//验证证书内容是否正确
            return false;
        } else {
            String DecryptedCA = RSA.Decrypt(CA, KDC_d, KDC_n);//证书使用KDC私钥解密
            JSONObject ca = JSON.parseObject(DecryptedCA);//将解密的结果转换为一个Json对象

            Client_ID = ca.getString("user_ID");//获取客户端ID
            PKc = ca.getString("PKc");//获取客户端公钥
            String[] PKcs = PKc.split("\r\n");
            C_n = new BigInteger(Base64.getDecoder().decode(PKcs[0]));
            C_d = new BigInteger(Base64.getDecoder().decode(PKcs[1]));
            return true;
        }
    }

    public String StatusMessage(int type) {//状态报文函数
        logger.debug("生成状态报文");
        JSONObject message = new JSONObject();
        message.put("id", 0);
        message.put("status", type);
        return message.toJSONString();
    }

    public String GenerateASLicenseMessage() throws IOException {//返回交换证书报文
        File ca_file = new File("./CA.txt");
        if (!ca_file.exists()) {//判断证书文件是否存在
            RSA.GenerateCA(Server_ID);//若不存在则生成证书文件
        }
        FileInputStream ca_fip = new FileInputStream(ca_file);
        InputStreamReader reader = new InputStreamReader(ca_fip, "UTF-8");
        // 构建InputStreamReader对象,编码与写入相同

        StringBuffer ca_sb = new StringBuffer();
        while (reader.ready()) {
            ca_sb.append((char) reader.read());
            // 转成char加到StringBuffer对象中
        }
        String ca = ca_sb.toString();//获取到证书
        JSONObject message = new JSONObject();//创建一个Json对象
        message.put("ID", 2);//设置报文ID为2
        message.put("CA", ca);//放入证书
        message.put("CAHash", new String(Integer.toString(ca.hashCode())));//放入证书的Hash值
        reader.close();
        ca_fip.close();
        return message.toJSONString();
    }

    public int ClientRegister(String message) throws SQLException {//客户端注册函数
        JSONObject msg = JSON.parseObject(message);//将传入的字符串转换为Json对象
        String user_id = msg.getString("user_ID");//获取用户ID
        String password = msg.getString("password");//获取用户密码
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select User_ID from User");//从数据库中获取所有用户ID
        while (rs.next()) {//如果对象中有数据，就会循环打印出来
            if (rs.getString("User_ID").equals(user_id)) {//判断该ID是否已经存在数据库中
                logger.error("该账户已存在");
                return 17;//返回状态码17，该账户已经注册
            }
        }
        if(stmt.executeUpdate("INSERT INTO User ( User_ID,User_PassWord ) VALUES ( " + user_id + "," + password + ")")>0){//如果该账户没有注册，则注册该账户进数据库
            logger.debug("注册成功");
            return 0;//写入成功，返回注册成功状态码
        }
        else{
            logger.error("注册失败");
            return 1;//写入失败，返回注册失败状态码
        }
    }

    public int ClientLogin(String message) throws IOException, SQLException {//客户端登录状态函数
        File file=new File("./RSA_Key.txt");//AS打开自己的密钥文件
        FileInputStream fip=new FileInputStream(file);
        InputStreamReader reader=new InputStreamReader(fip,"UTF-8");
        StringBuffer sb=new StringBuffer();
        while(reader.ready()){
            sb.append((char)reader.read());
        }
        String str=sb.toString();
        String[] strs=str.split("\r\n");
        BigInteger as_d=new BigInteger(Base64.getDecoder().decode(strs[2]));//获取到AS的d
        BigInteger as_n=new BigInteger(Base64.getDecoder().decode(strs[0]));//获取到AS的n

        JSONObject msg=JSON.parseObject(message);//将传入的文件转换为Json对象
        String loginData_rsa=msg.getString("loginData");//获取对象中的登录信息
        String loginData_json=RSA.Decrypt(loginData_rsa,as_d,as_n);//将登录信息使用AS的私钥进行解密
        JSONObject loginData=JSON.parseObject(loginData_json);//解密出来的结果转换为Json对象
        String user_id=loginData.getString("user_ID");//获取用户ID
        String password=loginData.getString("password");//获取用户密码
        Statement stmt= conn.createStatement();
        ResultSet rs = stmt.executeQuery("select User_ID，User_PassWord from User");//从数据库中获取所有的用户名和密码
        while (rs.next()) {//如果对象中有数据，就会循环打印出来
            if (rs.getString("User_ID").equals(user_id)) {//找到对应的用户ID
                if(rs.getString("User_PassWord").equals(password)){//判断密码是否一致
                    logger.debug("登录成功");
                    return 4;//密码一致，登录成功
                }
                else{
                    logger.error("密码不一致，登录失败");
                    return 3;//密码不一致，返回登录失败
                }
            }
        }
        logger.error("没有该账户，登录失败");
        return 2;
    }

    public Boolean VerifyRequestOfTicket(String message) throws SQLException {//验证请求TGS的请求是否正确
        Statement stmt= conn.createStatement();
        JSONObject request=JSON.parseObject(message);
        String TGS_ID=request.getString("IDtgs");
        ResultSet rs = stmt.executeQuery("select Server_ID from Server where Server_ID="+TGS_ID+"");
        if(rs.getString("Server_ID").equals(TGS_ID)){
            logger.debug("请求的TGS在数据库中");
            return true;
        }
        else {
            logger.error("请求的TGS不在数据库中");
            return false;
        }
    }

    public String GenerateTicketMessage(String message){
        JSONObject request=JSON.parseObject(message);
        String IDc=request.getString("user_ID");
        String IDtgs=request.getString("IDtgs");
        String Kc_tgs=Integer.toString((IDc+IDtgs).hashCode());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(TS2);
        calendar.add(calendar.SECOND,1); //把时间向后推迟1秒
        Date LifeTime2=calendar.getTime(); //这个时间就是日期往后推一天的结果
        JSONObject Ticket_tgs=new JSONObject();
        Ticket_tgs.put("Kc_tgs",Kc_tgs);
        Ticket_tgs.put("IDc",IDc);
        Ticket_tgs.put("ADc",Client_AD);
        Ticket_tgs.put("IDtgs",IDtgs);
        Ticket_tgs.put("TS2",TS2);
        Ticket_tgs.put("Lifetime2",LifeTime2);
        String Ticket= DES.DES_des.Encrypt_Text(Ticket_tgs.toJSONString(),Integer.toString(IDc.hashCode())) ;
        JSONObject AS_C=new JSONObject();
        AS_C.put("Kc_tgs",Kc_tgs);
        AS_C.put("IDtgs",IDtgs);
        AS_C.put("TS2",TS2);
        AS_C.put("Lifetime",LifeTime2);
        AS_C.put("Ticket_tgs",Ticket);
        String encryptedTicket=AS_C.toJSONString();
        JSONObject msg=new JSONObject();
        msg.put("id",6);
        msg.put("encryptedTicket",encryptedTicket);
        return msg.toJSONString();
    }
}