package App.Controller;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class User_Show_All_File_Pane_Singal {
    public AnchorPane pane;
    public Boolean is_Checked;
    Label check_Box;
    Label file_Name_Label;
    Separator separator;
    ImageView checked_Image;
    ImageView unchecked_Image;

    User_Show_All_File_Pane_Singal(String file_Name) {
        check_Box = new Label();
        pane = new AnchorPane();
        file_Name_Label = new Label();
        separator = new Separator();

        is_Checked = false;
        pane.setMaxHeight(35d);
        pane.setMinHeight(35d);
        pane.setPrefHeight(35d);
        unchecked_Image = new ImageView(User_Show_All_File_ArrayList.unchecked_Image_URL);
        checked_Image = new ImageView(User_Show_All_File_ArrayList.checked_Image_URL);

        checked_Image.setFitWidth(14);
        checked_Image.setFitHeight(14);
        unchecked_Image.setFitHeight(14);
        unchecked_Image.setFitWidth(14);

        check_Box.setPrefHeight(14);//高度宽度
        check_Box.setPrefWidth(14);//高度宽度
        check_Box.setAlignment(Pos.CENTER_LEFT);
        check_Box.setGraphic(unchecked_Image);
        //checkbox事件
        check_Box.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (is_Checked) {
                    is_Checked = false;
                    check_Box.setGraphic(unchecked_Image);
                } else {
                    is_Checked = true;
                    check_Box.setGraphic(checked_Image);
                }
            }
        });

        //file_Name_Label.setMaxWidth(850d);

        pane.getChildren().add(check_Box);
        pane.setTopAnchor(check_Box, 9d);
        pane.setLeftAnchor(check_Box, 22d);

        file_Name_Label.setText(file_Name);
        Font font = new Font(17);
        file_Name_Label.setFont(font);
        file_Name_Label.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().add(file_Name_Label);
        pane.setTopAnchor(file_Name_Label, 5d);
        pane.setLeftAnchor(file_Name_Label, 62d);
        pane.setRightAnchor(file_Name_Label, 10d);

        separator.setLayoutY(20d);
        separator.setPrefHeight(3d);
        pane.getChildren().add(separator);
        pane.setTopAnchor(separator, 32d);
        pane.setLeftAnchor(separator, 5d);
        pane.setRightAnchor(separator, 0d);
    }

    public void uncheck() {
        is_Checked = false;
        check_Box.setGraphic(unchecked_Image);
    }
}
