package 通信框架.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread{
    private Socket socket = null;
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        OutputStream os=null;
        PrintWriter pw=null;
        try {
            //消息接受和发送
            while(true){
                //接受请求
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String info =br.readLine();
                //心跳包
               if(info.toString()==null){//数据异常判断客户端是否关闭
                    socket.sendUrgentData(0xFF);//抛出异常
               }
               // while((info=br.readLine())!=null){
                    System.out.println("客户端输出："+info);
              //  }
               // socket.shutdownInput();//接受完毕进行关闭
                //响应请求
                os = socket.getOutputStream();
                pw = new PrintWriter(os);
                Scanner input=new Scanner(System.in);
                //System.out.println("服务端输入：");
                String str=input.next();
                pw.write("你好\n");
                pw.flush();
              //  socket.shutdownOutput();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("客户端断开连接");
            //e.printStackTrace();
        } finally{
            //关闭资源
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
