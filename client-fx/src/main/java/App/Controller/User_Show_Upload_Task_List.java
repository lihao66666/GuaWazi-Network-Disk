package App.Controller;

import java.io.File;
import java.util.ArrayList;

class User_Show_Upload_Task_List {
//    private ArrayList<String> file_Name;
    public ArrayList<User_Show_Upload_Task_Pane_Sinal> child_Pane;
    public ArrayList<Boolean> is_NULL;
    public ArrayList<File> file;
    private int total;

    public User_Show_Upload_Task_List() {
        child_Pane = new ArrayList<User_Show_Upload_Task_Pane_Sinal>();
//        file_Name = new ArrayList<>();
        is_NULL = new ArrayList<>();
        file = new ArrayList<>();
        total = 0;
    }

    public int get_Total() {
        return this.total;
    }

    public void add_NewFile(File file) {
        if (!this.file.contains(file)) {
            this.file.add(file);
            this.child_Pane.add(new User_Show_Upload_Task_Pane_Sinal(file.getName()));
            this.is_NULL.add(false);
            total++;
        }
    }

    public int get_index(File file) {
        return this.file.indexOf(file);
    }


    public File get_File(int index) {
        return file.get(index);
    }

    public Boolean file_is_Chosen(int index) {
        return child_Pane.get(index).is_Checked;
    }

    public void uncheck_All() {
        for (int i = 0; i < total; i++) {
            child_Pane.get(i).uncheck();
        }
    }

    public void delete(int index) {
        file.set(index, null);
        child_Pane.set(index, null);
        is_NULL.set(index, true);
    }

    //获取一个未下载的文件
    public int latestFile(){
        for(int i = 0;i<total;i++){
            if(is_NULL.get(i)==false){
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        file = null;
        file = new ArrayList<File>();
        child_Pane = null;
        child_Pane = new ArrayList<>();
        is_NULL = null;
        is_NULL = new ArrayList<>();
    }

}
