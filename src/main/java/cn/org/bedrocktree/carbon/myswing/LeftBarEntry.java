package cn.org.bedrocktree.carbon.myswing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LeftBarEntry extends MyJButton{

    public boolean isSelected = false;

    public LeftBarEntry(String value,String imgFileName){
        super(value,imgFileName);
        this.setBorderPainted(false);
        this.setHorizontalAlignment(LEFT);
        this.setPreferredSize(new Dimension(200,77));
        this.setDefaultBackground(ColorEnum.GREY_35);
        this.setForeground(ColorEnum.WHITE);
    }

    @Override
    public void addListeners() {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                LeftBarEntry.this.isSelected = true;
                LeftBarEntry.this.setBackground(ColorEnum.GREY_55);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

                LeftBarEntry.this.setBackground(ColorEnum.GREY_55);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                LeftBarEntry.this.setBackground(oldColor);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                LeftBarEntry.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (!LeftBarEntry.this.isSelected){
                    LeftBarEntry.this.setBackground(ColorEnum.GREY_50);
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if (!LeftBarEntry.this.isSelected){
                    LeftBarEntry.this.setBackground(oldColor);
                }
            }
        });
    }
    public void reset(){
        this.setBackground(oldColor);
        this.isSelected = false;
    }
}
