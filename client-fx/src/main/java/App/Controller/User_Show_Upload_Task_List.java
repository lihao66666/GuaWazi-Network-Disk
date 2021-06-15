package App.Controller;

import java.io.File;
import java.util.ArrayList;

class User_Show_Upload_Task_List {
    //    private ArrayList<String> file_Name;//不是文件名,File类型
    public ArrayList<File> file;
    public ArrayList<User_Show_Upload_Task_Pane_Sinal> child_Pane;
    public ArrayList<Boolean> is_NULL;
    public ArrayList<Boolean> is_Paused;
    public ArrayList<Boolean> is_In_Error;
    public ArrayList<Boolean> is_Complete;
    private int total;

    public User_Show_Upload_Task_List() {
        child_Pane = new ArrayList<User_Show_Upload_Task_Pane_Sinal>();
//        file_Name = new ArrayList<>();
        is_NULL = new ArrayList<>();
        file = new ArrayList<>();
        is_Paused = new ArrayList<>();
        is_In_Error = new ArrayList<>();
        is_Complete = new ArrayList<>();
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
            this.is_Paused.add(false);
            this.is_In_Error.add(false);
            this.is_Complete.add(false);
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

    //获取一个未下载的文件
    public int latestFile() {
        for (int i = 0; i < total; i++) {
            if (!is_NULL.get(i)) {//任务存在
                if (!is_Paused.get(i)) {//没有暂停
                    if (!is_In_Error.get(i)) {//没有错误
                        if (!child_Pane.get(i).is_Uploading) {//正在上传
                            if (!is_Complete.get(i)) {
                                return i;//第一个未下载的
                            }
                        }
                    }
                }
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
        is_In_Error = null;
        is_In_Error = new ArrayList<>();
        is_Paused = null;
        is_Paused = new ArrayList<>();
        is_Complete = null;
        is_Complete = new ArrayList<>();
    }


    public void show_Uploading(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, false);
        this.is_Complete.set(index,false);
        this.child_Pane.get(index).show_Uploading();
    }

    public void show_Restart(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, false);
        this.is_Complete.set(index,false);
        this.child_Pane.get(index).show_Restart();
    }

    public void show_Complete(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, false);
        this.is_Complete.set(index,true);
        this.child_Pane.get(index).show_Complete();
    }

    public void delete(int index) {
        file.set(index, null);
        child_Pane.set(index, null);
        is_NULL.set(index, true);
        is_Paused.set(index, null);
        is_In_Error.set(index, null);
    }

    public void show_Error_Uploading(int index) {
        this.is_In_Error.set(index, true);
        this.is_Paused.set(index, false);
        this.is_Complete.set(index,false);
        this.child_Pane.get(index).show_Error_Uploading();
    }
}
