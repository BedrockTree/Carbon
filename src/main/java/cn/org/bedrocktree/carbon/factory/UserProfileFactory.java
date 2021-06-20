package cn.org.bedrocktree.carbon.factory;

import cn.org.bedrocktree.carbon.entity.UserProfile;
import cn.org.bedrocktree.carbon.exceptions.AccountNotExistException;
import cn.org.bedrocktree.carbon.exceptions.LoginException;
import cn.org.bedrocktree.carbon.utils.MicrosoftUtils;
import cn.org.bedrocktree.carbon.utils.MinecraftUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserProfileFactory {

    public static UserProfile createMojangUserProfile(String email,String password) throws LoginException, IOException {
        UserProfile userProfile = new UserProfile();
        userProfile.setType("mojang");
        userProfile.setEmail(email);
        userProfile.setToken(Objects.requireNonNull(MinecraftUtils.getToken(email, password))[0]);
        userProfile.setPlayerName(Objects.requireNonNull(MinecraftUtils.getToken(email, password))[1]);
        userProfile.setClientToken(MinecraftUtils.getClientToken(email, password));
        userProfile.setName(userProfile.getPlayerName());
        userProfile.setUuid(MinecraftUtils.getUuid(userProfile.getPlayerName()));
        return userProfile;
    }

    public static UserProfile createMicrosoftUserProfile() throws Exception {
        UserProfile userProfile = new UserProfile();
        Map<String,String> map = MicrosoftUtils.login();
        String token = map.get("access_token");
        String profileJson = MicrosoftUtils.getProfile(token);
        userProfile.setType("Microsoft");
        userProfile.setToken(token);
        userProfile.setRefreshToken(map.get("refresh_token"));
        userProfile.setUuid(MicrosoftUtils.getUuid(profileJson));
        userProfile.setPlayerName(MicrosoftUtils.getPlayerName(profileJson));
        userProfile.setName(MicrosoftUtils.getPlayerName(profileJson));
        if (MicrosoftUtils.isAccountHasMinecraft(token)){
            return userProfile;
        }else {
            throw new AccountNotExistException("This Microsoft account doesn't have minecraft");
        }

    }

    public static UserProfile createOfflineUserProfile(String playerName){
        UserProfile userProfile = new UserProfile();
        userProfile.setType("offline");
        userProfile.setName(playerName);
        userProfile.setUuid(UUID.randomUUID().toString());
        userProfile.setPlayerName(playerName);
        userProfile.setToken("");
        return userProfile;
    }
}
