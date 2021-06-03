package RSA;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RSA {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RSA.class);

    public static String Encrypt(String inputStr, BigInteger e, BigInteger n) {//加密函数，输入参数：需加密字符串，大数e，大数n
        inputStr=Base64.getEncoder().encodeToString(inputStr.getBytes(StandardCharsets.UTF_8));
        byte[] inputStrBytes = inputStr.getBytes();//将输入的字符串转换为byte数组
        BigInteger inputStrBigInteger = new BigInteger(inputStrBytes);//将字符串的字节流转化为大数
        BigInteger result = inputStrBigInteger.modPow(e, n);//加密
        byte[] resultBytes = result.toByteArray();//将加密结果转换回字节数组
        String outputStr = Base64.getEncoder().encodeToString(resultBytes);//Base64编码处理结果字节数组为字符串
        return outputStr;
    }

    public static String Decrypt(String inputStr, BigInteger d, BigInteger n) {//解密函数，输入参数：需解密字符串，大数d，大数n
        logger.debug("RSA开始解密");
        byte[] inputStrBytes = Base64.getDecoder().decode(inputStr);//将输入的字符串使用Base64解码为字节数组
        logger.debug("输出数组："+inputStrBytes);
        BigInteger inputStrBigInteger = new BigInteger(inputStrBytes);//将该字节数组转换为大数
        logger.debug("输出大数："+inputStrBigInteger);
        BigInteger result = inputStrBigInteger.modPow(d, n);//解密
        logger.debug("解密结果："+result);
        byte[] resultBytes = result.toByteArray();//将解密结果转换回字节数组
        logger.debug("转回数组："+resultBytes);
        String outputStr = new String(resultBytes);//将字节数组转换为字符串返回
        logger.debug("outputStr="+outputStr);
        outputStr=new String(Base64.getDecoder().decode(outputStr));
        logger.debug("outputStr="+outputStr);
        return outputStr;
    }

    @Test
    public void test3(){
        String str="是saiohdioashd oiho3qh1速度从年四大哈斯殴打后ishdiohqio32哦3红豆红赛欧的哈斯殴打hi哦2好可oh大搜is好哦可啊哈迪欧hi哦吼可阿卡活塞";
        GenerateKey generateKey=new GenerateKey();
        generateKey.Generate();
        String E=Encrypt(str,generateKey.getE(),generateKey.getN());
        String D=Decrypt(E,generateKey.getD(),generateKey.getN());
        System.out.println("D = " + D);
    }

    public static void Generate(String type,String ID) throws IOException {//生成密钥文件
        GenerateKey generateKey = new GenerateKey();//实例化一个GenerateKey的对象
        generateKey.Generate();//调用对象中的公有函数
        File f = new File(type+"/target/"+ID+"_RSA_Key.txt");
        if (!f.exists()) {//如果要打开的文件不存在
            FileOutputStream fop = new FileOutputStream(f);
            // 构建FileOutputStream对象,文件不存在会自动新建
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            JSONObject RSA=new JSONObject();
            JSONObject PK=new JSONObject();
            JSONObject SK=new JSONObject();
            PK.put("n",generateKey.getN().toString());
            PK.put("e",generateKey.getE().toString());
            String pk=PK.toJSONString();
            SK.put("n",generateKey.getN().toString());
            SK.put("d",generateKey.getD().toString());
            String sk=SK.toJSONString();
            RSA.put("PK",pk);
            RSA.put("SK",sk);
            String rsa=RSA.toJSONString();
            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
            writer.append(rsa);
            //writer.append(Base64.getEncoder().encodeToString(rsa.getBytes(StandardCharsets.UTF_8)));//将n转换为Base64编码写入到缓冲区
            // 写入到缓冲区
            writer.append("\r\n");// 换行
            // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
            writer.close();
            // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
            fop.close();
        } else {
            System.out.println("密钥已存在！");
        }
    }

    @Test
    public void test1() throws IOException {
        Generate(".","KDC");
    }

    public static Boolean GenerateCA(String type,String ID) throws IOException {//生成证书文件，输入参数：用户ID
        BigInteger KDC_n;//设置变量存放KDC私钥中的n
        BigInteger KDC_e;//设置变量存放KDC私钥中的e
        BigInteger KDC_d;
        File ca_file = new File(type+"/target/"+ID+"_CA.txt");
        if (!ca_file.exists()) {//如果证书文件不存在
            File kdc_file = new File(type+"/target/KDC_Key.txt");
            if (kdc_file.exists()) {
                FileInputStream kdc_fip = new FileInputStream(kdc_file);
                InputStreamReader kdc_reader = new InputStreamReader(kdc_fip, "UTF-8");
                StringBuffer kdc_sb = new StringBuffer();
                while (kdc_reader.ready()) {
                    kdc_sb.append((char) kdc_reader.read());
                }
                String str = kdc_sb.toString();
                JSONObject KDC_obj=JSON.parseObject(str);
                String pk=KDC_obj.getString("PK");
                String sk=KDC_obj.getString("SK");
                JSONObject PK_obj=JSON.parseObject(pk);
                JSONObject SK_obj=JSON.parseObject(sk);
                KDC_e=new BigInteger(PK_obj.getString("e"));
                KDC_d =new BigInteger(SK_obj.getString("d"));
                KDC_n=new BigInteger(PK_obj.getString("n"));

                logger.debug("kDC_n="+KDC_n);
                logger.debug("KDC_e"+KDC_e);
                logger.debug("KDC_d"+KDC_d);

                kdc_reader.close();
                kdc_fip.close();
            } else {
                logger.error("没有找到KDC密钥");
                System.out.println("没有找到KDC密钥");
                return false;
            }
            File rsa_file = new File(type+"/target/"+ID+"_RSA_Key.txt");
            if (!rsa_file.exists()) {//如果密钥文件不存在
                logger.error("密钥文件不存在");
                Generate(type,ID);//生成密钥文件
            }
            FileInputStream fip = new FileInputStream(rsa_file);// 构建FileInputStream对象
            InputStreamReader reader = new InputStreamReader(fip, "UTF-8");// 构建InputStreamReader对象,编码与写入相同
            StringBuffer rsa_sb = new StringBuffer();
            while (reader.ready()) {
                rsa_sb.append((char) reader.read());// 转成char加到StringBuffer对象中
            }
            String str = rsa_sb.toString();
            JSONObject obj=JSON.parseObject(str);
            String PK= obj.getString("PK");
            reader.close();// 关闭读取流
            fip.close();// 关闭输入流,释放系统资源

            JSONObject CA_obj=new JSONObject();
            CA_obj.put("user_ID",ID);
            CA_obj.put("PK",PK);
            String ca_str=CA_obj.toJSONString();
            logger.debug("cA==="+ca_str);
            String CA = RSA.Encrypt(ca_str, KDC_e, KDC_n);
            logger.debug("CA====="+CA);
            FileOutputStream fop = new FileOutputStream(ca_file);
            // 构建FileOutputStream对象,文件不存在会自动新建
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
            writer.append(CA);// 写入到缓冲区
            logger.debug("写入缓冲区");
            // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
            writer.close();
            // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
            fop.close();
            return true;
        }
        else{
            System.out.println("证书文件已存在");
            return false;
        }
    }

    @Test
    public void test2() throws IOException {
        GenerateCA(".","client1");
    }

    @Test
    public void test() throws IOException {
       /* //生成密钥
        RSA.RSA.GenerateKey generateKey=new RSA.RSA.GenerateKey();
        generateKey.Generate();

        //加密
        String str="123123";
        String E=Encrypt(str,generateKey.getE(),generateKey.getN());

        //解密
        String D=Dencrypt(E,generateKey.getD(),generateKey.getN());

        System.out.println("E = " + E);
        System.out.println("D = " + D);*/

        //生成本地密钥
        //Generate();
        GenerateCA(".","as1");

    }
}
