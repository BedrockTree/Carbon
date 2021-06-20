package cn.org.bedrocktree.carbon.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StreamUtils {
    public static Image getImgIcon(URL url) throws IOException {
        InputStream input = url.openStream();
        Image img = ImageIO.read(input);
        return img;
    }
    public static void writeToFile(String value,File file) throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.write(value);
        fileWriter.close();
    }
    public static String readJsonFile(String path) throws FileNotFoundException {
        return new JSONReader(new FileReader(path)).readString();
    }
    public static String readJsonFile(File jsonFile) throws FileNotFoundException {
        return new JSONReader(new FileReader(jsonFile.getPath())).readString();
    }
}