package cn.org.bedrocktree.carbon.myswing;

import cn.org.bedrocktree.carbon.utils.StreamUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

public class MyJButton extends JButton {

    Color oldColor = MyJButton.this.getBackground();

    Color mousePressedColor = ColorEnum.GREY_55;

    Color mouseEnteredColor = ColorEnum.GREY_50;

    public MyJButton(){
        super();
        init();
    }
    public MyJButton(String value){
        super(value);
        init();
    }
    public MyJButton(String value,String imgFileName){
        super(value);
        try {
            this.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(LeftBarEntry.class.getResource("/icon/"+imgFileName+".png"))).getScaledInstance(23,23,Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }
    public void init(){

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setUI(new BasicButtonUI());
        this.setForeground(ColorEnum.WHITE);
        this.addListeners();
    }

    public void setMousePressedColor(Color mousePressedColor) {
        this.mousePressedColor = mousePressedColor;
    }

    public void setMouseEnteredColor(Color mouseEnteredColor) {
        this.mouseEnteredColor = mouseEnteredColor;
    }

    public void addListeners(){
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

                MyJButton.this.setBackground(mousePressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                MyJButton.this.setBackground(oldColor);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                MyJButton.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                MyJButton.this.setBackground(mouseEnteredColor);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                MyJButton.this.setBackground(oldColor);
            }
        });
    }


    public void setDefaultBackground(Color color){
        setBackground(color);
        oldColor = color;
    }
}
