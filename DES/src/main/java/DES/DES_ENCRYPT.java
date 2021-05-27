package DES;

import java.util.Base64;
import java.util.Random;

import org.apache.log4j.Logger;


public class DES_ENCRYPT {
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
     * 生成Key
     */
    public DES_ENCRYPT() {
        Key_Generator(); //首先自动生成Key
    }

    /**
     * 生成8位随机长度的string作为密钥
     */
    private void Key_Generator() {
        logger.info("开始生成8位密钥");
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < Key_Length; i++) {
            str.append((char) (random.nextInt() % 128));
        }
        Key = str.toString();
        logger.info("密钥已经生成！密钥为\t" + Key);
    }

    /**
     * 获取私有成员函数Key
     *
     * @return 私有成员函数Key
     */
    public String get_Key() {
        return Key;
    }


    /**
     * 设置Key
     *
     * @param key 需要设置的Key
     */
    public void set_Key(String key) {
        Key = key;
    }

    /**
     * 对文本进行加密
     *
     * @param text 需要加密的文本
     * @return 加密后的文本使用base64编码
     */
    public String Encrypt_Text(String text) {
        logger.info("开始加密");
        logger.debug("传入明文内容：\t\t" + text);
        DES Encrypt = new DES(Key);
        byte[] encrypted_Text_byte_array = Encrypt.deal(text.getBytes(), 1);
        String encrypted_Text = Base64.getEncoder().encodeToString(encrypted_Text_byte_array);
        logger.info("加密结束");
        logger.debug("传出密文内容：\t\t" + encrypted_Text);
        return encrypted_Text;
    }

}
