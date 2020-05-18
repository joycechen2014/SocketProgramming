import sun.jvm.hotspot.runtime.Threads;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGUI extends JFrame{
    // To store multiple client
    static List<ClientHandler> ls = new ArrayList<ClientHandler>();
    static int i = 0;

    static File file = new File("questions.txt");

    static java.util.List<String> ques = new ArrayList<>();
    static java.util.List<Integer> ans = new ArrayList<>();
    static Map<String, List<Integer>> user = new HashMap<>();

    private JButton closeButton;
    private JPanel myPanel;
    private JTextArea textArea1;
    private JScrollPane myScroll;

    private JButton uploadButton;
    final JFileChooser  fileDialog = new JFileChooser();
    JFrame frame = new JFrame("Server");
    ServerGUI() {
        frame.setContentPane(myPanel);
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        frame.setPreferredSize(new Dimension(400, 300));
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth / 2 - 400 / 2, screenHeight / 2 - 300 / 2);
        frame.pack();
        frame.setVisible(true);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    textArea1.setText(textArea1.getText() + "File Selected " + file.getName());
                    try{
                        /* Read questions and answers from file*/
                        textArea1.setText("Uploading the file start" +"\n");
                        //Always scroll down
                        textArea1.setCaretPosition(textArea1.getText().length());
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String row = br.readLine();
                        while (row != null && !row.isEmpty()) {
                            String question = "";
                            int correctAns = -1;
                            for (int i = 0; i < 4; i++) {
                                if (row.contains("(correct)")) {
                                    row = row.substring(0, row.length() - 10);
                                    correctAns = i - 1;
                                }
                                question += row + "\n";
                                row = br.readLine();
                            }
                            ques.add(question);
                            ans .add(correctAns);
                            if (row.equalsIgnoreCase("---")) {
                                row = br.readLine();
                                continue;
                            }
                        }
                        textArea1.setText(textArea1.getText() + "Uploading the file finished!\n\n");
                        textArea1.setCaretPosition(textArea1.getText().length());
                    }catch(Exception se) {
                        se.printStackTrace();
                    }
                } else {
                    textArea1.setText("Open command cancelled by user." );
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String arg[])
    {
        ServerGUI frame ;
        try
        {
            frame=new ServerGUI();
            // Server is listening on 3000
            ServerSocket ss = new ServerSocket(1234);
            // For client
            Socket s ;
            // Accept request from client
            while (true) {
                s = ss.accept();
                frame.textArea1.setText(frame.textArea1.getText() + "New client received\n\n");
                System.out.println("New client request received : " +  s);
                // obtain input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                System.out.println("Creating a new handler for client...");
                ServerGUI.ClientHandler client = new ServerGUI.ClientHandler(s, dis,dos,frame);
                Thread thread = new Thread(client);
                System.out.println("Adding this client to active client list");
                ls.add(client);
                thread.start();
                i++;
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(Exception e)
        {JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public static class ClientHandler extends Thread {

        private String name;
        private int score;
        final DataInputStream dis;
        final DataOutputStream dos;
        ServerGUI frame;
        Socket s;

        public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,ServerGUI f) {
            this.dis = dis;
            this.dos = dos;
            this.s = s;
            this.name = null;
            this.score = 0;
            this.frame = f;
        }

        @Override
        public void run() {
            String received;
            while (true) {
                try {
                    received = dis.readUTF();
                    if (received.equals("-1")) {
                        dos.writeUTF("0"+ "#" + ques.get(0));
                    }
                    else if(received.contains("#")) {
                        StringTokenizer st = new StringTokenizer(received, "#");
                        String name = st.nextToken();
                        String SID = st.nextToken();
                        frame.textArea1.setText(frame.textArea1.getText() + name + " has logged in ! \n\n");
                        frame.textArea1.setCaretPosition(frame.textArea1.getText().length());
                        dos.writeUTF("Welcome " + name + " to online quiz");
                        this.name = name;
                        user.put(name,new ArrayList<>());
                    }
                    else if(received.contains(":")){
                        StringTokenizer st = new StringTokenizer(received, ":");
                        String number = st.nextToken();
                        int answer = st.nextToken().charAt(0) - 'A' ;
                        if(answer == ans.get(Integer.parseInt(number))) {
                            this.score += 1;
                        }
                        int next = Integer.valueOf(number) + 1;
                        if(next != ques.size()) {
                            dos.writeUTF(String.valueOf(next) + "#" + ques.get(next));
                        }
                        else {
                            dos.writeUTF("-1" );
                        }

                    }else if(received.equals("OK")) {
                        frame.textArea1.setText(frame.textArea1.getText() + name + " has finished! The total score is " + score + "\n\n");
                        frame.textArea1.setCaretPosition(frame.textArea1.getText().length());
                        System.out.println(this.name + "'s total score is " + score);
                        break;
                    }
                    else {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try
            {
                // closing resources
                this.dis.close();
                this.dos.close();

            }catch(IOException e){
                e.printStackTrace();
            }

        }

    }
}
