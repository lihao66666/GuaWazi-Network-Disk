package 通信框架.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws InterruptedException {
        Socket socket=null;
        OutputStream os=null;
        PrintWriter pw=null;
        InputStream is=null;
        BufferedReader br=null;
        try {
            // 和服务器创建连接
            socket = new Socket("192.168.49.45",8888);
            System.out.println("客户端已启动\n");
            // 要发送给服务器的信息
            while(true){
                os = socket.getOutputStream();//字节流(二进制)
                pw = new PrintWriter(os);//字符编码
                Scanner input=new Scanner(System.in);
                System.out.println("客户端输入:");
                String str=input.next();
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
