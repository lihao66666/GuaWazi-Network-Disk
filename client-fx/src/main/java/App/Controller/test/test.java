package App.Controller.test;//package App.Controller.test;
//
//import App.Controller.EC_Show_ArrayList;
//
//import java.util.Random;
//
//public class test {
//    public static void main(String[] args) {
//        EC_Show_ArrayList test = new EC_Show_ArrayList();
//        for (int q = 0; q < 20; q++) {
//            for (int i = 0; i < 100; i++) {
//                Random r = new Random();
//                if (r.nextBoolean()) {
//                    test.add(true,
//                            r.nextBoolean(),
//                            String.valueOf(i) + "\tDES Key\t\t" + String.valueOf(r.nextLong()),
//                            "",
//                            "",
//                            String.valueOf(i) + "\tDES Origin\t" + String.valueOf(r.nextLong()),
//                            String.valueOf(i) + "\tDES Encrypt\t" + String.valueOf(r.nextLong())
//                    );
//                } else {
//                    test.add(false,
//                            r.nextBoolean(),
//                            "",
//                            String.valueOf(i) + "\tRSA PKey\t" + String.valueOf(r.nextLong()),
//                            String.valueOf(i) + "\tRSA SKey\t" + String.valueOf(r.nextLong()),
//                            String.valueOf(i) + "\tRSA Origin\t" + String.valueOf(r.nextLong()),
//                            String.valueOf(i) + "\tRSA Encrypt\t" + String.valueOf(r.nextLong())
//                    );
//                }
//            }
//            int[] index = test.get_index();
//            System.out.println(String.valueOf(index[0]) + " " +
//                    String.valueOf(index[1]) + " " +
//                    String.valueOf(index[2]) + " " +
//                    String.valueOf(test.get_Total()));
//            int i = 0;
//            test.set_index_0();
//            while (i < test.get_Total()) {
//                index = test.get_index();
//                Boolean is_DES = test.get_is_DES(index[0]);
//                Boolean is_Encrypt = test.get_is_Encrypt(index[0]);
//                if (is_DES) {
//                    String Key = test.get_DES_Key(index[1]);
//                    String original_Text = test.get_original_Text(index[0]);
//                    String encrypt_Text = test.get_encrypt_Text(index[0]);
//                    test.add_index_DES();
//                    System.out.println(index[0] + "\tDES\n" +
//                            index[0] + "\t" + Key + "\n" +
//                            index[0] + "\t" + original_Text + "\n" +
//                            index[0] + "\t" + encrypt_Text + "\n"
//                    );
//                } else {
//                    String PKey = test.get_RSA_Pkey(index[2]);
//                    String SKey = test.get_RSA_Skey(index[2]);
//                    String original_Text = test.get_original_Text(index[0]);
//                    String encrypt_Text = test.get_encrypt_Text(index[0]);
//                    test.add_index_RSA();
//                    System.out.println(index[0] + "\tRSA\n" +
//                            index[0] + "\t" + PKey + "\n" +
//                            index[0] + "\t" + SKey + "\n" +
//                            index[0] + "\t" + original_Text + "\n" +
//                            index[0] + "\t" + encrypt_Text + "\n"
//                    );
//                }
//                i++;
//            }
//            int p = 0;
//        }
//    }
//}
