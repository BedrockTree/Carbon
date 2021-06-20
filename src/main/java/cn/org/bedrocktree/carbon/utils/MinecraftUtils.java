package cn.org.bedrocktree.carbon.utils;

import cn.org.bedrocktree.carbon.exceptions.LoginException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class MinecraftUtils {

    public static final int PROFILE_NOT_EXIST = 0;

    public static final int PROFILE_DID_NOT_PAID = 1;

    public static final int PROFILE_DID_NOT_MIGRATED_TO_MOJANG = 2;

    public static final int PROFILE_NORMAL = 3;

    public static final String PASSWORD_CORRECT = "0";

    public static final String PASSWORD_INCORRECT = "1";

    public static boolean isUserExist(String playerName) throws Exception {
        if (getProfileStatus(playerName) == MinecraftUtils.PROFILE_NOT_EXIST) {
            return false;
        } else {
            return true;
        }
    }

    public static int getProfileStatus(String playerName) throws Exception {
        String resultStr;

        resultStr = HttpUtils.post("https://api.mojang.com/profiles/minecraft", "[\"{username}\"]".replace("{username}", playerName));

        JSONObject jsonObject = null;

        System.out.println(resultStr);

        if (!"[]".equals(resultStr)) {
            jsonObject = JSONObject.parseObject(resultStr.replace("[", "").replace("]", ""));
        }
        if ("[]".equals(resultStr)) {
            return MinecraftUtils.PROFILE_NOT_EXIST;
        } else if (Objects.requireNonNull(resultStr).contains("IllegalArgumentException")) {
            return MinecraftUtils.PROFILE_NOT_EXIST;
        } else if (jsonObject.containsKey("legacy")) {
            return MinecraftUtils.PROFILE_DID_NOT_MIGRATED_TO_MOJANG;
        } else if (jsonObject.containsKey("demo")) {
            return MinecraftUtils.PROFILE_DID_NOT_PAID;
        } else {
            return MinecraftUtils.PROFILE_NORMAL;
        }
    }

    public static String getClientToken(String playerName, String password) throws IOException, LoginException {
        JSONObject json = null;
        json = JSON.parseObject(HttpUtils.post("https://authserver.mojang.com/authenticate",
                "{\n" +
                        "  \"agent\": {\n" +
                        "    \"name\": \"Minecraft\",\"requestUser\": true,\n" +
                        "    \"version\": 1\n" +
                        "  },\n" +
                        "  \"username\": \""+playerName+"\",\n" +
                        "  \"password\": \""+password+"\"\n" +
                        "}"));


        if (Objects.requireNonNull(json).containsKey("accessToken")) {
            return json.getString("client_token");
        } else if (json.containsKey("errorMessage")) {
            throw new LoginException(json.getString("errorMessage"));
        } else {
            return null;
        }
    }

    public static String[] getToken(String playerName, String password) throws IOException, LoginException {
        JSONObject json = null;
        json = JSON.parseObject(HttpUtils.post("https://authserver.mojang.com/authenticate",
                "{\n" +
                        "  \"agent\": {\n" +
                        "    \"name\": \"Minecraft\",\"requestUser\": true,\n" +
                        "    \"version\": 1\n" +
                        "  },\n" +
                        "  \"username\": \"{username}\",\n\"password\": \"{password}\"\n}".replace("{username}", playerName).replace("{password}", password)));


        if (Objects.requireNonNull(json).containsKey("accessToken")) {
            return new String[]{json.getString("accessToken"), json.getJSONObject("selectedProfile").getString("name")};
        } else if (json.containsKey("errorMessage")) {
            throw new LoginException(json.getString("errorMessage"));
        } else {
            return null;
        }
    }

    public static String[] refreshToken(String playerName, String oldToken, String clientToken, String playerId) throws IOException {
        int code = 0;
        code = HttpUtils.getServerStatusCode("https://authserver.mojang.com/validate",
                "{\n" +
                        "    \"accessToken\": \""+oldToken+"\",\n" +
                        "    \"clientToken\": \""+clientToken+"\"\n" +
                        "}");


        if (code == 204) {

        } else {
            JSONObject json = null;
            json = JSON.parseObject(HttpUtils.post("https://authserver.mojang.com/refresh",
                    "{\n" +
                            "    \"accessToken\": \""+oldToken+"\",\"requestUser\": true,\n" +
                            "    \"clientToken\": \""+clientToken+"\", \n" +
                            "                                        \n" +
                            "    \"selectedProfile\": {\n" +
                            "        \"id\": \""+playerId+"\", \n" +
                            "        \"name\": \""+playerName+"\"\n" +
                            "    }\n" +
                            "}"));


            return new String[]{json.getString("accessToken"), json.getJSONObject("selectedProfile").getString("name")};
        }
        return null;
    }
    public static String getUuid(String playerName) throws IOException, LoginException {
        JSONObject json = null;
        json = JSONObject.parseObject(HttpUtils.post("https://api.mojang.com/profiles/minecraft",
                "[\"" + playerName + "\"]").replace("[", "").replace("]", ""));
        return json.getString("id");
    }
}