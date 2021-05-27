package 文件操作;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TEST_FILE {
    public static void main(String[] args) {
        File f=new File("/home/lemon/Desktop/瓜娃子云盘/lihao.txt");
        if(f.exists()==false){
            System.out.println("不存在");
            //f.mkdir();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(f));
            String str="lihao";
            try {
                out.write(str.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // String[] str=f.list();
        //List<String> filename=new ArrayList<String>();
       // for(String s:str){
       //     filename.add(s);
       //     System.out.println("\n"+s);
       // }
    }
}
