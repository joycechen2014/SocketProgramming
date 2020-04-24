import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    // To store multiple client
    static List<ClientHandler> ls = new ArrayList<ClientHandler>();
    static int i = 0;

    static File file = new File("questions.txt");

    //Store the questions
    static List<String> ques = new ArrayList<>();
    //Store the answer
    static List<Integer> ans = new ArrayList<>();

    public static void main(String[] args) {
        //Get the question and answer from the question.txt
        try{
            BufferedReader  br = new BufferedReader(new FileReader(file));
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
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }


        try{

            ServerSocket ss = new ServerSocket(2525);
            Socket s ;
            // Accept request from client
            while (true) {
                s = ss.accept();
                System.out.println("New client request received : " +  s);
                // obtain input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                System.out.println("Creating a new handler for client...");
                ClientHandler client = new ClientHandler(s, dis,dos);
                Thread thread = new Thread(client);
                System.out.println("Adding this client to active client list");
                ls.add(client);
                thread.start();
                i++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    public static class ClientHandler implements Runnable {

        private String name;
        private int score;
        final DataInputStream dis;
        final DataOutputStream dos;
        Socket s;


        public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
            this.dis = dis;
            this.dos = dos;
            this.s = s;
            this.name = null;
            this.score = 0;
        }

        @Override
        public void run() {
            String received;

            while (true) {
                try {
                    received = dis.readUTF();
                    System.out.println(received);
                    // "-1" means start
                    if (received.equals("-1")) {
                        dos.writeUTF("0"+ "#" + ques.get(0));
                    }
                    else if(received.contains("#")) {
                        StringTokenizer st = new StringTokenizer(received, "#");
                        String name = st.nextToken();
                        String SID = st.nextToken();
                        dos.writeUTF("Welcome " + name + " to online quiz");
                        this.name = name;
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
                         System.out.println(this.name + "'s total score is " + score);
                    }
                     else {
                         break;
                    }

//                    for (ClientHandler client : Server.ls) {
//                        System.out.println(client.name);
//                        if (client.name.equals(recipient) && client.isloggedin == true) {
//                            client.dos.writeUTF(this.name + " : " + MsgToSend);
//                            break;
//                        }
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }


        }

    }

}
