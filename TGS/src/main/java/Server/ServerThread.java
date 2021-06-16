package Server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class ServerThread extends Thread {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServerThread.class);
    private Socket socket = null;
    private TGS_Server server;//应用服务

    public ServerThread(Socket socket) throws SQLException, ClassNotFoundException {
        logger.info("通信线程已启动");
        this.socket = socket;
        server = new TGS_Server(socket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        logger.debug("通信线程开始运行");
        try {
            //消息接受和发送
            while (true) {
                //接受请求
                logger.debug("接受请求");
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String info = br.readLine();
//                //心跳包
//                if (info == null) {//数据异常判断客户端是否关闭
//                    socket.sendUrgentData(0xFF);//抛出异常
//                }
                // while((info=br.readLine())!=null){
                System.out.println("客户端输出：" + info);
                //  }
                // socket.shutdownInput();//接受完毕进行关闭
                //响应请求
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                JSONObject message = JSON.parseObject(info);
                System.out.println(info);
                int id = message.getInteger("id");
                switch (id) {
                    case 7://请求Ticketv
                    {
                        int status=server.VerifyTicketTGS(info);
                        if (status == -1) {//证书验证成功
                            logger.error("证书验证成功");
                            String msg = server.GenerateTicketVMessage(info);
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            //socket.shutdownOutput();
                            // Thread.sleep(5000);
                        } else {
                            logger.info("票据验证失败");
                            String msg = server.StatusMessage(status);
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            throw new Exception();
                        }
                        break;
                    }
                    default:
                        logger.error("异常报文");
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("客户端断开连接");
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
                if(server.conn!=null)
                    server.conn.close();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
