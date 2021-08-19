package cn.org.bedrocktree.carbon.myswing;

import javax.swing.*;
import java.awt.*;

public class MyListCellRenderer implements ListCellRenderer<String> {
    @Override
    public Component getListCellRendererComponent(JList<? extends String> jList, String s, int i, boolean b, boolean b1) {
        JLabel result = new JLabel("  "+s,JLabel.LEFT);
        result.setOpaque(true);
        result.setBackground(b?ColorEnum.GREY_70 :ColorEnum.GREY_55);
        result.setForeground(ColorEnum.WHITE);
        result.setPreferredSize(new Dimension(320,25));
        result.setSize(320,25);
        return result;
    }
}
