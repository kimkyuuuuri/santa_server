package com.smileflower.santa.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonParseException;
import lombok.*;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;



@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@Component
public class FcmPush {

private ObjectMapper objectMapper=new ObjectMapper();
    private static final String apiKey="AAAA2LFAFIk:APA91bHDz5DZ9_iqCYfA5Iom-QXPb9mJuBdlP_2EMzwaSNf-Vg2qCg30bvXieipFyFXMh-mq9u6o758TfaFgTo9o9YMQUds5PT_WSEbyOjp-YyN-fOa6XtXChIl86zNH5LbwuMPvm1LJ";
    private static final String senderId="930686702729";

    public void iosPush(String  token,String title,String data) throws IOException {

        if (token != null) {
            System.out.println(data);
            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody body = new FormBody.Builder()
                    .add("to", token)
                    .add("project_id", senderId)
                    .add("notification", "")
                    .add("content-available","1")
                    .add("priority","high")
                    .build();


            iosSendMessageTo(
                    token,
                    title,
                    data);
           ResponseEntity.ok().build();


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
                     if (response.isSuccessful()) {

                     // System.out.println(response.code() + "\n" + response.body().string() + "\n SUCCESS");
                    } else {
                      //System.out.println(response.body());
                    }
                }
            });
        }


    }
    public void androidPush(String  token,String title,String data) throws IOException {

        if (token != null) {

            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody body = new FormBody.Builder()
                    .add("to", token)
                    .add("project_id", senderId)
                    .add("notification", "")
                    .add("content-available","1")
                    .add("priority","high")
                    .build();


            androidSendMessageTo(
                    token,
                    title,
                    data);
            ResponseEntity.ok().build();


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

                         //System.out.println(response.code() + "\n" + response.body().string() + "\n SUCCESS");
                    } else {
                        //System.out.println(response.body());
                       // System.out.println(response.code());
                    }
                }
            });
        }


    }



    private String iosMakeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
       PushModel fcmMessage = PushModel.builder()
                .message(PushModel.Message.builder()
                        .token(targetToken)
                        .notification(PushModel.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }
    private String androidMakeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        AndroidPushModel fcmMessage = AndroidPushModel.builder()
                .message(AndroidPushModel.Message.builder()
                        .token(targetToken)
                        .data(body)
                        .notification(AndroidPushModel.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )

                        .build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }



    public void iosSendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = iosMakeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                message);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/santa-dev-7262a/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();


    }

    public void androidSendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = iosMakeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                message);
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/santa-dev-7262a/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();


    }

    private String getAccessToken() throws IOException {

        String firebaseConfigPath = "/firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}


