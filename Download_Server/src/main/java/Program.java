import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args)throws Exception{
        System.out.println("HELLO! This is Download_Server!");
        JSONObject Data=new JSONObject();
        Data.put("num",2);
        List<String> filename=new ArrayList<String>();
        filename.add("lihao.txt");
        filename.add("liu.jpg");
        Data.put("filename",filename);
        //Data DEs kc_v加密
        //String data=Data.toJSONString();
        List<String> str=Data.getJSONArray("filename").toJavaList(String.class);
        for(String s:str)
            System.out.println(s);
    }
}
