package cn.org.bedrocktree.carbon.download.mirrors;

import cn.org.bedrocktree.carbon.download.MinecraftMirror;
import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;

import java.io.File;
import java.io.FileNotFoundException;

public class McbbsMinecraftMirror extends MinecraftMirror {

    private static final String MCBBS_BASE_URL = "https://download.mcbbs.net/";

    OfficialMinecraftMirror official = new OfficialMinecraftMirror();

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
    public String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException {
        return official.getMinecraftIndexJsonDownloadUrl(versionJson).replace("https://launchermeta.mojang.com/",MCBBS_BASE_URL);
    }

    @Override
    public String getMinecraftResourceDownloadUrl(String hashFirst2, String hash) throws DownloadFailedException, FileNotFoundException {
        return official.getMinecraftResourceDownloadUrl(hashFirst2, hash).replace("http://resources.download.minecraft.net/",MCBBS_BASE_URL+"assets/");
    }

    @Override
    public String getMinecraftLoggerConfigDownloadUrl(File versionJson) throws FileNotFoundException {
        //return official.getMinecraftLoggerConfigDownloadUrl(versionJson).replace("https://launcher.mojang.com/",MCBBS_BASE_URL);
        return official.getMinecraftLoggerConfigDownloadUrl(versionJson);
    }

    @Override
    public String getMinecraftLibrariesDownloadUrl(String childPath) {
        return official.getMinecraftLibrariesDownloadUrl(childPath).replace("https://libraries.minecraft.net/",MCBBS_BASE_URL+"maven/");
    }

    @Override
    public String getMinecraftNativeLibrariesDownloadUrl(File versionJson, String childPath) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException {
        if (official.getMinecraftNativeLibrariesDownloadUrl(versionJson, childPath) != null) {
            return official.getMinecraftNativeLibrariesDownloadUrl(versionJson, childPath).replace("https://libraries.minecraft.net/", MCBBS_BASE_URL + "maven/");
        }else {
            return null;
        }
    }
}
