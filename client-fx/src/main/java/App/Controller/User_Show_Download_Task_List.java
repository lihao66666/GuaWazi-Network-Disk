package App.Controller;

import java.util.ArrayList;

public class User_Show_Download_Task_List {
    private ArrayList<String> file_Name;
    public ArrayList<User_Show_Download_Task_Pane_Singal> child_Pane;
    public ArrayList<Boolean> is_NULL;
    public ArrayList<Boolean> is_Paused;
    public ArrayList<Boolean> is_In_Error;
    private int total;

    public User_Show_Download_Task_List() {
        child_Pane = new ArrayList<User_Show_Download_Task_Pane_Singal>();
        file_Name = new ArrayList<>();
        is_NULL = new ArrayList<>();
        is_Paused = new ArrayList<>();
        is_In_Error = new ArrayList<>();
        total = 0;
    }

    public int get_Total() {
        return this.total;
    }

    public void add_NewFile(String file_Name) {
        if (!this.file_Name.contains(file_Name)) {
            this.file_Name.add(file_Name);
            this.child_Pane.add(new User_Show_Download_Task_Pane_Singal(file_Name));
            this.is_NULL.add(false);
            this.is_Paused.add(false);
            this.is_In_Error.add(false);
            total++;
        }
    }

    public int get_index(String file_Name) {
        return this.file_Name.indexOf(file_Name);
    }


    public String get_File_Name(int index) {
        return file_Name.get(index);
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
            if (is_NULL.get(i) == false) {//任务存在
                if (is_Paused.get(i) == false) {//没有暂停
                    if (is_In_Error.get(i) == false) {//没有错误
                        if (child_Pane.get(i).is_Downloading == false) {//正在下载
                            return i;//第一个未下载的
                        }
                    }
                }
            }
        }
        return -1;//全部下载完成
    }

    public void clear() {
        file_Name = null;
        file_Name = new ArrayList<String>();
        child_Pane = null;
        child_Pane = new ArrayList<>();
        is_NULL = null;
        is_NULL = new ArrayList<>();
        is_In_Error = null;
        is_In_Error = new ArrayList<>();
        is_Paused = null;
        is_Paused = new ArrayList<>();
    }

    public void show_Downloading(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, false);
        this.child_Pane.get(index).show_Downloading();
    }

    public void show_Error_Downloading(int index) {
        this.is_In_Error.set(index, true);
        this.is_Paused.set(index, false);
        this.child_Pane.get(index).show_Error_Downloading();
    }

    public void show_Pause(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, true);
        this.child_Pane.get(index).show_Pause();
    }

    public void show_Restart(int index) {
        this.is_In_Error.set(index, false);
        this.is_Paused.set(index, false);
        this.child_Pane.get(index).show_Restart();
    }

    public void delete(int index) {
        file_Name.set(index, null);
        child_Pane.set(index, null);
        is_NULL.set(index, true);
        is_Paused.set(index, null);
        is_In_Error.set(index, null);
    }
}
