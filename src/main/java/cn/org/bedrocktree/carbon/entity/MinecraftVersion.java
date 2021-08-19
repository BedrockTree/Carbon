package cn.org.bedrocktree.carbon.entity;

import cn.org.bedrocktree.carbon.Carbon;
import cn.org.bedrocktree.carbon.exceptions.AccountNotExistException;
import cn.org.bedrocktree.carbon.utils.DownloadUtils;
import cn.org.bedrocktree.carbon.utils.JSONUtils;
import cn.org.bedrocktree.carbon.utils.StreamUtils;
import cn.org.bedrocktree.carbon.utils.SystemUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MinecraftVersion {

    private boolean isVersionIsolation;

    private File versionJson;

    private JSONObject versionJsonObject;

    private String version;

    private String versionType;

    private String assetIndexVersion;

    private String mainClass;

    private String gameDirectory;

    private String assetsDirectory;

    private String librariesDirectory;

    private String nativePath;

    private String log4jConfig;

    private ArrayList<String> libraries = new ArrayList<String>();

    public MinecraftVersion(File versionJson, String versionName, boolean isVersionIsolation) throws FileNotFoundException {
        this.versionJson = versionJson;
        this.isVersionIsolation = isVersionIsolation;
        this.versionJsonObject = JSONObject.parseObject(StreamUtils.readJsonFile(versionJson));
        this.version = versionJsonObject.getString("id");
        this.versionType = versionJsonObject.getString("type");
        this.assetIndexVersion = versionJsonObject.getJSONObject("assetIndex").getString("id");
        this.mainClass = versionJsonObject.getString("mainClass");
        if (this.isVersionIsolation) {
            this.gameDirectory = Carbon.ROOT_LOCATION + ".minecraft" + File.separator + "versions" + File.separator + versionName + File.separator;
        }
        JSONArray librariesArray = versionJsonObject.getJSONArray("libraries");
        for (int i = 0; i < librariesArray.size(); i++) {
            JSONObject json = librariesArray.getJSONObject(i);
            if (json.getJSONObject("downloads").containsKey("artifact")) {
                if (json.containsKey("rules")) {
                    JSONArray rules = json.getJSONArray("rules");
                    if (JSONUtils.rulesJudgmenter(rules)) {
                        this.libraries.add(json.getJSONObject("downloads").getJSONObject("artifact").getString("path"));
                    }
                } else {
                    this.libraries.add(json.getJSONObject("downloads").getJSONObject("artifact").getString("path"));
                }
            }
        }
        this.nativePath = gameDirectory+ "natives";
        this.assetsDirectory = Carbon.ROOT_LOCATION + ".minecraft" + File.separator + "assets" + File.separator;
        this.librariesDirectory = Carbon.ROOT_LOCATION + ".minecraft" + File.separator + "libraries"+File.separator;
        if (JSONObject.parseObject(StreamUtils.readJsonFile(versionJson)).containsKey("logging")){
            this.log4jConfig = assetsDirectory + File.separator + "log_configs" + File.separator + versionJsonObject.getJSONObject("logging").getJSONObject("client").getJSONObject("file").getString("id");
        }
    }

    public String getLaunchCommandLine(UserProfile userProfile) throws AccountNotExistException, IOException {
        String pathSeparator;
        if (SystemUtils.getSystemName().contains("Windows")){
            pathSeparator = ";";
        }else {
            pathSeparator = ":";
        }
        String launchCommandLine = "java";
        String cp = " -cp ";
        launchCommandLine += " -Djava.library.path=" + nativePath;
        if (versionJsonObject.containsKey("logging")){
            launchCommandLine += " -Dlog4j.configurationFile="+log4jConfig;
        }
        launchCommandLine += " -Dminecraft.launcher.brand="+Carbon.APPLICATION_NAME;
        launchCommandLine += " -Dminecraft.launcher.version="+Carbon.VERSION;
        for (String lib: DownloadUtils.getLibraries(versionJson)){
            cp += librariesDirectory+lib+pathSeparator;
        }
        cp += gameDirectory+version+".jar";
        launchCommandLine += cp;
        launchCommandLine += " "+mainClass;
        if (versionJsonObject.containsKey("minecraftArguments") && versionJsonObject.getString("minecraftArguments").contains("--userProperties")){
            launchCommandLine += " --userProperties {\"registrationCountry\":[\"CN\"]}";
        }
        if (versionJsonObject.containsKey("minecraftArguments") && versionJsonObject.getString("minecraftArguments").contains("${auth_player_name} ${auth_session}")){
            launchCommandLine += " "+userProfile.getPlayerName();
            launchCommandLine += " "+userProfile.getToken();
            launchCommandLine += " --gameDir " + gameDirectory;
            launchCommandLine += " --assetsDir " + assetsDirectory;
            return launchCommandLine;
        }else {
            launchCommandLine += " --username " + userProfile.getPlayerName();
            launchCommandLine += " --version " + version+"/Carbon";
            launchCommandLine += " --gameDir " + gameDirectory;
            launchCommandLine += " --assetsDir " + assetsDirectory;
            launchCommandLine += " --assetIndex " + assetIndexVersion;
            launchCommandLine += " --uuid " + userProfile.getUuid();
            launchCommandLine += " --accessToken " + userProfile.getToken();
            launchCommandLine += " --versionType " + versionType;
            return launchCommandLine;
        }
    }
}