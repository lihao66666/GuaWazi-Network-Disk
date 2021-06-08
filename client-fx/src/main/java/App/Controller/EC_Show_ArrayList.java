package App.Controller;//package App.Controller;
//
//
//import java.util.ArrayList;
//
//public class EC_Show_ArrayList {
//    private ArrayList<Boolean> is_DES;
//    private ArrayList<Boolean> is_Encrypt;
//    private ArrayList<String> DES_Key;
//    private ArrayList<String> RSA_Pkey;
//    private ArrayList<String> RSA_Skey;
//    private ArrayList<String> original_Text;
//    private ArrayList<String> encrypted_Text;
//    private int index_of_DES;
//    private int index_of_RSA;
//    private int index_of_ALL;
//    private int total;
//
//    public EC_Show_ArrayList() {
//        is_DES = new ArrayList<Boolean>();
//        is_Encrypt = new ArrayList<Boolean>();
//        DES_Key = new ArrayList<String>();
//        RSA_Pkey = new ArrayList<String>();
//        RSA_Skey = new ArrayList<String>();
//        original_Text = new ArrayList<String>();
//        encrypted_Text = new ArrayList<String>();
//        index_of_DES = 0;
//        index_of_RSA = 0;
//        index_of_ALL = 0;
//        total = 0;
//    }
//
//    /**
//     * 添加
//     *
//     * @param is_DES         是否为DES：true DES加密；false RSA加密
//     * @param is_Encrypt     是否为加密的：true 加密；false 解密
//     * @param DES_Key        DES密钥
//     * @param RSA_Pkey       RSA的PK
//     * @param RSA_Skey       RSA的SK
//     * @param original_Text  明文
//     * @param encrypted_Text 密文
//     */
//    public void add(Boolean is_DES, Boolean is_Encrypt, String DES_Key, String RSA_Pkey, String RSA_Skey, String original_Text, String encrypted_Text) {
//        this.is_DES.add(is_DES);
//        this.is_Encrypt.add(is_Encrypt);
//        if (is_DES) {
//            this.DES_Key.add(DES_Key);
//        } else {
//            this.RSA_Pkey.add(RSA_Pkey);
//            this.RSA_Skey.add(RSA_Skey);
//        }
//        this.original_Text.add(original_Text);
//        this.encrypted_Text.add(encrypted_Text);
//        total++;
//    }
//
//    public int[] get_index() {
//        return new int[]{index_of_ALL, index_of_DES, index_of_RSA};
//    }
//
//    public void add_index_DES() {
//        index_of_ALL++;
//        index_of_DES++;
//    }
//
//    public void add_index_RSA() {
//        index_of_ALL++;
//        index_of_RSA++;
//    }
//
//    public void set_index_0() {
//        index_of_ALL = 0;
//        index_of_DES = 0;
//        index_of_RSA = 0;
//    }
//
//    public int get_Total() {
//        return total;
//    }
//
//    public Boolean get_is_DES(int index) {
//        return is_DES.get(index);
//    }
//
//    public Boolean get_is_Encrypt(int index) {
//        return is_Encrypt.get(index);
//    }
//
//    public String get_DES_Key(int index) {
//        return DES_Key.get(index);
//    }
//
//    public String get_RSA_Pkey(int index) {
//        return RSA_Pkey.get(index);
//    }
//
//    public String get_RSA_Skey(int index) {
//        return RSA_Skey.get(index);
//    }
//
//    public String get_original_Text(int index) {
//        return original_Text.get(index);
//    }
//
//    public String get_encrypt_Text(int index) {
//        return encrypted_Text.get(index);
//    }
//}
