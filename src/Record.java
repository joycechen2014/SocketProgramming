import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

class Record
{
    JFrame frame;
    Record(JFrame f) {
        frame = f;
    }

    public void setLocation() {
        try
        {
            MyThreadData.getThreadInstance().setData(new Random().nextInt());
            FileInputStream fin=new FileInputStream("jf.dat");
            ObjectInputStream oin=new ObjectInputStream(fin);

            JFData jf=(JFData)oin.readObject();
            frame.setLocation(jf.x,jf.y);
            frame.setSize(jf.size);

            oin.close();
            fin.close();
        }catch(Exception e){}
    }

    public void recordLocation() {
        try
        {

            FileOutputStream fout=new FileOutputStream("jf.dat");
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
}