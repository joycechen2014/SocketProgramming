import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuestionWin extends JFrame {
    static File file = new File("record.txt");
    private static int xLocation ;
    private static int yLocation ;
     String saveValue = null;
    private RadioButtonListener radioButtonListener=new RadioButtonListener();
    private static Socket s = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;
    private JButton nextButton;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;

    private JPanel myPanel;
    private JLabel title;
    JFrame frame = new JFrame("Question");
    QuestionWin(String text) throws IOException {

        s = MySocket.getInstance().getS();

        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());

        String[] question = text.split("#");
        String[] options = question[1].split("\n");
        title.setText(options[0]);
        radioButton1.setText(options[1]);
        radioButton1.addActionListener(radioButtonListener);
        radioButton2.setText(options[2]);
        radioButton2.addActionListener(radioButtonListener);
        radioButton3.setText(options[3]);
        radioButton3.addActionListener(radioButtonListener);
        frame.setPreferredSize(new Dimension(800, 300));
        getlocation();
        System.out.println("get X location " + xLocation);
        System.out.println("get Y location " + yLocation);
        //frame.setLocation(xLocation,yLocation);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        //frame.setLocation(300,300);
        frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);

        System.out.println("repeate X : " +frame.getX());
        System.out.println("repeate Y : " +frame.getY());
        frame.setContentPane(myPanel);
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
//        Listener listener = new Listener(frame);
//        frame.addComponentListener(listener);
//        frame.addWindowListener(listener);
        frame.pack();
        frame.setVisible(true);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"hello");
                try {
                    dos.writeUTF(question[0] + ":" + saveValue);
                    String response = dis.readUTF();
                    //System.out.println(response);
                    if(!response.contains("-1")) {
                        QuestionWin page=new QuestionWin(response);
                        frame.dispose();
                        frame.pack();
                        //page.setVisible(true);
                    } else {
                        Finished finish = new Finished();
                        frame.dispose();
                        frame.pack();
                        finish.setVisible(true);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public class RadioButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            JRadioButton temp=(JRadioButton)arg0.getSource();
            if(temp.isSelected()){
                saveValue=temp.getText();
               // System.out.println(temp.getText());
            }

        }

    }
    //从文件读取坐标信息
    private static void getlocation() {
        if(file.length() == 0) return;
        try{
            FileInputStream fis  = new FileInputStream(file);
            DataInputStream dis =new DataInputStream(fis);
            xLocation = dis.readInt();
            yLocation = dis.readInt();
            System.out.println("xLocationL----------------: " + xLocation);
            System.out.println("yLocation----------------: " + yLocation);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
