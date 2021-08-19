package cn.org.bedrocktree.carbon.utils;

public class SystemUtils {

    public static String getUsername(){
        return System.getProperty("user.name");
    }

    public static String getUserHomeDirectory(){
        return System.getProperty("user.home");
    }

    public static String getSystemName(){
        return System.getProperty("os.name");
    }

    public static String getSystemVersion(){
        return System.getProperty("os.version");
    }

    public static String getSystemArch(){
        return System.getProperty("os.arch");
    }

    public static String getSystemConfigDirectory(){
        if (getSystemName().contains("Windows")){
            return getUserHomeDirectory()+"\\AppData\\Roaming\\";
        }else if (getSystemName().contains("Linux")){
            return getUserHomeDirectory()+"/.config/";
        }else {
            return null;
        }
    }
    public static String getSystemTempPath(){
        if (getSystemName().contains("Windows")){
            return getUserHomeDirectory()+"\\AppData\\Local\\Temp\\";
        }else if (getSystemName().contains("Linux")){
            return "/tmp/";
        }else {
            return null;
        }
    }
}
