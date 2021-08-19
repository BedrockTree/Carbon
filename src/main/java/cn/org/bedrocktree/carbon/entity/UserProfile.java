package cn.org.bedrocktree.carbon.entity;

import cn.org.bedrocktree.carbon.exceptions.AccountNotExistException;
import cn.org.bedrocktree.carbon.utils.MicrosoftUtils;
import cn.org.bedrocktree.carbon.utils.MinecraftUtils;

import java.io.IOException;
import java.util.Map;

public class UserProfile {

    private String name;

    private String playerName;

    private String uuid;

    private String type;

    private String token;

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private String clientToken;

    private String refreshToken;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void refreshToken() throws IOException, AccountNotExistException {
        if ("Mojang".equals(this.type)){
            this.token = MinecraftUtils.refreshToken(playerName,token,clientToken,uuid)[0];
        }else if ("Microsoft".equals(this.type)){
            Map<String, String> map = MicrosoftUtils.login(refreshToken);
            this.refreshToken = map.get("refresh_token");
            this.token = map.get("access_token");
        }
    }
}
