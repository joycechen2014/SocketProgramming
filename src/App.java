import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class App extends JFrame{
    static File file = new File("record.txt");
    private static int xLocation ;
    private static int yLocation ;

    private static Socket s = null;
    private  DataInputStream dis = null;
    private  DataOutputStream dos = null;


    private JButton submit;
    ButtonGroup group = new ButtonGroup();

    public JPanel panelMain;
    private JLabel lable;

    JFrame frame = new JFrame("App");

    public  App(String text) throws IOException {
            s = MySocket.getInstance().getS();
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            lable.setText(text);
            frame.setContentPane(panelMain);
            frame.setPreferredSize(new Dimension(400, 300));
        frame.setSize(400,300);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);

        System.out.println("x in APP :" + (screenWidth / 2 - windowWidth / 2));
        System.out.println("y in APP : " + (screenHeight / 2 - windowHeight / 2));
            frame.pack();
            frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
              frame.setVisible(true);

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"hello");
                try {
                    dos.writeUTF( "-1");
                    String response = dis.readUTF();
                    //System.out.println(response);
                    QuestionWin page=new QuestionWin(response);
                    frame.dispose();
                    page.pack();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


    }

    //从文件读取坐标信息
    private static void getlocation() {
        if(file.length() == 0) return;
        try{
            FileInputStream fis  = new FileInputStream(file);
            DataInputStream dis =new DataInputStream(fis);
            xLocation = dis.readInt();
            yLocation = dis.readInt();
            System.out.println("Read x location" + xLocation);
            System.out.println("Read y location" + yLocation);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
