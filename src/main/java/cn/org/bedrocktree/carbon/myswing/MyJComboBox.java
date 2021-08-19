package cn.org.bedrocktree.carbon.myswing;

import javax.swing.*;
import java.awt.*;

public class MyJComboBox<E> extends JComboBox<E> {

    public MyJComboBox(){
        super();
        init();
    }
    public MyJComboBox(E[] contents){
        super(contents);
        init();
    }
    public void init(){
        this.setForeground(ColorEnum.WHITE);
        this.setFocusable(false);
        this.setEditable(false);
        this.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel result = new JLabel("  "+value,JLabel.LEFT);
            result.setOpaque(true);
            result.setBackground(isSelected?ColorEnum.GREY_70 :ColorEnum.GREY_50);
            result.setForeground(ColorEnum.WHITE);
            result.setPreferredSize(new Dimension(320,25));
            result.setSize(320,25);
            return result;
        });
    }

}
