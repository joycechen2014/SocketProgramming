import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Finished extends JDialog {
    static File file = new File("record.txt");
    private static int xLocation ;
    private static int yLocation ;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public Finished()  {
        getlocation();
        //Finished dialog = new Finished();
        setSize(260,140);
        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        this.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws IOException {
        Socket s =  MySocket.getInstance().getS();
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF("OK");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    //从文件读取坐标信息
    private static void getlocation() {
        if(file.length() == 0) return;
        try{
            FileInputStream fis  = new FileInputStream(file);
            DataInputStream dis =new DataInputStream(fis);
            xLocation = dis.readInt();
            yLocation = dis.readInt();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
