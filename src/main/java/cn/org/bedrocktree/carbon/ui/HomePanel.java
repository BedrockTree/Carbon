package cn.org.bedrocktree.carbon.ui;

import cn.org.bedrocktree.carbon.myswing.ColorEnum;
import cn.org.bedrocktree.carbon.myswing.MyComboBoxUI;
import cn.org.bedrocktree.carbon.myswing.MyJButton;
import cn.org.bedrocktree.carbon.myswing.MyListCellRenderer;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JPanel launchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
    private final MyJButton launch = new MyJButton("Launch","launch");
    private final JComboBox<String> versionChooser = new JComboBox<String>(new String[]{"1.12.2"});
    private final JLabel version = new JLabel("Game Version");

    public HomePanel(){
        super(new BorderLayout(0,0));
        this.version.setForeground(ColorEnum.WHITE);
        this.launchBar.add(version);
        this.setSize(600,480);
        this.add(launchBar,BorderLayout.SOUTH);
        this.setBackground(ColorEnum.GREY_75);
        this.launchBar.setSize(500,60);
        this.add(launchBar,BorderLayout.SOUTH);
        this.launchBar.add(versionChooser);
        this.launchBar.add(launch);
        this.launchBar.setBackground(ColorEnum.GREY_50);
        this.launchBar.setPreferredSize(new Dimension(500,60));
        this.launchBar.setForeground(ColorEnum.WHITE);
        this.launch.setSize(70,50);
        this.launch.setDefaultBackground(ColorEnum.GREY_35);
        this.launch.setPreferredSize(new Dimension(150,40));
        this.launch.setBorderPainted(true);
        this.launch.setBorder(BorderFactory.createLineBorder(ColorEnum.GREY_25,1,true));
        this.versionChooser.setBackground(ColorEnum.GREY_55);
        this.versionChooser.setPreferredSize(new Dimension(320,35));
        this.versionChooser.setBorder(BorderFactory.createLineBorder(ColorEnum.GREY_25,1,true));
        this.versionChooser.setForeground(ColorEnum.WHITE);
        this.versionChooser.setFocusable(false);
        this.versionChooser.setEditable(false);
        this.versionChooser.setToolTipText("Choose a Game Version");
        this.versionChooser.setUI(new MyComboBoxUI());
        this.versionChooser.setRenderer(new MyListCellRenderer());
        this.launch.addActionListener(e -> {

        });
    }
}
