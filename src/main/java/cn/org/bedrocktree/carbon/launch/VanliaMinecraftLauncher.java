package cn.org.bedrocktree.carbon.launch;

import cn.org.bedrocktree.carbon.entity.MinecraftVersion;
import cn.org.bedrocktree.carbon.entity.UserProfile;
import cn.org.bedrocktree.carbon.exceptions.AccountNotExistException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VanliaMinecraftLauncher {

    MinecraftVersion minecraftVersion;

    UserProfile userProfile;

    public VanliaMinecraftLauncher(MinecraftVersion version,UserProfile user){
        this.minecraftVersion = version;
        this.userProfile = user;
    }

    public void launch() throws AccountNotExistException, IOException {
        String command = minecraftVersion.getLaunchCommandLine(userProfile);
        System.out.println(command);
        Process process = Runtime.getRuntime().exec(command);
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null){
            System.out.println(line);
        }
    }

}
