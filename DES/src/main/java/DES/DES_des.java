package DES;

import org.apache.log4j.Logger;

import java.util.Base64;
import java.util.Random;

public class DES_des {
    /**
     * log声明
     */
    private static final Logger logger = Logger.getLogger(DES_des.class);

    /**
     * 密钥（string类型）长度 定义成8
     * ( 8 * 8 = 64 )
     */
    private static final int Key_Length = 8;

    /**
     * 构造函数
     */
    public DES_des() {
    }

    /**
     * 生成8位随机长度的string作为密钥
     *
     * @return 生成的Key
     */
    public static String Key_Generator() {
        logger.info("开始生成8位密钥");
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < Key_Length; i++) {
            str.append((char) (random.nextInt() % 128));
        }
        String key = str.toString();
        logger.info("密钥已经生成！密钥为\t" + key);
        return key;
    }

    /**
     * 对文本进行加密
     *
     * @param text 需要加密的文本
     * @param Key  密钥
     * @return 加密后的文本使用base64编码
     */
    public static String Encrypt_Text(String text, String Key) {
        logger.info("开始加密");
        logger.debug("传入明文内容：\t\t" + text);
        logger.debug("传入密钥内容：\t\t" + Key);

        DES Encrypt = new DES(Key);
        byte[] encrypted_Text_byte_array = Encrypt.deal(text.getBytes(), 1);
        String encrypted_Text = Base64.getEncoder().encodeToString(encrypted_Text_byte_array);
        logger.info("加密结束");
        logger.debug("传出密文内容：\t\t" + encrypted_Text);
        return encrypted_Text;
    }

    /**
     * 对密文进行解密
     *
     * @param c   （使用base64编码的）密文
     * @param Key 密钥
     * @return 明文
     */
    public static String Decrypt_Text(String c, String Key) {
        logger.info("开始解密");
        logger.debug("传入密文内容：\t\t" + c);
        logger.debug("解密密钥\t\t密钥为\t" + Key);
        byte[] c_Byte = Base64.getDecoder().decode(c);
        DES Decrypt = new DES(Key);

        byte[] origion_Text = Decrypt.deal(c_Byte, 0);
        logger.info("解密结束");
        logger.debug("传出明文内容：\t\t" + new String(origion_Text));
        return new String(origion_Text);
    }
}
