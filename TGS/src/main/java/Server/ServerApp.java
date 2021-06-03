package Server;

import java.sql.SQLException;

public class ServerApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SocketServer server=new SocketServer(8888);
        TGS_Server.ConnectToDB();
        server.ServerListener();
    }
}
