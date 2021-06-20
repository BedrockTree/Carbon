package cn.org.bedrocktree.carbon.download;

import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class MinecraftMirror {

    public abstract String getMinecraftManifestJsonDownloadUrl();

    public abstract String getMinecraftJsonDownloadUrl(File manifestJson, String version) throws DownloadFailedException, NoSuchMinecraftVersionException, FileNotFoundException;

    public abstract String getMinecraftJarDownloadUrl(File versionJson) throws DownloadFailedException, FileNotFoundException;

    public abstract String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException;

    public abstract String getMinecraftResourceDownloadUrl(String hashFirst2, String hash) throws DownloadFailedException, FileNotFoundException;

    public abstract String getMinecraftLoggerConfigDownloadUrl(File versionJson) throws FileNotFoundException;

    public abstract String getMinecraftLibrariesDownloadUrl(String childPath);

    public abstract String getMinecraftNativeLibrariesDownloadUrl(File versionJson, String childPath) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException;

}
