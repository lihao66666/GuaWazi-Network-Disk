package Client;

import Server.DownloadServer;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) throws InterruptedException {
        Socket socket=null;
        OutputStream os=null;
        PrintWriter pw=null;
        InputStream is=null;
        BufferedReader br=null;
        try {
            // 和服务器创建连接
            socket = new Socket("localhost",8888);
            System.out.println("客户端已启动\n");
            // 要发送给服务器的信息
            int a=2;
            while(--a!=0){
                os = socket.getOutputStream();//字节流(二进制)
                pw = new PrintWriter(os);//字符编码
                DownloadServer server=new DownloadServer(socket.getInetAddress().getHostAddress());
                String str=server.creat_msg9();
                pw.write(str+"\n");
                pw.flush();
                //socket.shutdownOutput();//使用完关闭
                // 从服务器接收的信息
                is = socket.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String info =br.readLine();
                //心跳包
                if(info.toString()==null){//数据异常判断客户端是否关闭
                    socket.sendUrgentData(0xFF);//抛出异常
                }
                //while((info = br.readLine())!=null){
                    System.out.println("服务端输出："+info);
                //}
                String str1=server.creat_msg11();
                pw.write(str1+"\n");
                pw.flush();
                String info1 =br.readLine();
                System.out.println("服务端输出："+info1);
            }
        } catch (Exception e) {
            System.out.println("服务端已经断开连接\n");
            e.printStackTrace();
        }finally {
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
    }
}
