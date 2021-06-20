package cn.org.bedrocktree.carbon.utils;

import cn.org.bedrocktree.carbon.exceptions.AccountNotExistException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.alm.oauth2.useragent.AuthorizationException;
import com.microsoft.alm.oauth2.useragent.UserAgent;
import com.microsoft.alm.oauth2.useragent.UserAgentImpl;
import com.microsoft.alm.oauth2.useragent.AuthorizationResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MicrosoftUtils {

    public static final String MICROSOFT_CODE_URL = "https://login.live.com/oauth20_authorize.srf" +
            "?client_id=00000000402b5328" +
            "&response_type=code" +
            "&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL" +
            "&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";

    public static final String MICROSOFT_TOKEN_URL = "https://login.live.com/oauth20_token.srf";

    public static final String XBOX_LIVE_URL = "https://user.auth.xboxlive.com/user/authenticate";

    public static final String XSTS_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";

    public static final String MINECRAFT_LOGIN_URL = "https://api.minecraftservices.com/authentication/login_with_xbox";

    public static final String MINECRAFT_ACCOUNT_CHECK_URL = "https://api.minecraftservices.com/entitlements/mcstore";

    public static final String MINECRAFT_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";

    public static String getMicrosoftLiveCode() throws URISyntaxException, AuthorizationException {
        UserAgent userAgent = new UserAgentImpl();
        AuthorizationResponse authorizationResponse = userAgent.requestAuthorizationCode(new URI(MICROSOFT_CODE_URL),
                new URI("https://login.live.com/oauth20_desktop.srf?code=codegoeshere&lc=1033"));
        return authorizationResponse.getCode();
    }

    public static String[] getMicrosoftToken(String code) throws IOException {
        String jsonStr = HttpUtils.get(MICROSOFT_TOKEN_URL+"/?client_id=00000000402b5328&code="+code+"&" +
                "grant_type=authorization_code&redirect_uri=https://login.live.com/oauth20_desktop.srf&" +
                "scope=service::user.auth.xboxlive.com::MBI_SSL");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return new String[] {jsonObject.getString("access_token"),jsonObject.getString("refresh_token")};
    }

    public static String[] refreshMicrosoftToken(String refreshToken) throws IOException {
        String jsonStr = HttpUtils.get(MICROSOFT_TOKEN_URL+"/?client_id=00000000402b5328&refresh_token="+refreshToken+"&" +
                "grant_type=authorization_code&redirect_uri=https://login.live.com/oauth20_desktop.srf&" +
                "scope=service::user.auth.xboxlive.com::MBI_SSL");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return new String[] {jsonObject.getString("access_token"),jsonObject.getString("refresh_token")};
    }

    public static String getXboxLiveToken(String msToken) throws IOException {
        String jsonStr = HttpUtils.post(XBOX_LIVE_URL, " {\n" +
                        "    \"Properties\": {\n" +
                        "        \"AuthMethod\": \"RPS\",\n" +
                        "        \"SiteName\": \"user.auth.xboxlive.com\",\n" +
                        "        \"RpsTicket\": \""+msToken+"\"\n" +
                        "    },\n" +
                        "    \"RelyingParty\": \"http://auth.xboxlive.com\",\n" +
                        "    \"TokenType\": \"JWT\"\n" +
                        " }");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject.getString("Token");
    }

    public static String getXstsToken(String xblToken) throws IOException {
        String jsonStr = HttpUtils.post(XSTS_URL, " {\n" +
                        "    \"Properties\": {\n" +
                        "        \"SandboxId\": \"RETAIL\",\n" +
                        "        \"UserTokens\": [\n" +
                        "            \""+xblToken+"\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"RelyingParty\": \"rp://api.minecraftservices.com/\",\n" +
                        "    \"TokenType\": \"JWT\"\n" +
                        " }", new String[]{"Content-Type", "Accept"},
                new String[]{"application/json", "application/json"});
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getString("Token");
    }

    public static String getXstsUserHash(String xblToken) throws IOException, AccountNotExistException {
        String jsonStr = HttpUtils.post(XSTS_URL, " {\n" +
                        "    \"Properties\": {\n" +
                        "        \"SandboxId\": \"RETAIL\",\n" +
                        "        \"UserTokens\": [\n" +
                        "            \""+xblToken+"\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"RelyingParty\": \"rp://api.minecraftservices.com/\",\n" +
                        "    \"TokenType\": \"JWT\"\n" +
                        " }", new String[]{"Content-Type", "Accept"},
                new String[]{"application/json", "application/json"});
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (jsonObject.containsKey("XErr")){
            if (jsonStr.contains("2148916233")) {
                throw new AccountNotExistException("The account doesn't have an Xbox account. Once they sign up for one (or login through minecraft.net to create one) then they can proceed with the login. This shouldn't happen with accounts that have purchased Minecraft with a Microsoft account, as they would've already gone through that Xbox signup process.");
            }else if (jsonStr.contains("2148916238")) {
                throw new AccountNotExistException("The account is a child (under 18) and cannot proceed unless the account is added to a Family by an adult. This only seems to occur when using a custom Microsoft Azure application. When using the Minecraft launchers client id, this doesn't trigger.");
            }else {
                throw new AccountNotExistException("An unknown error happens in checking XBoxLive Account");
            }
        }
        return jsonObject.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs");
    }

    public static String getMinecraftToken(String xstsToken, String userHash) throws IOException {
        String jsonStr = HttpUtils.post(MINECRAFT_LOGIN_URL, " {\n" +
                "    \"identityToken\": \"XBL3.0 x="+userHash+";"+xstsToken+"\"\n" +
                " }".replace("{userHash}", userHash).replace("{xstsToken}", xstsToken));
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject.getString("access_token");
    }


    public static Map<String,String> login() throws Exception {
        String[] microsoftTokens = getMicrosoftToken(getMicrosoftLiveCode());
        String xboxLiveToken = getXboxLiveToken(microsoftTokens[0]);
        String token = getMinecraftToken(getXstsToken(xboxLiveToken), getXstsUserHash(xboxLiveToken));
        Map<String,String> map = new HashMap<>();
        map.put("access_token",token);
        map.put("refresh_token",microsoftTokens[1]);
        return map;
    }

    public static Map<String,String> login(String refreshToken) throws IOException, AccountNotExistException {
        String[] microsoftTokens = refreshMicrosoftToken(refreshToken);
        String xboxLiveToken = getXboxLiveToken(microsoftTokens[0]);
        String token = getMinecraftToken(getXstsToken(xboxLiveToken), getXstsUserHash(xboxLiveToken));
        Map<String,String> map = new HashMap<>();
        map.put("access_token",token);
        map.put("refresh_token",microsoftTokens[1]);
        return map;
    }

    public static boolean isAccountHasMinecraft(String token) throws IOException {
        String jsonStr = HttpUtils.get(MINECRAFT_ACCOUNT_CHECK_URL,new String[] {"Authorization"},new String[] {"Bearer "+token});
        System.out.println(jsonStr);
        JSONArray jsonArray = JSONObject.parseObject(jsonStr).getJSONArray("items");
        if (jsonArray.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public static String getProfile(String token) throws IOException {
        String jsonStr = HttpUtils.get(MINECRAFT_PROFILE_URL,new String[] {"Authorization"},new String[] {"Bearer "+token});
        System.out.println(jsonStr);
        return jsonStr;
    }

    public static String getUuid(String json) throws AccountNotExistException {
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject.containsKey("error")){
            throw new AccountNotExistException("This Microsoft account has not buyMinecraft yet");
        }
        return jsonObject.getString("id");
    }

    public static String getPlayerName(String json) throws AccountNotExistException {
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject.containsKey("error")){
            throw new AccountNotExistException("This Microsoft account has not buyMinecraft yet");
        }
        return jsonObject.getString("name");
    }

}