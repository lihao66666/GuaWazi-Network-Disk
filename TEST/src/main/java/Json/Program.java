package Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        JSONObject object = new JSONObject();
        //string
        object.put("string","string");
        //int
        object.put("int",2);
        //boolean
        object.put("boolean",true);
        //array
        List<Integer> integers = Arrays.asList(1,2,3);
        object.put("list",integers);
        //null
        object.put("null",null);
        //转换为String
        String str=object.toJSONString();
        System.out.println(str);
        //转换为Json
        JSONObject test= JSON.parseObject(str);
        //获取字段
        System.out.println(test.get("string"));
    }
}
