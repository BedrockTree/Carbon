package cn.org.bedrocktree.carbon.myswing;

import cn.org.bedrocktree.carbon.Carbon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyJFrame extends JFrame {


    public static final URL imgURL = MyJFrame.class.getResource("/icon/icon.png");

    private int oldX = 0,oldY = 0;

    public MyJFrame() throws IOException {

        this.setBackground(ColorEnum.GREY_75);
        this.setLayout(new BorderLayout());
        this.setTitle(Carbon.APPLICATION_NAME);
        this.setIconImage(getImgIcon());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);

    }

    public Image getImgIcon() throws IOException {
        assert imgURL != null;
        InputStream input = imgURL.openStream();
        Image img = ImageIO.read(input);
        return img;
    }

}

