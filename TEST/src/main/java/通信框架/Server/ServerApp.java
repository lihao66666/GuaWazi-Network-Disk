package 通信框架.Server;

public class ServerApp {
    public static void main(String[] args) {
        SocketServer server=new SocketServer(8888);
        server.ServerListener();
    }
}
