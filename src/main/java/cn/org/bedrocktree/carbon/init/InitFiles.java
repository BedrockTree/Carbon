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


    public static void init() throws IOException {
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
    public static void genConfigFile() throws IOException {
        if (!CONFIG_FILE.exists()){
            CONFIG_FILE.createNewFile();
        }
        if (PROFILE_JSON_FILE.exists()){
            PROFILE_JSON_FILE.createNewFile();
        }
    }
}
