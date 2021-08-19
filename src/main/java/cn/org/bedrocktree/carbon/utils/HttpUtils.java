package cn.org.bedrocktree.carbon.utils;

import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class HttpUtils {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient();

    public static String post(String url, String json) throws IOException {
        System.out.println(url+"：\n" +json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            System.out.println("\n\n"+result+"\n\n\n");
            return result;
        }
    }

    public static String post(String url, String json,String[] name,String[] value) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println(url+"：\n" +json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        for (int i = 0; i < name.length;i++){
            requestBuilder = requestBuilder.header(name[i],value[i]);
        }
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            System.out.println("\n\n"+result+"\n\n\n");
            return result;
        }
    }

    public static String get(String requestUrl) throws IOException {
        StringBuffer buffer = new StringBuffer();
        URL url = new URL(requestUrl);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setDoInput(true);
        httpUrlConn.setRequestMethod("GET");

        httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        buffer = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }

    public static String get(String url,String[] name,String[] value) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        for (int i = 0;i < name.length;i++){
            requestBuilder = requestBuilder.addHeader(name[i],value[i]);
        }
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static int getServerStatusCode(String url,String json)throws IOException{
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
    }
}