package Server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SocketServer.class);
    private int port;
    public SocketServer(int port){
        logger.debug("通信服务已启动");
        this.port=port;
    }
    public void ServerListener() {
        try {
            // 创建服务端socket
            ServerSocket serverSocket = new ServerSocket(port);
            //循环监听等待客户端的连接
            while(true){
                // 监听客户端 // 创建客户端socket
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();
                InetAddress address=socket.getInetAddress();
                logger.info("通信请求,客户端IP"+address.getHostAddress());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
