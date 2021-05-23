package 通信框架.Server;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer1 {
    public static void main(String[] args) {
        try {
            // 创建服务端socket
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("服务端已启动\n");
            //循环监听等待客户端的连接
            while(true){
                // 监听客户端 // 创建客户端socket
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();
                InetAddress address=socket.getInetAddress();
                System.out.println("当前客户端的IP："+address.getHostAddress());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
