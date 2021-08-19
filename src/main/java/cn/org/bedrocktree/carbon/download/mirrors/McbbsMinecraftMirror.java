package cn.org.bedrocktree.carbon.download.mirrors;

import cn.org.bedrocktree.carbon.download.MinecraftMirror;
import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class McbbsMinecraftMirror extends MinecraftMirror {

    private static final String MCBBS_BASE_URL = "https://download.mcbbs.net/";

    OfficialMinecraftMirror official = new OfficialMinecraftMirror();

    @Override
    public String getLibUrl() {
        return MCBBS_BASE_URL+"maven/";
    }

    @Override
    public String getMinecraftManifestJsonDownloadUrl() {
        return official.getMinecraftManifestJsonDownloadUrl().replace("https://launchermeta.mojang.com/",MCBBS_BASE_URL);
    }

    @Override
    public String getMinecraftJsonDownloadUrl(File manifestJson, String version) throws DownloadFailedException, NoSuchMinecraftVersionException, FileNotFoundException {
        return official.getMinecraftJsonDownloadUrl(manifestJson, version).replace("https://launchermeta.mojang.com/",MCBBS_BASE_URL);
    }

    @Override
    public String getMinecraftJarDownloadUrl(File versionJson) throws DownloadFailedException {
        return MCBBS_BASE_URL+"version/{version}/client".replace("{version}",versionJson.getName().replace(".json",""));
    }

    @Override
    public String getMinecraftJarSha1(File versionJson) throws DownloadFailedException, FileNotFoundException {
        return official.getMinecraftJarSha1(versionJson);
    }

    @Override
    public String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException {
        return official.getMinecraftIndexJsonDownloadUrl(versionJson).replace("https://launchermeta.mojang.com/",MCBBS_BASE_URL);
    }

    @Override
    public String getMinecraftIndexJsonSha1(File versionJson) throws FileNotFoundException {
        return official.getMinecraftIndexJsonSha1(versionJson);
    }

    @Override
    public String getMinecraftResourceDownloadUrl(String hashFirst2, String hash) throws DownloadFailedException, FileNotFoundException {
        return official.getMinecraftResourceDownloadUrl(hashFirst2, hash).replace("http://resources.download.minecraft.net/",MCBBS_BASE_URL+"assets/");
    }

    @Override
    public String getMinecraftLoggerConfigDownloadUrl(File versionJson) throws FileNotFoundException {
        return official.getMinecraftLoggerConfigDownloadUrl(versionJson).replace("https://launcher.mojang.com/",MCBBS_BASE_URL);
    }

    @Override
    public String getMinecraftLoggerConfigSha1(File versionJson) throws FileNotFoundException {
        return official.getMinecraftLoggerConfigSha1(versionJson);
    }

    @Override
    public List<String> getMinecraftLibrariesDownloadUrl(File versionJson) throws FileNotFoundException {
        List<String> list = official.getMinecraftLibrariesDownloadUrl(versionJson),result = new ArrayList<>();
        for (String url:list){
            result.add(url.replace("https://libraries.minecraft.net/",MCBBS_BASE_URL+"maven/"));
        }
        return result;
    }

    @Override
    public List<String> getMinecraftLibrariesSha1(File versionJson) throws FileNotFoundException {
        return official.getMinecraftLibrariesSha1(versionJson);
    }

    @Override
    public List<String> getMinecraftNativeLibrariesDownloadUrl(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        List<String> list = official.getMinecraftNativeLibrariesDownloadUrl(versionJson),result = new ArrayList<>();
        for (String url:list){
            result.add(url.replace("https://libraries.minecraft.net/",MCBBS_BASE_URL+"maven/"));
        }
        return result;
    }

    @Override
    public List<String> getMinecraftNativeLibrariesSha1(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        return official.getMinecraftNativeLibrariesSha1(versionJson);
    }
}
