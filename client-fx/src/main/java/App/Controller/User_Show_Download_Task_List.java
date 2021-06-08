package App.Controller;

import java.util.ArrayList;

public class User_Show_Download_Task_List {
    private ArrayList<String> file_Name;
    public ArrayList<User_Show_Download_Task_Pane_Singal> child_Pane;
    public ArrayList<Boolean> is_NULL;
    private int total;

    public User_Show_Download_Task_List() {
        child_Pane = new ArrayList<User_Show_Download_Task_Pane_Singal>();
        file_Name = new ArrayList<>();
        is_NULL = new ArrayList<>();
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

    public void delete(int index) {
        file_Name.set(index, null);
        child_Pane.set(index, null);
        is_NULL.set(index, true);
    }

    public void clear() {
        file_Name = null;
        file_Name = new ArrayList<String>();
        child_Pane = null;
        child_Pane = new ArrayList<>();
        is_NULL = null;
        is_NULL = new ArrayList<>();
    }

}
