package DES.Example;

import DES.DES_DECRYPT;
import DES.DES_ENCRYPT;
import org.apache.log4j.Logger;

public class DES_TEST {
    private static final Logger logger = Logger.getLogger(DES_TEST.class);

    public static void main(String[] args) {
        //加密过程
        DES_ENCRYPT Encrypt_Text = new DES_ENCRYPT();//构造函数中自动生成密钥
        //获取密钥
        String Key = Encrypt_Text.get_Key();
        String text = "需要加密的文字";
        //进行加密
        String en_text = Encrypt_Text.Encrypt_Text(text);

        //解密
        DES_DECRYPT Decrypt_Text = new DES_DECRYPT(Key);//构造函数参数为密钥
        String de_text = Decrypt_Text.Decrypt_Text(en_text);
    }
}
