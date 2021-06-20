package cn.org.bedrocktree.carbon;

import cn.org.bedrocktree.carbon.factory.UserProfileFactory;
import cn.org.bedrocktree.carbon.init.InitFiles;
import cn.org.bedrocktree.carbon.ui.MainWindow;
import cn.org.bedrocktree.carbon.utils.MicrosoftUtils;
import cn.org.bedrocktree.carbon.utils.SystemUtils;

import java.io.File;

public class Carbon {

    public static final String APPLICATION_NAME = "Carbon";

    public static final String VERSION = "ALPHA_0.0.1";

    public static final String CONFIGURATION_DIRECTORY_PATH = SystemUtils.getSystemConfigDirectory()+APPLICATION_NAME;

    public static final String ROOT_LOCATION = "/data/Projects/Java/Another/Carbon/Test/";

    public static void main(String[] args) throws Exception {
        try {
            InitFiles.init();
            MainWindow.genUIManager();
            new MainWindow().init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
