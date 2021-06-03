package Server;

import java.sql.SQLException;

public class ServerApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SocketServer server=new SocketServer(8888);
        AS_Server.ConnectToDB();
        server.ServerListener();
    }
}
