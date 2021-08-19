package cn.org.bedrocktree.carbon.download;

import cn.org.bedrocktree.carbon.download.mirrors.OfficialMinecraftMirror;
import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;
import cn.org.bedrocktree.carbon.utils.DownloadUtils;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;


public class MinecraftDownloader {

    public MinecraftMirror minecraftMirror;

    public String version,versionName,path,gamePath,libPath,nativePath,assetsPath,indexPath,objectsPath,loggingPath;

    public boolean isVersionIsolation;

    public  File manifest;

    public File versionJson,indexJson;

    public int threadCount;

    private final ThreadPoolExecutor threadPool;

    public MinecraftDownloader(MinecraftMirror mirror,String version,String versionName,String path,boolean isVersionIsolation,int thread){
        this.minecraftMirror = mirror;
        this.version = version;
        this.versionName = versionName;
        this.path = path;
        this.manifest = new File(path+File.separator+"version_manifest.json");
        this.isVersionIsolation = isVersionIsolation;
        this.threadCount = thread;
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
        if (!(new File(libPath)).exists()){
            new File(libPath).mkdirs();
        }
        this.threadPool = new ThreadPoolExecutor(10,10,0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    private void downloadManifest(){
        try {
            DownloadUtils.download(minecraftMirror.getMinecraftManifestJsonDownloadUrl(),path,"version_manifest.json");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftManifestJsonDownloadUrl(),path,"version_manifest.json");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        manifest = new File(path+File.separator+"version_manifest.json");
    }

    private void downloadJson() {
        try {
            DownloadUtils.download(minecraftMirror.getMinecraftJsonDownloadUrl(new File(path+File.separator+"version_manifest.json"),version),gamePath,version+".json");
        } catch (IOException | DownloadFailedException | NoSuchMinecraftVersionException e) {
            e.printStackTrace();
            try {
                DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftJsonDownloadUrl(new File(path+File.separator+"version_manifest.json"),version),gamePath,version+".json");
            } catch (IOException | DownloadFailedException | NoSuchMinecraftVersionException ioException) {
                ioException.printStackTrace();
            }

        }
        MinecraftDownloader.this.versionJson = new File(gamePath+File.separator+version+".json");
    }

    private void downloadJar() {
        threadPool.execute(new Thread(() -> {
            if (manifest.exists()){
                downloadManifest();
            }
            if (!versionJson.exists()){
                downloadJson();
            }
            try {
                DownloadUtils.download(minecraftMirror.getMinecraftJarDownloadUrl(versionJson),gamePath,version+".jar",minecraftMirror.getMinecraftJarSha1(versionJson));
            } catch (IOException | DownloadFailedException e) {
                e.printStackTrace();
                try {
                    DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftJarDownloadUrl(versionJson),gamePath,version+".jar",minecraftMirror.getMinecraftJarSha1(versionJson));
                } catch (IOException | DownloadFailedException ioException) {
                    ioException.printStackTrace();
                }
            }
        }));
    }

    private void downloadAssetIndexJson(){
        try {
            DownloadUtils.download(minecraftMirror.getMinecraftIndexJsonDownloadUrl(versionJson),indexPath+File.separator,JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getString("assets")+".json",minecraftMirror.getMinecraftIndexJsonSha1(versionJson));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftIndexJsonDownloadUrl(versionJson),indexPath+File.separator,JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getString("assets")+".json",minecraftMirror.getMinecraftIndexJsonSha1(versionJson));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            MinecraftDownloader.this.indexJson = new File(indexPath+File.separator+JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getString("assets")+".json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void downloadResourceObjects(){
        threadPool.execute(new Thread(() -> {
            ThreadPoolExecutor downloadThreadPool = new ThreadPoolExecutor(threadCount-10,threadCount-10,0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            Map<String,String> map = null;
            try {
                map = DownloadUtils.getObjects(indexJson);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Set<String> keySet = map.keySet();
            for (int i = 0;i < map.size();i++){
                String key = (String) keySet.toArray()[i];
                String hash = map.get(key);
                File path = new File(objectsPath+File.separator+hash.substring(0,2));
                path.mkdirs();
                downloadThreadPool.execute(new Thread(() -> {
                    try {
                        DownloadUtils.download(minecraftMirror.getMinecraftResourceDownloadUrl(hash.substring(0,2),hash),path.getPath(),hash,hash);
                    } catch (IOException | DownloadFailedException e) {
                        e.printStackTrace();
                        try {
                            DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftResourceDownloadUrl(hash.substring(0,2),hash),path.getPath(),hash,hash);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }));
            }
            downloadThreadPool.shutdown();
        }));
    }

    private void downloadLoggerConfig(){
        threadPool.execute(new Thread(() -> {
            try {
                DownloadUtils.download(minecraftMirror.getMinecraftLoggerConfigDownloadUrl(versionJson),loggingPath,JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("id"),minecraftMirror.getMinecraftLoggerConfigSha1(versionJson));
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    DownloadUtils.download(new OfficialMinecraftMirror().getMinecraftLoggerConfigDownloadUrl(versionJson),loggingPath,JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("id"),minecraftMirror.getMinecraftLoggerConfigSha1(versionJson));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }));
    }

    private void downloadLibraries(){
        threadPool.execute(new Thread(() -> {
            ThreadPoolExecutor downloadThreadPool = new ThreadPoolExecutor(10,10,0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            List<String> libraries = null,sha1 = null;
            try {
                sha1 = minecraftMirror.getMinecraftLibrariesSha1(versionJson);
                libraries = minecraftMirror.getMinecraftLibrariesDownloadUrl(versionJson);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for (int i = 0;i < libraries.size();i++){
                String url = libraries.get(i);
                String path = url.replaceAll(minecraftMirror.getLibUrl(),"").replaceAll("/",File.separator);
                new File(libPath+path).getParentFile().mkdirs();
                List<String> finalSha = sha1;
                int finalI = i;
                downloadThreadPool.execute(new Thread(() -> {
                    try {
                        DownloadUtils.download(url,(libPath+path).replaceAll("/",File.separator),"", finalSha.get(finalI));
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            DownloadUtils.download(url,(libPath+path).replaceAll("/",File.separator),"", finalSha.get(finalI));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }));
            }
            downloadThreadPool.shutdown();
        }));
    }

    private void downloadNativeLibraries(){
        threadPool.execute(new Thread(() -> {

            List<String> natives = null,sha1 = null;
            try {
                natives = minecraftMirror.getMinecraftNativeLibrariesDownloadUrl(versionJson);
                sha1 = minecraftMirror.getMinecraftNativeLibrariesSha1(versionJson);
            } catch (DownloadFailedException | OsNotSupportsException | FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 0;i < natives.size();i++){
                String url = natives.get(i);
                try {
                    if (minecraftMirror.getMinecraftNativeLibrariesDownloadUrl(versionJson) != null){
                        try {
                            DownloadUtils.download(url,nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),"",sha1.get(i));
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                DownloadUtils.download(url,nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),"",sha1.get(i));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                        try {
                            DownloadUtils.unzipNativeLibraries(nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),nativePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        new File(nativePath+File.separator+url.substring(url.lastIndexOf("/")+1),nativePath).delete();
                    }
                } catch (DownloadFailedException | OsNotSupportsException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void download() throws FileNotFoundException {
        downloadManifest();
        downloadJson();
        downloadAssetIndexJson();
        downloadJar();
        downloadResourceObjects();
        if (JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).containsKey("logging")) {
            downloadLoggerConfig();
        }
        downloadLibraries();
        downloadNativeLibraries();
        threadPool.shutdown();
    }
}