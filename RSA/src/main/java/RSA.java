

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.util.Base64;

public class RSA {
    public String Encrypt(String inputStr, BigInteger e,BigInteger n){
        byte[] inputStrBytes=inputStr.getBytes();
        BigInteger inputStrBigInteger=new BigInteger(inputStrBytes);
        BigInteger result=inputStrBigInteger.modPow(e,n);
        byte[] resultBytes=result.toByteArray();
        String outputStr= Base64.getEncoder().encodeToString(resultBytes);
        return outputStr;
    }

    public String Dencrypt(String inputStr, BigInteger d,BigInteger n){
        byte[] inputStrBytes=Base64.getDecoder().decode(inputStr);
        BigInteger inputStrBigInteger=new BigInteger(inputStrBytes);
        BigInteger result=inputStrBigInteger.modPow(d,n);
        byte[] resultBytes=result.toByteArray();
        String outputStr=new String(resultBytes);
        return outputStr;
    }

    public void Generate() throws IOException {
        GenerateKey generateKey=new GenerateKey();
        generateKey.Generate();
        File f = new File("target/RSA_Key.txt");
        if(!f.exists()){
        FileOutputStream fop = new FileOutputStream(f);
        // 构建FileOutputStream对象,文件不存在会自动新建

        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk

        writer.append(generateKey.getN().toString());
        // 写入到缓冲区

        writer.append("\r\n");
        // 换行

        writer.append(generateKey.getE().toString());
        // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
            writer.append("\r\n");
            // 换行

            writer.append(generateKey.getD().toString());
        writer.close();
        // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉

        fop.close();
        }
        else{
            System.out.println("密钥已存在！");
        }
    }

    @Test
    public void test() throws IOException {
        //生成密钥
        GenerateKey generateKey=new GenerateKey();
        generateKey.Generate();

        //加密
        String str="123123";
        String E=Encrypt(str,generateKey.getE(),generateKey.getN());

        //解密
        String D=Dencrypt(E,generateKey.getD(),generateKey.getN());

        System.out.println("E = " + E);
        System.out.println("D = " + D);

        //生成本地密钥
        //Generate();
    }
}
