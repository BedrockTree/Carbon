package cn.org.bedrocktree.carbon.myswing;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class MyComboBoxUI extends BasicComboBoxUI {
    @Override
    protected JButton createArrowButton() {
        MyJButton button = new MyJButton("","more");
        button.setDefaultBackground(ColorEnum.GREY_35);
        return button;
    }

    @Override
    protected ComboPopup createPopup() {
        return new BasicComboPopup(comboBox){
            @Override
            protected void configureScroller() {
                super.configureScroller();
                scroller.getVerticalScrollBar().setUI(new MyJScrollBarUI());
                scroller.setBackground(ColorEnum.WHITE);
                scroller.getViewport().setBackground(ColorEnum.BLACK);
                scroller.setOpaque(false);
            }
        };
    }
}
