package cn.org.bedrocktree.carbon.download.mirrors;

import cn.org.bedrocktree.carbon.download.MinecraftMirror;
import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;
import cn.org.bedrocktree.carbon.utils.JSONUtils;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import cn.org.bedrocktree.carbon.utils.SystemUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OfficialMinecraftMirror extends MinecraftMirror {

    @Override
    public String getLibUrl() {
        return "https://libraries.minecraft.net/";
    }

    @Override
    public String getMinecraftManifestJsonDownloadUrl() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public String getMinecraftJsonDownloadUrl(File manifestJson, String version) throws DownloadFailedException, NoSuchMinecraftVersionException, FileNotFoundException {
        if (manifestJson.exists()){
            JSONObject jsonObject = JSONObject.parseObject(new JSONReader(new FileReader(manifestJson.getPath())).readString());
            JSONArray jsonArray = jsonObject.getJSONArray("versions");
            JSONObject json = null;
            boolean isVersionExist = false;
            for (int i = 0;i < jsonArray.size();i++){
                JSONObject tmp = jsonArray.getJSONObject(i);
                if (tmp.getString("id").equals(version)){
                    json = tmp;
                    isVersionExist = true;
                    break;
                }
            }
            if (json == null){
                throw new DownloadFailedException("version_manifest.json is not standard");
            }
            return json.getString("url");

        }else {
            throw new DownloadFailedException("Could not find version_manifest.json in "+ manifestJson.getPath());
        }
    }

    @Override
    public String getMinecraftJarDownloadUrl(File versionJson) throws DownloadFailedException, FileNotFoundException {
        if (versionJson.exists()){
            JSONObject jsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
            return jsonObject.getJSONObject("downloads").getJSONObject("client").getString("url");
        }else {
            throw new DownloadFailedException("Could not find " + versionJson.getName() +" in " + versionJson.getPath());
        }
    }

    @Override
    public String getMinecraftJarSha1(File versionJson) throws DownloadFailedException, FileNotFoundException {
        if (versionJson.exists()){
            JSONObject jsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
            return jsonObject.getJSONObject("downloads").getJSONObject("client").getString("sha1");
        }else {
            throw new DownloadFailedException("Could not find " + versionJson.getName() +" in " + versionJson.getPath());
        }
    }

    @Override
    public String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException {
        JSONObject jsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        return jsonObject.getJSONObject("assetIndex").getString("url");
    }

    @Override
    public String getMinecraftIndexJsonSha1(File versionJson) throws FileNotFoundException {
        JSONObject jsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        return jsonObject.getJSONObject("assetIndex").getString("sha1");
    }


    @Override
    public String getMinecraftResourceDownloadUrl(String hashFirst2,String hash){
        String result = "http://resources.download.minecraft.net/";
        result += hashFirst2+"/"+hash;
        return result;
    }

    @Override
    public String getMinecraftLoggerConfigDownloadUrl(File versionJson) throws FileNotFoundException {
        JSONObject jsonObject = JSON.parseObject(StreamUtils.readJsonFile(versionJson));
        return jsonObject.getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("url");
    }

    @Override
    public String getMinecraftLoggerConfigSha1(File versionJson) throws FileNotFoundException {
        JSONObject jsonObject = JSON.parseObject(StreamUtils.readJsonFile(versionJson));
        return jsonObject.getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("sha1");
    }

    @Override
    public List<String> getMinecraftLibrariesDownloadUrl(File versionJson) throws FileNotFoundException {
        JSONObject json = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        JSONArray libraries = json.getJSONArray("libraries");
        List<String> librariesList = new ArrayList<String>();
        for (int i = 0;i < libraries.size();i++){
            JSONObject downloads = libraries.getJSONObject(i).getJSONObject("downloads");
            if (libraries.getJSONObject(i).containsKey("rules")){
                if (JSONUtils.rulesJudgmenter(libraries.getJSONObject(i).getJSONArray("rules"))){
                    if (downloads.containsKey("artifact")) {
                        librariesList.add(downloads.getJSONObject("artifact").getString("url"));
                    }
                }
            }else {
                if (downloads.containsKey("artifact")) {
                    librariesList.add(downloads.getJSONObject("artifact").getString("url"));
                }
            }
        }
        return librariesList;
    }

    @Override
    public List<String> getMinecraftLibrariesSha1(File versionJson) throws FileNotFoundException {
        JSONObject json = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        JSONArray libraries = json.getJSONArray("libraries");
        List<String> librariesList = new ArrayList<String>();
        for (int i = 0;i < libraries.size();i++){
            JSONObject downloads = libraries.getJSONObject(i).getJSONObject("downloads");
            if (libraries.getJSONObject(i).containsKey("rules")){
                if (JSONUtils.rulesJudgmenter(libraries.getJSONObject(i).getJSONArray("rules"))){
                    if (downloads.containsKey("artifact")) {
                        librariesList.add(downloads.getJSONObject("artifact").getString("sha1"));
                    }
                }
            }else {
                if (downloads.containsKey("artifact")) {
                    librariesList.add(downloads.getJSONObject("artifact").getString("sha1"));
                }
            }
        }
        return librariesList;
    }

    @Override
    public List<String> getMinecraftNativeLibrariesDownloadUrl(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONArray("libraries");
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject downloads = jsonArray.getJSONObject(i).getJSONObject("downloads");
            if (downloads.containsKey("classifiers")){
                JSONObject classifiers = downloads.getJSONObject("classifiers");
                String key;
                if (SystemUtils.getSystemName().contains("Windows")){
                    key = "natives-windows";
                }else {
                    key = "natives-linux";
                }
                if (classifiers.getJSONObject(key) != null){
                    list.add(classifiers.getJSONObject(key).getString("url"));
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getMinecraftNativeLibrariesSha1(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONArray("libraries");
        for (int i = 0;i < jsonArray.size();i++){
            JSONObject downloads = jsonArray.getJSONObject(i).getJSONObject("downloads");
            if (downloads.containsKey("classifiers")){
                JSONObject classifiers = downloads.getJSONObject("classifiers");
                String key;
                if (SystemUtils.getSystemName().contains("Windows")){
                    key = "natives-windows";
                }else {
                    key = "natives-linux";
                }
                if (classifiers.getJSONObject(key) != null){
                    list.add(classifiers.getJSONObject(key).getString("sha1"));
                }
            }
        }
        return list;
    }
}