package com.smileflower.santa.utils;


import lombok.*;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



import java.io.IOException;



@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@Component
public class FcmPush {


    private static final String apiKey="AAAA2LFAFIk:APA91bHDz5DZ9_iqCYfA5Iom-QXPb9mJuBdlP_2EMzwaSNf-Vg2qCg30bvXieipFyFXMh-mq9u6o758TfaFgTo9o9YMQUds5PT_WSEbyOjp-YyN-fOa6XtXChIl86zNH5LbwuMPvm1LJ";
    private static final String senderId="930686702729";

    public void push(String  token,String data) {
        if (token != null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            okhttp3.RequestBody body = new FormBody.Builder()
                    .add("to", token)
                    .add("projeect_id", senderId)
                    .add("notification", "")
                    .add("data", data)
                    .build();
            System.out.println(apiKey);

            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=" + apiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {


                public void onFailure(Call call, IOException e) {

                    // System.out.println(e.getMessage() + "\n ERROR");
                }


                public void onResponse(Call call, Response response) throws IOException {
                    // if (response.isSuccessful()) {
                    //   System.out.println(response.code() + "\n" + response.body().string() + "\n SUCCESS");
                    //} else {
                    //  System.out.println(response.body());
                    //}
                }
            });
        }
    }
}
