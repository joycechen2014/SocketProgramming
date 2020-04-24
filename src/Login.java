import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class Login extends JFrame implements ActionListener
{

    static File file = new File("record.txt");
    //private static int xLocation = 300;
    //private static int yLocation = 300;

    MySocket s = MySocket.getInstance();
    private JButton SUBMIT;
    private JButton CANCEL;
    private JPanel panel;
    private JLabel label1,label2;
    final JTextField  text1,text2;
    Login() {

        setTitle("LOGIN FORM");
        label1 = new JLabel();
        label1.setText("Name:");
        text1 = new JTextField(15);

        label2 = new JLabel();
        label2.setText("SID:");
        text2 = new JTextField(15);

        SUBMIT=new JButton("SUBMIT");
        CANCEL=new JButton("CANCEL");

        panel=new JPanel(new GridLayout(3,1));
        panel.add(label1);
        panel.add(text1);
        panel.add(label2);
        panel.add(text2);
        panel.add(SUBMIT);
        panel.add(CANCEL);
        add(panel,BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        Listener listener = new Listener(this);
//       // addComponentListener(listener);
//        addWindowListener(listener);
        setVisible(true);
        SUBMIT.addActionListener(this);
        CANCEL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }
    public void actionPerformed(ActionEvent ae)
    {
        String value1=text1.getText();
        String value2=text2.getText();
        if (!value1.isEmpty() && !value2.isEmpty()) {
            this.dispose();
            try {
                s.getDos().writeUTF(value1 + "#"+ value2);
                String response = s.getDis().readUTF();
                //System.out.println(response);
                App page=new App(response);
                dispose();
                page.pack();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            System.out.println("enter the valid username and SID");
            JOptionPane.showMessageDialog(this,"Input name and SJSU ID",
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String arg[])
    {
        try
        {
            Login frame=new Login();
            frame.setSize(300,100);
            int windowWidth = frame.getWidth();
            int windowHeight = frame.getHeight();
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
            System.out.println("x in Login :" + (screenWidth / 2 - windowWidth / 2));
            System.out.println("y in login : " + (screenHeight / 2 - windowHeight / 2));
        }
        catch(Exception e)
        {JOptionPane.showMessageDialog(null, e.getMessage());}
    }

}
