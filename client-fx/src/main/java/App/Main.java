package App;

import javafx.application.Application;

public class Main {
    public static int AS_Port = 8888;
    public static String AS_IP = "localhost";
    public static String TGS_ID = "TGS1";
    public static String  ticket_TGS = "";

    public static void main(String[] args) {
        Application.launch(Starter.class);
    }
}
