package cn.com.findfine.jddaojia.http;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {

    private static OkHttpClient client = new OkHttpClient();

    public static void requestGet(String url, Callback callback) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void requestPost(String url, FormBody.Builder builder, Callback callback) {

//        builder.add("token", "7562901a1807841e42b8fb60fc0cad9f5af037411aa662.35496722")
//                .add("m", "search")
//                .add("a", "getrankuser")
//                .add("c", "rank")
//                .add("uid", "119834721")
//                .add("isJailbreak", "0");

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
