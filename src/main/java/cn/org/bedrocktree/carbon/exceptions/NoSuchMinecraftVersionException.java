package cn.org.bedrocktree.carbon.exceptions;

import cn.org.bedrocktree.carbon.init.InitFiles;

import java.io.File;

public class NoSuchMinecraftVersionException extends Exception{
    public NoSuchMinecraftVersionException(String version){
        super("Minecraft version "+version+" was not found in "+ InitFiles.CONFIG_DIRECTORY+ File.separator+"version_manifest.json");
    }
}
