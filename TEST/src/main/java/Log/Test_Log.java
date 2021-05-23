package Log;
import org.apache.log4j.Logger;

public class Test_Log {
    private static final Logger logger = Logger.getLogger(Test_Log.class);
    //请在resources文件中复制log4j的配置文件
    public static void main(String[] args) {
        logger.info("192.168.1.1 建立连接");
        logger.info("客户端2 1号请求报文");
        logger.error("身份验证失败,断开连接");
        logger.error("身份验证失败,断开连接");
    }
}
