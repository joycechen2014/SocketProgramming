import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

class Record
{
    JFrame frame;
    String file;
    Record(JFrame f,String name) {
        frame = f;
        file = name;

    }

    public void setLocation() {
        try
        {
            String file_name = file + ".dat";
            FileInputStream fin=new FileInputStream(file_name);
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