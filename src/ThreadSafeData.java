import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadSafeData {

    private static Map<Thread, String> map = new HashMap<Thread, String>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    System.out.println("this is " + Thread.currentThread().getName() + "---" + data);
                    String file = "jf" + String.valueOf(data) + ".dat";
                    map.put(Thread.currentThread(), "file");
//                    new A().getDate();
//                    new B().getDate();
                }
            }).start();
        }
    }
}
