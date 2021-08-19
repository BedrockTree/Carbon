package cn.org.bedrocktree.carbon.ui;

import cn.org.bedrocktree.carbon.myswing.*;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DownloadsPanel extends JPanel{



    public DownloadsPanel(File manifestJson){

    }

}



/*public class DownloadsPanel extends JScrollPane {

    private final CardLayout cardLayout = new CardLayout();

    private final JPanel mainPanel = new JPanel(new BorderLayout(0,0));

    private final JPanel chooser = new JPanel(new GridLayout(1,3));

    private final JPanel downloadsPanel = new JPanel(cardLayout);

    private final ChooserButton release = new ChooserButton("release");

    private final ChooserButton snapshot = new ChooserButton("snapshot");

    private final ChooserButton olds = new ChooserButton("olds");

    private final JPanel releasesPanel;

    private final JPanel snapshotsPanel;

    private final JPanel oldsPanel;

    private BoxLayout releasesBoxLayout;

    private BoxLayout snapshotsBoxLayout;

    private BoxLayout oldsBoxLayout;

    public DownloadsPanel(File manifestJsonFile) throws FileNotFoundException {
        super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setBackground(ColorEnum.GREY_75);
        this.getVerticalScrollBar().setUI(new MyJScrollBarUI());
        this.setSize(new Dimension(600,480));
        this.setViewportView(mainPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        downloadsPanel.setBackground(ColorEnum.GREY_75);
        downloadsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,18,0));
        chooser.setBackground(ColorEnum.GREY_75);
        release.setBackground(ColorEnum.GREY_75);
        release.isSelected = true;
        chooser.setSize(600,38);
        chooser.setPreferredSize(new Dimension(600,38));
        chooser.add(release);
        chooser.add(snapshot);
        chooser.add(olds);
        mainPanel.setBackground(ColorEnum.GREY_75);
        mainPanel.add(chooser,BorderLayout.NORTH);
        JSONArray versions = JSONObject.parseObject(StreamUtils.readJsonFile(manifestJsonFile)).getJSONArray("versions");
        List<String> releasesList = new ArrayList<>();
        List<String> snapshotsList = new ArrayList<>();
        List<String> oldsList = new ArrayList<>();
        for (int i = 0;i < versions.size();i++){
            JSONObject jsonObject = versions.getJSONObject(i);
            String id = jsonObject.getString("id");
            String type = jsonObject.getString("type");
            if ("release".equals(type)){
                releasesList.add(id);
            }else if ("snapshot".equals(type)){
                snapshotsList.add(id);
            }else {
                oldsList.add(id);
            }
        }
        releasesPanel = new JPanel(null);
        snapshotsPanel = new JPanel(null);
        oldsPanel = new JPanel(null);
        releasesPanel.setSize(200,(releasesList.size()*60)+10);
        releasesPanel.setPreferredSize(new Dimension(200,(releasesList.size()*60)+10));
        snapshotsPanel.setSize(200,(snapshotsList.size()*60)+10);
        snapshotsPanel.setPreferredSize(new Dimension(200,(snapshotsList.size()*60)+10));
        oldsPanel.setSize(200,(oldsList.size()*60)+10);
        oldsPanel.setPreferredSize(new Dimension(200,(oldsList.size()*60)+10));
        releasesBoxLayout = new BoxLayout(releasesPanel,BoxLayout.Y_AXIS);
        snapshotsBoxLayout = new BoxLayout(snapshot,BoxLayout.Y_AXIS);
        oldsBoxLayout = new BoxLayout(olds,BoxLayout.Y_AXIS);
        releasesPanel.setBackground(ColorEnum.GREY_75);
        snapshotsPanel.setBackground(ColorEnum.GREY_75);
        oldsPanel.setBackground(ColorEnum.GREY_75);
        System.gc();
        init(releasesList,snapshotsList,oldsList);
        mainPanel.add(downloadsPanel,BorderLayout.CENTER);
        cardLayout.show(downloadsPanel,"release");
    }

    private void init(List<String> releasesList,List<String> snapshotsList,List<String> oldsList){
        for (int i = 0;i < releasesList.size();i++){
            String version = releasesList.get(i);
            DownloadButton downloadButton = new DownloadButton(version);
            releasesPanel.add(downloadButton);
            downloadButton.setLocation(95,(i*60)+18);
            System.out.println(downloadButton.getY());
        }
        for (int i = 0;i < snapshotsList.size();i++){
            String version = snapshotsList.get(i);
            DownloadButton downloadButton = new DownloadButton(version);
            snapshotsPanel.add(downloadButton);
            downloadButton.setLocation(95,(i*60)+18);
        }
        for (int i = 0;i < oldsList.size();i++){
            String version = oldsList.get(i);
            DownloadButton downloadButton = new DownloadButton(version);
            oldsPanel.add(downloadButton);
            downloadButton.setLocation(95,(i*60)+18);
        }
        downloadsPanel.add(releasesPanel,"release");
        downloadsPanel.add(snapshotsPanel,"snapshot");
        downloadsPanel.add(oldsPanel,"old");
        release.addActionListener(actionEvent -> {
            DownloadsPanel.this.snapshot.reset();
            DownloadsPanel.this.olds.reset();
            cardLayout.show(downloadsPanel,"release");
            downloadsPanel.setSize(480,releasesPanel.getHeight());
            mainPanel.setSize(480,releasesPanel.getHeight()+38);
        });
        snapshot.addActionListener(actionEvent -> {
            DownloadsPanel.this.release.reset();
            DownloadsPanel.this.olds.reset();
            cardLayout.show(downloadsPanel,"snapshot");
            downloadsPanel.setSize(480,snapshotsPanel.getHeight());
            mainPanel.setSize(480,snapshotsPanel.getHeight()+38);
        });
        olds.addActionListener(actionEvent -> {
            DownloadsPanel.this.snapshot.reset();
            DownloadsPanel.this.release.reset();
            cardLayout.show(downloadsPanel,"old");
            downloadsPanel.setSize(480,oldsPanel.getHeight());
            mainPanel.setSize(480,oldsPanel.getHeight()+38);
        });
    }

    private static class DownloadButton extends MyJButton{
        public DownloadButton(String version){
            super(version);
            this.setDefaultBackground(ColorEnum.GREY_45);
            this.setSize(new Dimension(400,50));
            this.setPreferredSize(new Dimension(400,50));
            this.setMouseEnteredColor(ColorEnum.GREY_55);
            this.setMousePressedColor(ColorEnum.GREY_65);
        }
    }

    private static class ChooserButton extends MyJButton{

        public boolean isSelected = false;

        Color oldColor = ColorEnum.GREY_45;

        public ChooserButton(String value){
            super(value);
            this.setBorderPainted(false);
            this.setHorizontalAlignment(CENTER);
            this.setPreferredSize(new Dimension(200,38));
            this.setSize(200,38);
            this.setDefaultBackground(ColorEnum.GREY_45);
            this.setForeground(ColorEnum.WHITE);
        }

        @Override
        public void setDefaultBackground(Color color) {
            super.setDefaultBackground(color);
        }

        @Override
        public void addListeners() {
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    ChooserButton.this.isSelected = true;
                    ChooserButton.this.setBackground(ColorEnum.GREY_75);
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                    ChooserButton.this.setBackground(ColorEnum.GREY_75);
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    ChooserButton.this.setBackground(oldColor);
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                    ChooserButton.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    if (!ChooserButton.this.isSelected){
                        ChooserButton.this.setBackground(ColorEnum.GREY_35);
                    }
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                    if (!ChooserButton.this.isSelected){
                        ChooserButton.this.setBackground(oldColor);
                    }
                }
            });
        }
        public void reset(){
            this.setBackground(oldColor);
            this.isSelected = false;
        }
    }
}*/