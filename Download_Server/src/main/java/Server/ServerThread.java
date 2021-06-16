package Server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;


public class ServerThread extends Thread {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServerThread.class);
    private Socket socket = null;
    private DownloadServer server;//应用服务

    public ServerThread(Socket socket) {
        logger.info("通信线程已启动");
        this.socket = socket;
        server = new DownloadServer(socket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        try {
            //消息接受和发送
            while (true) {
                //接受请求
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String info = br.readLine();
                //心跳包
                //心跳包
                if (info == null) {//数据异常判断客户端是否关闭
                    while(true){
                        socket.sendUrgentData(0xFF);//抛出异常
                    }
                }
                //响应请求
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                JSONObject message = JSON.parseObject(info);
                System.out.println(info);
                int id = message.getInteger("id");
                switch (id) {
                    case 9://证书
                    {
                        logger.debug("case 9");
                        if (server.check_CA(info) == false) {//证书校验失败
                            logger.debug("case 9 证书校验失败");

                            String msg = server.status_message(7);//应用服务器认证失败
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("应用服务器认证失败");
                            socket.shutdownOutput();
                            // Thread.sleep(5000);
                            throw new Exception();
                        } else {
                            logger.debug("case 9 证书校验成功");

                            server.creat_NetDisk();//创建云盘
                            String msg = server.return_CA();//回传认证
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.info("应用服务器认证成功");
                        }
                        logger.debug("case 9结束");

                        break;
                    }
                    case 12: {
                        String msg = null;
                        if ((msg = server.Download_Handler(info)) == null) {
                            msg = server.status_message(14);//
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件下载失败");
                        } else {
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件下载成功");
                        }
                        break;
                    }
                    case 13: {
                        String msg = null;
                        if ((msg = server.Browse_Handler(info)) == null) {
                            msg = server.status_message(15);//
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件浏览失败");
                        } else {
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件浏览成功");
                        }
                        break;
                    }
                    case 14: {
                        String msg = null;
                        if (!server.Delete_Handler(info)) {
                            msg = server.status_message(13);//
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件删除失败");
                        } else {
                            msg = server.status_message(12);//
                            pw.write(msg + "\n");//发送
                            pw.flush();
                            logger.error("文件删除成功");
                        }
                        break;
                    }
                    default:
                        logger.error("异常报文");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(socket.getInetAddress() + "断开连接");
            //e.printStackTrace();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
