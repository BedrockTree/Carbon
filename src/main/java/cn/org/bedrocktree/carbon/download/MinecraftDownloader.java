package cn.org.bedrocktree.carbon.download;

import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;
import cn.org.bedrocktree.carbon.utils.DownloadUtils;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftDownloader {

    public MinecraftMirror minecraftMirror;

    public String version,versionName,path,gamePath,libPath,nativePath,assetsPath,indexPath,objectsPath,loggingPath;

    public boolean isVersionIsolation;

    public  File manifest = new File(path+File.separator+"version_manifest.json");

    public File versionJson,indexJson;

    public MinecraftDownloader(MinecraftMirror mirror,String version,String versionName,String path,boolean isVersionIsolation) throws DownloadFailedException, NoSuchMinecraftVersionException, IOException {
        this.minecraftMirror = mirror;
        this.version = version;
        this.versionName = versionName;
        this.path = path;
        this.isVersionIsolation = isVersionIsolation;
        if (isVersionIsolation){
            gamePath = path+File.separator+".minecraft"+File.separator+"versions"+File.separator+versionName;
        }else {
            gamePath = path+File.separator+".minecraft";
        }
        this.libPath = path+File.separator+".minecraft"+File.separator+"libraries"+File.separator;
        this.nativePath = gamePath+File.separator+"natives";
        this.assetsPath = path+File.separator+".minecraft"+File.separator+"assets";
        this.indexPath = assetsPath+File.separator+"indexes";
        this.objectsPath = assetsPath+File.separator+"objects";
        this.loggingPath = assetsPath+File.separator+"log_configs";
        if (!(new File(gamePath).exists())){
            new File(gamePath).mkdirs();
        }
        if (!(new File(nativePath)).exists()){
            new File(nativePath).mkdirs();
        }
        if (!(new File(assetsPath)).exists()){
            new File(assetsPath).mkdirs();
        }
        if (!(new File(indexPath)).exists()){
            new File(indexPath).mkdirs();
        }
        if (!(new File(loggingPath)).exists()){
            new File(loggingPath).mkdirs();
        }
        if (!(new File(libPath)).exists()){
            new File(libPath).mkdirs();
        }
    }

    private void downloadManifest() throws IOException {
        DownloadUtils.download(minecraftMirror.getMinecraftManifestJsonDownloadUrl(),path,"version_manifest.json");
        manifest = new File(path+File.separator+"version_manifest.json");
    }

    private void downloadJson() throws DownloadFailedException, NoSuchMinecraftVersionException, IOException {
        DownloadUtils.download(minecraftMirror.getMinecraftJsonDownloadUrl(new File(path+File.separator+"version_manifest.json"),version),gamePath,version+".json");
        this.versionJson = new File(gamePath+File.separator+version+".json");
    }

    private void downloadJar() throws DownloadFailedException, NoSuchMinecraftVersionException, IOException {
        if (manifest.exists()){
            downloadManifest();
        }
        if (!versionJson.exists()){
            downloadJson();
        }
        DownloadUtils.download(minecraftMirror.getMinecraftJarDownloadUrl(versionJson),gamePath,version+".jar");
    }

    private void downloadAssetIndexJson() throws IOException {
        DownloadUtils.download(minecraftMirror.getMinecraftIndexJsonDownloadUrl(versionJson),indexPath+File.separator+JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getString("assets")+".json");
        this.indexJson = new File(indexPath+File.separator+JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getString("assets")+".json");
    }

    private void downloadResourceObjects() throws IOException, DownloadFailedException {
        Map<String,String> map = DownloadUtils.getObjects(indexJson);
        Set<String> keySet = map.keySet();
        for (int i = 0;i < map.size();i++){
            String key = (String) keySet.toArray()[i];
            String hash = map.get(key);
            File path = new File(objectsPath+File.separator+hash.substring(0,2));
            path.mkdirs();
            DownloadUtils.download(minecraftMirror.getMinecraftResourceDownloadUrl(hash.substring(0,2),hash),path.getPath(),hash);
        }
    }

    private void downloadLoggerConfig() throws IOException {
        DownloadUtils.download(minecraftMirror.getMinecraftLoggerConfigDownloadUrl(versionJson),loggingPath,JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("id"));
    }

    private void downloadLibraries() throws IOException {
        List<String> libraries = minecraftMirror.getMinecraftLibrariesDownloadUrl(versionJson);
        for (String url : libraries){
            String path = url.replaceAll(minecraftMirror.getLibUrl(),"").replaceAll("/",File.separator);
            new File(libPath+path).getParentFile().mkdirs();
            DownloadUtils.download(url,(libPath+path).replaceAll("/",File.separator));
        }
    }

    private void downloadNativeLibraries() throws IOException, DownloadFailedException, OsNotSupportsException {
        List<String> natives = minecraftMirror.getMinecraftNativeLibrariesDownloadUrl(versionJson);
        for (String url : natives){
            if (minecraftMirror.getMinecraftNativeLibrariesDownloadUrl(versionJson) != null){
                DownloadUtils.download(url,nativePath+File.separator+url.substring(url.lastIndexOf("/")+1));
                DownloadUtils.unzipNativeLibraries(nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),nativePath);
                new File(nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),nativePath).delete();
            }
        }
    }

    public void download() throws DownloadFailedException, NoSuchMinecraftVersionException, IOException, OsNotSupportsException {
        downloadManifest();
        downloadJson();
        downloadJar();
        downloadAssetIndexJson();
        downloadResourceObjects();
        if (JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).containsKey("logging")) {
            downloadLoggerConfig();
        }
        downloadLibraries();
        downloadNativeLibraries();
    }

}
