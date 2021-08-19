package cn.org.bedrocktree.carbon.init;

import cn.org.bedrocktree.carbon.Carbon;
import cn.org.bedrocktree.carbon.utils.DownloadUtils;

import java.io.File;
import java.io.IOException;

public class InitFiles {

    public static final File CONFIG_DIRECTORY = new File(Carbon.CONFIGURATION_DIRECTORY_PATH);

    public static final File MINECRAFT_DIRECTORY = new File(CONFIG_DIRECTORY+File.separator+".minecraft");

    public static final File CONFIG_FILE = new File(CONFIG_DIRECTORY+ File.separator+"config.properties");

    public static final File PROFILE_JSON_FILE = new File(CONFIG_DIRECTORY+ File.separator+"profile.json");


    public static void init() {
        genDirectory();
        genConfigFile();
    }
    public static void genDirectory(){
        if (CONFIG_DIRECTORY.exists()){
            if (!CONFIG_DIRECTORY.isDirectory()) {
                CONFIG_DIRECTORY.mkdirs();
            }
        }else {
            CONFIG_DIRECTORY.mkdirs();
        }
        if (MINECRAFT_DIRECTORY.exists()){
            if (!MINECRAFT_DIRECTORY.isDirectory()) {
                MINECRAFT_DIRECTORY.mkdirs();
            }
        }else {
            MINECRAFT_DIRECTORY.mkdirs();
        }
    }
    public static void genConfigFile() {
        if (!CONFIG_FILE.exists()){
            try {
                CONFIG_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (PROFILE_JSON_FILE.exists()){
            try {
                PROFILE_JSON_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
