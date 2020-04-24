import javax.swing.*;
import java.io.*;

public class Record extends Thread {
    private JFrame frame;
    //private JLabel label;
    private int xLocation;
    private int yLocation;
    static File file = new File("record.txt");

    public Record(JFrame frame) {
        this.frame = frame;
        //this.label = label;
    }

    public void run() {
        while (true) {
            updateLocation();
            writeRecord();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //读取横纵坐标
    void getLocation() {
        xLocation = frame.getX();
        yLocation = frame.getY();

    }

    //更新位置
    void updateLocation() {
        getLocation();
    }

    //读入位置
    void writeLocation() throws IOException {
        FileInputStream fis  = new FileInputStream(file);
        DataInputStream dis =new DataInputStream(fis);
        xLocation = dis.readInt();
        yLocation = dis.readInt();

    }


    //记录坐标到文件
    void writeRecord() {
        getLocation();
        try {
            FileOutputStream fos  = new FileOutputStream(file);
            DataOutputStream dos =new DataOutputStream(fos);
            System.out.println("Write X in Record: " + frame.getX());
            System.out.println("Write Y in Record: " + frame.getY());
            dos.writeInt(xLocation);
            dos.writeInt(yLocation);
            dos.flush();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }

}