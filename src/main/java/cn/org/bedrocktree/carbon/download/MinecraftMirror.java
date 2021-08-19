package cn.org.bedrocktree.carbon.download;

import cn.org.bedrocktree.carbon.exceptions.DownloadFailedException;
import cn.org.bedrocktree.carbon.exceptions.NoSuchMinecraftVersionException;
import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public abstract class MinecraftMirror {

    public abstract String getLibUrl();

    public abstract String getMinecraftManifestJsonDownloadUrl();

    public abstract String getMinecraftJsonDownloadUrl(File manifestJson, String version) throws DownloadFailedException, NoSuchMinecraftVersionException, FileNotFoundException;

    public abstract String getMinecraftJarDownloadUrl(File versionJson) throws DownloadFailedException, FileNotFoundException;

    public abstract String getMinecraftJarSha1(File versionJson) throws DownloadFailedException, FileNotFoundException;

    public abstract String getMinecraftIndexJsonDownloadUrl(File versionJson) throws FileNotFoundException;

    public abstract String getMinecraftIndexJsonSha1(File versionJson) throws FileNotFoundException;

    public abstract String getMinecraftResourceDownloadUrl(String hashFirst2, String hash) throws DownloadFailedException, FileNotFoundException;

    public abstract String getMinecraftLoggerConfigDownloadUrl(File versionJson) throws FileNotFoundException;

    public abstract String getMinecraftLoggerConfigSha1(File versionJson) throws FileNotFoundException;

    public abstract List<String> getMinecraftLibrariesDownloadUrl(File versionJson) throws FileNotFoundException;

    public abstract List<String> getMinecraftLibrariesSha1(File versionJson) throws FileNotFoundException;

    public abstract List<String> getMinecraftNativeLibrariesDownloadUrl(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException;

    public abstract List<String> getMinecraftNativeLibrariesSha1(File versionJson) throws DownloadFailedException, OsNotSupportsException, FileNotFoundException;
}
