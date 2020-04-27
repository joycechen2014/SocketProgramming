import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App extends JFrame{
    private static Map<Thread,Integer> dataMap = new HashMap<Thread,Integer>();
    private static Socket s = null;
    private  DataInputStream dis = null;
    private  DataOutputStream dos = null;


    private JButton submit;
    ButtonGroup group = new ButtonGroup();

    public JPanel panelMain;
    private JLabel lable;

    JFrame frame = new JFrame("App");
    Record myRecord;
    public  App(String text,String name) throws IOException {
        myRecord = new Record(frame,name);
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
        //frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
        myRecord.setLocation();
            frame.pack();
            frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
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
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null,"hello");
                try {
                    dos.writeUTF( "-1");
                    String response = dis.readUTF();
                    //System.out.println(response);
                    QuestionWin page=new QuestionWin(response,name);
                    page.pack();
                    frame.dispose();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


    }


}
