package Server.Show.controller;

import java.util.LinkedList;
import java.util.Queue;

public class EC_Show_Queue {
    private Queue<Boolean> is_DES;
    private Queue<Boolean> is_Encrypt;
    private Queue<String> DES_Key;
    private Queue<String> RSA_PKey;
    private Queue<String> RSA_SKey;
    private Queue<String> original_Text;
    private Queue<String> encrypted_Text;
    private int left;

    public EC_Show_Queue() {
        is_DES = new LinkedList<>();
        is_Encrypt = new LinkedList<Boolean>();
        DES_Key = new LinkedList<String>();
        RSA_PKey = new LinkedList<String>();
        RSA_SKey = new LinkedList<String>();
        original_Text = new LinkedList<String>();
        encrypted_Text = new LinkedList<String>();
        left = 0;
    }

    public void appendent(Boolean is_DES, Boolean is_Encrypt, String DES_Key, String RSA_Pkey, String RSA_Skey, String original_Text, String encrypted_Text) {
        this.is_DES.offer(is_DES);
        this.is_Encrypt.offer(is_Encrypt);
        if (is_DES) {
            this.DES_Key.offer(DES_Key);
            this.RSA_PKey.offer("");
            this.RSA_SKey.offer("");
        } else {
            this.DES_Key.offer("");
            this.RSA_PKey.offer(RSA_Pkey);
            this.RSA_SKey.offer(RSA_Skey);
        }
        this.original_Text.add(original_Text);
        this.encrypted_Text.add(encrypted_Text);

        left++;
    }

    public int get_Left() {
        return this.left;
    }

    public String[] get() {

        return new String[]{String.valueOf(is_DES.poll()), String.valueOf(is_Encrypt.poll()), DES_Key.poll(), RSA_PKey.poll(), RSA_SKey.poll(), original_Text.poll(), encrypted_Text.poll()};
    }

}
