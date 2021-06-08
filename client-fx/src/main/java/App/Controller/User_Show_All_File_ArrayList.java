package App.Controller;


import App.Starter;

import java.util.ArrayList;

public class User_Show_All_File_ArrayList {
    public static String checked_Image_URL;
    public static String unchecked_Image_URL;
    private ArrayList<String> file_Name;
    public ArrayList<User_Show_All_File_Pane_Singal> child_Pane;
    private int total;

    public User_Show_All_File_ArrayList() {
        checked_Image_URL = Starter.class.getResource("img/User/check_Box_Ckecked.png").toExternalForm();
        unchecked_Image_URL = Starter.class.getResource("img/User/check_Box_Unckecked.png").toExternalForm();
        child_Pane = new ArrayList<>();
        file_Name = new ArrayList<String>();
        total = 0;
    }

    public int get_Total() {
        return this.total;
    }

    /**
     * 添加
     *
     * @param file_Name 文件名
     */
    public void add_New_File(String file_Name) {
        this.file_Name.add(file_Name);
        child_Pane.add(new User_Show_All_File_Pane_Singal(file_Name));
        total++;
        //return total;//返回当前的位置
    }

    /**
     * 查找位置
     *
     * @param file_Name 文件名
     * @return 位置
     */
    public int get_index(String file_Name) {
        return this.file_Name.indexOf(file_Name);
    }


    public String get_File_Name(int index) {
        return file_Name.get(index);
    }

    /**
     * 文件是否选中
     *
     * @param index 位置
     */
    public Boolean file_is_Chosen(int index) {
        return child_Pane.get(index).is_Checked;
    }

    public void uncheck_All() {
        for (int i = 0; i < total; i++) {
            child_Pane.get(i).uncheck();
        }
    }

    /**
     * 清空
     */
    public void clear() {
        file_Name = null;
        file_Name = new ArrayList<String>();
        child_Pane = null;
        child_Pane = new ArrayList<>();
    }
}
