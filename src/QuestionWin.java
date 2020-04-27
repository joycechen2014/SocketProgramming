import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class QuestionWin extends JFrame {

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
    JFrame frame;
    Record myRecord;
    QuestionWin(String text,String name) throws IOException {
        frame = new JFrame(name + "'s questions");
        myRecord = new Record(frame,name);
        frame.setContentPane(myPanel);
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

        //frame.setLocation(xLocation,yLocation);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        myRecord.setLocation();
        //frame.setLocation(screenWidth / 2 - 800 / 2, screenHeight / 2 - 300 / 2);


        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));

        frame.pack();
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent ce)
            {

                try
                {
                    String file_name = name + ".dat";

                    FileOutputStream fout=new FileOutputStream(file_name);
                    ObjectOutputStream out=new ObjectOutputStream(fout);

                    JFData jf=new JFData();

                    jf.x=frame.getLocation().x;
                    jf.y=frame.getLocation().y;
                    jf.size=frame.getSize();
                    out.writeObject(jf);

                    out.close();
                    fout.close();

                }catch(Exception e){}

            }

        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"hello");
                try {
                    dos.writeUTF(question[0] + ":" + saveValue);
                    String response = dis.readUTF();
                    if(!response.contains("-1")) {
                        //frame.pack();
                        frame.dispose();
                        QuestionWin page=new QuestionWin(response,name);
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


}
