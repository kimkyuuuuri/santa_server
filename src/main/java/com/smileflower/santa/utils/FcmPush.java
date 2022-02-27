package com.smileflower.santa.utils;


import lombok.*;
import okhttp3.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



import java.io.IOException;



@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)

public class FcmPush {


    private static final String apiKey="";
    private static final String senderId="";

    public void push(String  token,String data) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        okhttp3.RequestBody body = new FormBody.Builder()
                .add("to", token)
                .add("projeect_id", senderId)
                .add("notification", "")
                .add("data", data)
                .build();

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Authorization", "key=" + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {


            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage() + "\n ERROR");
            }


            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.code() + "\n" + response.body().string() + "\n SUCCESS");
                } else {
                    System.out.println(response.body());
                }
            }
        });
    }
}
