package cn.org.bedrocktree.carbon.ui;

import cn.org.bedrocktree.carbon.Carbon;
import cn.org.bedrocktree.carbon.myswing.*;
import cn.org.bedrocktree.carbon.utils.StreamUtils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainWindow {

    public static final URL logoIconURL = MainWindow.class.getResource("/icon/icon.png");

    public static final URL minimizeIconURL = MainWindow.class.getResource("/icon/minimize.png");

    public static final URL closeIconURL = MainWindow.class.getResource("/icon/close.png");

    private final CardLayout cardLayout = new CardLayout();

    private final MyJFrame mainFrame = new MyJFrame();

    private final Container container = this.mainFrame.getContentPane();

    private final JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,9));

    private final JPanel control = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));

    private final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));

    private final JPanel center = new JPanel(cardLayout);

    private final JPanel homePanel = new JPanel(new BorderLayout(0,0));

    private final JPanel launchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));

    private final MyJButton launch = new MyJButton("Launch","launch");

    //TODO 列表
    private final JComboBox<String> versionChooser = new JComboBox<String>(new String[]{"1.12.2"});

    private final JComboBox<JLabel> profileChooser = new JComboBox<>(new JLabel[] {new JLabel("az")});

    private final JLabel version = new JLabel("Game Version");

    private final JPanel profilePanel = new JPanel(new GridLayout());

    private final JPanel gamesPanel = new JPanel(new GridLayout());

    private final JPanel downloadsPanel = new JPanel(new GridLayout(10,1,25,25));

    private final JPanel settingsPanel = new JPanel(new GridLayout());

    private final JPanel aboutPanel = new JPanel(new GridLayout());

    private final JLabel logo = new JLabel();

    private final JLabel title = new JLabel(Carbon.APPLICATION_NAME,JLabel.CENTER);

    private final LeftBarEntry home = new LeftBarEntry("Home","home");

    private final LeftBarEntry profile = new LeftBarEntry("Profile","profile");

    private final LeftBarEntry games = new LeftBarEntry("Games","games");

    private final LeftBarEntry downloads = new LeftBarEntry("Downloads","downloads");

    private final LeftBarEntry settings = new LeftBarEntry("Settings","settings");

    private final LeftBarEntry about = new LeftBarEntry("About","about");

    private final MyJButton minimize = new MyJButton();

    private final MyJButton close = new MyJButton();

    int oldX = 0,oldY = 0;

    public MainWindow() throws IOException { }

    public static void genUIManager(){
        UIManager.put("ScrollBar.trackHighlightForeground", (ColorEnum.GREY_35));
        UIManager.put("scrollbar", (ColorEnum.GREY_35));
        UIManager.put("ScrollBar.thumb", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.thumbHeight", 2);
        UIManager.put("ScrollBar.background", (ColorEnum.GREY_35));
        UIManager.put("ScrollBar.thumbDarkShadow", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.thumbShadow", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.trackForeground", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.trackHighlight", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.foreground", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.shadow", new ColorUIResource(ColorEnum.GREY_35));
        UIManager.put("ScrollBar.highlight", new ColorUIResource(ColorEnum.GREY_35));
    }

    public void init() throws IOException {

        this.setSizes();
        this.setComponents();
        this.setBackgrounds();
        this.addComponents();
        this.addListeners();
        this.mainFrame.setVisible(true);

    }


    private void setSizes(){

        this.top.setSize(800,20);
        this.control.setSize(800,20);
        this.left.setPreferredSize(new Dimension(200,480));
        this.homePanel.setSize(600,480);
        this.profilePanel.setSize(600,480);
        this.gamesPanel.setSize(600,480);
        this.downloadsPanel.setSize(600,480);
        this.settingsPanel.setSize(600,480);
        this.aboutPanel.setSize(600,480);
        this.launchBar.setSize(500,60);
        this.launch.setSize(70,50);


    }

    private void addComponents(){

        this.container.add(top,BorderLayout.NORTH);
        this.container.add(left,BorderLayout.WEST);
        this.container.add(center,BorderLayout.CENTER);

        this.top.add(logo);
        this.top.add(title);
        this.top.add(control);
        this.left.add(home);
        this.left.add(profile);
        this.left.add(games);
        this.left.add(downloads);
        this.left.add(settings);
        this.left.add(about);
        this.control.add(minimize);
        this.control.add(close);
        this.center.add(homePanel,"home");
        this.center.add(profilePanel,"profile");
        this.center.add(gamesPanel,"games");
        this.center.add(downloadsPanel,"downloads");
        this.center.add(settingsPanel,"settings");
        this.center.add(aboutPanel,"about");
        this.homePanel.add(launchBar,BorderLayout.SOUTH);
        this.launchBar.add(version);
        this.launchBar.add(versionChooser);
        this.launchBar.add(launch);

    }
    private void setBackgrounds(){

        this.top.setBackground(ColorEnum.GREY_25);
        this.control.setBackground(ColorEnum.GREY_25);
        this.left.setBackground(ColorEnum.GREY_75);
        this.homePanel.setBackground(ColorEnum.GREY_75);
        this.profilePanel.setBackground(ColorEnum.GREY_75);
        this.gamesPanel.setBackground(ColorEnum.GREY_75);
        this.downloadsPanel.setBackground(ColorEnum.GREY_75);
        this.settingsPanel.setBackground(ColorEnum.GREY_75);
        this.aboutPanel.setBackground(ColorEnum.GREY_75);
        this.launchBar.setBackground(ColorEnum.GREY_50);
        this.launch.setDefaultBackground(ColorEnum.GREY_35);
        this.versionChooser.setBackground(ColorEnum.GREY_55);

    }
    private void setComponents() throws IOException {

        this.container.setLayout(new BorderLayout(0,0));

        this.logo.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(logoIconURL)).getScaledInstance(20,20,Image.SCALE_SMOOTH)));

        this.title.setPreferredSize(new Dimension(700,20));
        this.title.setForeground(ColorEnum.WHITE);

        this.minimize.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(minimizeIconURL)).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        this.minimize.setDefaultBackground(ColorEnum.GREY_25);
        this.minimize.setPreferredSize(new Dimension(20,20));

        this.close.setIcon(new ImageIcon(StreamUtils.getImgIcon(Objects.requireNonNull(closeIconURL)).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        this.close.setDefaultBackground(ColorEnum.GREY_25);
        this.close.setPreferredSize(new Dimension(20,20));

        this.home.setBackground(ColorEnum.GREY_55);
        this.home.isSelected = true;

        this.launchBar.setPreferredSize(new Dimension(500,60));
        this.launchBar.setForeground(ColorEnum.WHITE);

        this.launch.setPreferredSize(new Dimension(150,40));
        this.launch.setBorderPainted(true);
        this.launch.setBorder(BorderFactory.createLineBorder(ColorEnum.GREY_25,1,true));

        this.versionChooser.setPreferredSize(new Dimension(320,35));
        this.versionChooser.setBorder(BorderFactory.createLineBorder(ColorEnum.GREY_25,1,true));
        this.versionChooser.setForeground(ColorEnum.WHITE);
        this.versionChooser.setFocusable(false);
        this.versionChooser.setEditable(false);
        this.versionChooser.setToolTipText("Choose a Game Version");
        this.versionChooser.setUI(new MyComboBoxUI());
        this.versionChooser.setRenderer(new MyListCellRenderer());

        this.version.setForeground(ColorEnum.WHITE);
    }


    private void addListeners(){

        this.minimize.addActionListener(actionEvent -> mainFrame.setExtendedState(Frame.ICONIFIED));
        this.close.addActionListener(actionEvent -> System.exit(0));
        this.home.addActionListener(actionEvent -> {
            MainWindow.this.profile.reset();
            MainWindow.this.games.reset();
            MainWindow.this.downloads.reset();
            MainWindow.this.settings.reset();
            MainWindow.this.about.reset();
            MainWindow.this.cardLayout.show(center,"home");
        });
        this.profile.addActionListener(actionEvent -> {
            MainWindow.this.home.reset();
            MainWindow.this.games.reset();
            MainWindow.this.downloads.reset();
            MainWindow.this.settings.reset();
            MainWindow.this.about.reset();
            MainWindow.this.cardLayout.show(center,"profile");
        });
        this.games.addActionListener(actionEvent -> {
            MainWindow.this.home.reset();
            MainWindow.this.profile.reset();
            MainWindow.this.downloads.reset();
            MainWindow.this.settings.reset();
            MainWindow.this.about.reset();
            MainWindow.this.cardLayout.show(center,"games");
        });
        this.downloads.addActionListener(actionEvent -> {
            MainWindow.this.home.reset();
            MainWindow.this.profile.reset();
            MainWindow.this.games.reset();
            MainWindow.this.settings.reset();
            MainWindow.this.about.reset();
            MainWindow.this.cardLayout.show(center,"downloads");

        });
        this.settings.addActionListener(actionEvent -> {
            MainWindow.this.home.reset();
            MainWindow.this.profile.reset();
            MainWindow.this.games.reset();
            MainWindow.this.downloads.reset();
            MainWindow.this.about.reset();
            MainWindow.this.cardLayout.show(center,"settings");
        });
        this.about.addActionListener(actionEvent -> {
            MainWindow.this.home.reset();
            MainWindow.this.profile.reset();
            MainWindow.this.games.reset();
            MainWindow.this.downloads.reset();
            MainWindow.this.settings.reset();
            MainWindow.this.cardLayout.show(center,"about");
        });

        this.top.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
            }
        });

        this.top.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int xOnScreen,yOnScreen,x,y;
                xOnScreen = mouseEvent.getXOnScreen();
                yOnScreen = mouseEvent.getYOnScreen();
                x = mouseEvent.getX();
                y = mouseEvent.getY();
                MainWindow.this.mainFrame.setLocation(xOnScreen - oldX,yOnScreen - oldY);
            }
        });

        this.launch.addActionListener(e -> {



        });
    }
}