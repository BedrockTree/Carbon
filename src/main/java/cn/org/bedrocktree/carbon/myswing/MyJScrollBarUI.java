package cn.org.bedrocktree.carbon.myswing;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyJScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = ColorEnum.GREY_75;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton jButton = super.createIncreaseButton(orientation);
        jButton.setBackground(ColorEnum.GREY_55);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        jButton.setBorderPainted(false);
        return jButton;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton jButton = super.createDecreaseButton(orientation);
        jButton.setBackground(ColorEnum.GREY_55);
        jButton.setBorder(BorderFactory.createEmptyBorder());
        jButton.setBorderPainted(false);
        return jButton;
    }
}
