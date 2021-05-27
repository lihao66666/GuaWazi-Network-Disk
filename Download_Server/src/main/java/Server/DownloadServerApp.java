package Server;

public class DownloadServerApp {
    public static void main(String[] args) {
        SocketServer server=new SocketServer(8888);
        server.ServerListener();
    }
}
