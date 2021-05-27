package DES;

import org.apache.log4j.Logger;

import java.util.Base64;

public class DES_DECRYPT {
    /**
     * log声明
     */
    private static final Logger logger = Logger.getLogger(DES_ENCRYPT.class);

    /**
     * 密钥（string类型）长度 定义成8
     * ( 8 * 8 = 64 )
     */
    private static final int Key_Length = 8;

    /**
     * 密钥（私有，通过get_Key获取）
     */
    private static String Key;

    /**
     * 构造函数
     *
     * @param key 密钥
     */
    public DES_DECRYPT(String key) {
        Key = key;
    }

    /**
     * 设置私有变量密钥
     *
     * @param key 密钥
     */
    public static void set_Key(String key) {
        Key = key;
    }

    /**
     * 对密文进行解密
     *
     * @param c （使用base64编码的）密文
     * @return 明文
     */
    public String Decrypt_Text(String c) {
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
