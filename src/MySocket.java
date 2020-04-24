import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class MySocket {
    private java.net.Socket s = null;
    private DataInputStream dis = null;
    private static MySocket instance;

    static {
        try {
            instance = new MySocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    private DataOutputStream dos = null;

    public java.net.Socket getS() {
        return s;
    }

    public void setS(java.net.Socket s) {
        this.s = s;
    }

    private MySocket() throws IOException {
        InetAddress ip = InetAddress.getLocalHost();
        int port = 2525;
        // Scanner scanner = new Scanner(System.in);

        s = new java.net.Socket(ip, port);

        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());
    }

    public static MySocket getInstance() {
        return instance;
    }

}
