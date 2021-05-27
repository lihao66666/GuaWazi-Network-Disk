package DES.Example;

import DES.DES_des;
import org.apache.log4j.Logger;

public class DES_TEST {
    private static final Logger logger = Logger.getLogger(DES_TEST.class);

    public static void main(String[] args) {
        DES_des DES = new DES_des();
        //生成Key
        String Key = DES.Key_Generator();
        //明文
        String Origion_Text = "这里是需要加密的String";
        //生成密文
        String encrypted_Text = DES.Encrypt_Text(Origion_Text, Key);
        //解密生成明文
        String decrypted_Text = DES.Decrypt_Text(encrypted_Text, Key);

    }
}
