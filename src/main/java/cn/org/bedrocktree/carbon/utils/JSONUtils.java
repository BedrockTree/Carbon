package cn.org.bedrocktree.carbon.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
    public static boolean rulesJudgmenter(JSONArray rules){
        boolean result = false;
        for (int i = 0;i < rules.size();i++){
            JSONObject jsonObject = rules.getJSONObject(i);
            if (jsonObject.containsKey("os")){
                if (isOsCorrect(jsonObject)){
                    if ("disallow".equals(jsonObject.getString("action"))){
                        result = false;
                    }else {
                        result = true;
                    }
                }else {
                    if ("disallow".equals(jsonObject.getString("action"))){
                        result = true;
                    }else {
                        result = false;
                    }
                }
            }
        }
        System.out.println(!result);
        return !result;
    }
    private static boolean isOsCorrect(JSONObject jsonObject){
        boolean isOsCorrect = false;
        if (jsonObject.containsKey("os")) {
            String os = jsonObject.getJSONObject("os").getString("name");
            switch (os) {
                case "osx":
                    if (SystemUtils.getSystemName().contains("Mac")) {
                        isOsCorrect = true;
                        break;
                    } else {
                        isOsCorrect = false;
                    }
                case "windows":
                    if (SystemUtils.getSystemName().contains("Windows")) {
                        isOsCorrect = true;
                        break;
                    } else {
                        isOsCorrect = false;
                    }
                default:
                    if (SystemUtils.getSystemName().contains("Linux")) {
                        isOsCorrect = true;
                    } else {
                        isOsCorrect = false;
                    }
            }
        }
        return isOsCorrect;
    }
}
