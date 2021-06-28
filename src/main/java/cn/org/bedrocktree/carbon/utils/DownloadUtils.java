package cn.org.bedrocktree.carbon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class DownloadUtils {

    public static void download(String url, String path, String fileName) throws IOException {

        if (!new File(path + File.separator + fileName).exists()) {
            long fileSize = (new URL(url).openConnection()).getContentLengthLong();
            URL downloadUrl = new URL(url);
            URLConnection urlConnection = downloadUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Range", "bytes=0-");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            File file = new File(path + File.separator + fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[4096];
            int count = 0, read = 0;
            boolean hasFinished = false;
            while (!hasFinished) {
                while ((read = inputStream.read(bytes)) != -1) {
                    if (fileSize - count < bytes.length) {
                        read = (int) (fileSize - count);
                    }
                    outputStream.write(bytes, 0, read);
                    count += read;
                    if (count == fileSize) {
                        break;
                    }
                }
                hasFinished = true;
            }
        }

    }

    public static void download(String url, String filePath) throws IOException {
        if (!new File(filePath).exists()) {
            long fileSize = (new URL(url).openConnection()).getContentLengthLong();
            URL downloadUrl = new URL(url);
            URLConnection urlConnection = downloadUrl.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Range", "bytes=0-");
            httpUrlConnection.connect();
            InputStream inputStream = httpUrlConnection.getInputStream();
            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[4096];
            int count = 0, read = 0;
            boolean hasFinished = false;
            while (!hasFinished) {
                while ((read = inputStream.read(bytes)) != -1) {
                    if (fileSize - count < bytes.length) {
                        read = (int) (fileSize - count);
                    }
                    outputStream.write(bytes, 0, read);
                    count += read;
                    if (count == fileSize) {
                        break;
                    }
                }
                hasFinished = true;
            }
        }
    }

    public static void download(String url, String path, String fileName, String sha1) throws IOException {

        if (!new File(path + File.separator + fileName).exists()) {
            do {
                System.gc();
                new File(path + File.separator + fileName).delete();
                download(url, path, fileName);
                System.out.println(url+"\n"+path+fileName +"\n  "+new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(new File(path + File.separator + fileName))+"   "+sha1+"\n\n");
            } while (!(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(new File(path + File.separator + fileName)).equals(sha1)));
        } else if (!(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(new File(path + File.separator + fileName)).equals(sha1))) {
            do {
                System.gc();
                new File(path + File.separator + fileName).delete();
                download(url, path, fileName);
                System.out.println(url+"\n"+path+fileName +"\n  "+new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(new File(path + File.separator + fileName))+"   "+sha1+"\n\n");
            } while (!(new DigestUtils(MessageDigestAlgorithms.SHA_1).digestAsHex(new File(path + File.separator + fileName)).equals(sha1)));
        }
    }



    public static Map<String,String> getObjects(File indexJson) throws FileNotFoundException {
        Map<String,String> map = new HashMap<String,String>();
        JSONObject indexJsonObject = JSON.parseObject(StreamUtils.readJsonFile(indexJson));
        JSONObject objects = indexJsonObject.getJSONObject("objects");
        Set<String> keySet = objects.keySet();
        for (int i = 0;i < objects.size();i++){
            String key = (String) keySet.toArray()[i];
            String hash = objects.getJSONObject(key).getString("hash");
            map.put(key,hash);
        }
        return map;
    }

    public static List<String> getLibraries(File versionJson) throws FileNotFoundException {
        List<String> list = new ArrayList<String>();
        JSONObject json = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        JSONArray libraries = json.getJSONArray("libraries");
        List<String> librariesList = new ArrayList<String>();
        for (int i = 0;i < libraries.size();i++){
            JSONObject downloads = libraries.getJSONObject(i).getJSONObject("downloads");
            if (libraries.getJSONObject(i).containsKey("rules")){
                if (JSONUtils.rulesJudgmenter(libraries.getJSONObject(i).getJSONArray("rules"))){
                    if (downloads.containsKey("artifact")) {
                        librariesList.add(downloads.getJSONObject("artifact").getString("path"));
                    }
                }
            }else {
                if (downloads.containsKey("artifact")) {
                    librariesList.add(downloads.getJSONObject("artifact").getString("path"));
                }
            }
        }
        list.addAll(librariesList);
        return list;
    }

    public static List<String> getNativeLibraries(File versionJson) throws FileNotFoundException {
        JSONObject versionJsonObject = JSON.parseObject(StreamUtils.readJsonFile(versionJson));
        JSONArray libraries = versionJsonObject.getJSONArray("libraries");
        List<String> list = new ArrayList<String>();
        for (int i = 0;i < libraries.size();i++){
            JSONObject downloads = libraries.getJSONObject(i).getJSONObject("downloads");
            if (downloads.containsKey("classifiers")){
                if (downloads.getJSONObject("artifact") != null){
                    String path = downloads.getJSONObject("artifact").getString("path");
                    if (libraries.getJSONObject(i).containsKey("rules")) {
                        if (JSONUtils.rulesJudgmenter(libraries.getJSONObject(i).getJSONArray("rules"))) {
                            list.add(path);
                        }
                    }else {
                        list.add(path);
                    }
                }
            }
        }
        return list;
    }

    public static void unzipNativeLibraries(String filePath,String outputDirectory) throws IOException {
        ZipFile zipFile = new ZipFile(filePath);
        zipFile.extractAll(outputDirectory);
    }

}