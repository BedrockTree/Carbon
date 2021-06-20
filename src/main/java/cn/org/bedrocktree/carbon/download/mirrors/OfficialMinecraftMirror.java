package cn.org.bedrocktree.carbon.download.mirrors;

import cn.org.bedrocktree.carbon.download.MinecraftMirror;
import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import cn.org.bedrocktree.carbon.utils.SystemUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OfficialMinecraftMirror extends MinecraftMirror {

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
            if (!isVersionExist){
                throw new NoSuchMinecraftVersionException(version);
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
    public String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException {
        JSONObject jsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        return jsonObject.getJSONObject("assetIndex").getString("url");
    }

    @Override
    public String getMinecraftResourceDownloadUrl(String hashFirst2,String hash) throws DownloadFailedException, FileNotFoundException {
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
    public String getMinecraftLibrariesDownloadUrl(String childPath) {
        return "https://libraries.minecraft.net/"+childPath;
    }

    @Override
    public String getMinecraftNativeLibrariesDownloadUrl(File versionJson, String childPath) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        String os = SystemUtils.getSystemName();
        if (versionJson.exists()){
            JSONArray jsonArray = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONArray("libraries");
            JSONObject jsonObject = null;
            for (int i = 0;i < jsonArray.size();i++){
                if (jsonArray.getJSONObject(i).getJSONObject("downloads").containsKey("classifiers")){
                    if (jsonArray.getJSONObject(i).getJSONObject("downloads").getJSONObject("artifact") != null){
                        if (jsonArray.getJSONObject(i).getJSONObject("downloads").getJSONObject("artifact").getString("path").equals(childPath)){
                            jsonObject = jsonArray.getJSONObject(i).getJSONObject("downloads").getJSONObject("classifiers");
                            break;
                        }
                    }
                }
            }
            if (jsonObject == null){
                throw new DownloadFailedException(versionJson.getName() + " is not standard");
            }
            if (os.contains("Mac")){
                jsonObject = jsonObject.getJSONObject("natives-macos");

            }else if (os.contains("Windows")){
                jsonObject = jsonObject.getJSONObject("natives-windows");
            }else if (os.contains("Linux")){
                jsonObject = jsonObject.getJSONObject("natives-linux");
            }else {
                throw new OsNotSupportsException();
            }
            if (jsonObject != null){
                return jsonObject.getString("url");
            }else {
                return null;
            }
        }else {
            throw new DownloadFailedException("Could not find "+versionJson.getName()+" in "+versionJson.getPath());
        }
    }
}