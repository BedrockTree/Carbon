package cn.org.bedrocktree.carbon.myswing;

import cn.org.bedrocktree.carbon.ui.MainWindow;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class BrowserDialog extends JDialog {

    public static final URL imgURL = BrowserDialog.class.getResource("/icon/icon.png");

    private static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    private static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final URL closeIconURL = MainWindow.class.getResource("/icon/close.png");

    private final Container container = this.getContentPane();

    private final JLabel logo = new JLabel();

    private final JPanel topBar = new JPanel(new BorderLayout(0,0));

    private final JPanel control = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));

    private final JLabel title =  new JLabel("Login",JLabel.CENTER);

    private final JFXPanel browser = new JFXPanel();

    private int oldX = 0,oldY = 0;

    private final MyJButton close = new MyJButton();

    public BrowserDialog() throws IOException {
        this.setIconImage(getImgIcon(imgURL));
        this.setSize(406,406);
        this.control.setSize(406,20);
        this.topBar.setSize(406,40);
        this.topBar.setPreferredSize(new Dimension(406,406));
        this.container.setLayout(new BorderLayout(0,0));
        this.close.setMargin(new Insets(10,10,10,10));
        this.logo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.logo.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(imgURL)).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        this.close.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(closeIconURL)).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        this.close.setDefaultBackground(ColorEnum.GREY_25);
        this.close.setPreferredSize(new Dimension(40,40));
        this.title.setForeground(ColorEnum.WHITE);
        this.title.setPreferredSize(new Dimension(1300,20));
        //this.control.add(close);
        this.topBar.setBackground(ColorEnum.GREY_25);
        this.control.setBackground(ColorEnum.GREY_25);
        //this.topBar.add(logo,BorderLayout.WEST);
        //this.topBar.add(title,BorderLayout.CENTER);
        //this.topBar.add(control,BorderLayout.EAST);
        this.genJFX();
        //this.container.add(topBar,BorderLayout.NORTH);
        this.container.add(browser,BorderLayout.CENTER);
        this.close.addActionListener(actionEvent -> System.exit(0));
        this.topBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
            }
        });

        this.topBar.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int xOnScreen,yOnScreen,x,y;
                xOnScreen = mouseEvent.getXOnScreen();
                yOnScreen = mouseEvent.getYOnScreen();
                x = mouseEvent.getX();
                y = mouseEvent.getY();
                BrowserDialog.this.setLocation(xOnScreen - oldX,yOnScreen - oldY);
            }
        });
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    public void genJFX(){
        Platform.runLater(() -> {
            WebView webView = new WebView();

            WebEngine webEngine = webView.getEngine();
            Group root = new Group();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            browser.setScene(scene);
            double widthDouble = new Integer(WIDTH).doubleValue();
            double heightDouble = new Integer(HEIGHT).doubleValue();

            VBox box = new VBox(10);

            WebView view = new WebView();
            view.setMinSize(widthDouble, heightDouble);
            view.setPrefSize(widthDouble, heightDouble);
            final WebEngine eng = view.getEngine();
            eng.load( "https://login.live.com/oauth20_authorize.srf" +
                    "?client_id=00000000402b5328" +
                    "&response_type=code" +
                    "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL" +
                    "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf");
            // TODO: 登录url
            root.getChildren().add(view);

            box.getChildren().add(view);
            root.getChildren().add(box);
        });
    }

    public Image getImgIcon(URL imgURL) throws IOException {
        assert BrowserDialog.imgURL != null;
        InputStream input = BrowserDialog.imgURL.openStream();
        Image img = ImageIO.read(input);
        return img;
    }

}
