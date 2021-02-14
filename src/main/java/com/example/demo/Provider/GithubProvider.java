package com.example.demo.Provider;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public static final String logUrl = "https://github.com/login/oauth/access_token";
    public static final String userUrl = "https://api.github.com/user";

    public String GetAccesstoken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url(logUrl)
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            String token=string.split("&")[0].split("=")[1];String[] split = string.split("&");
            return token;
        }catch (IOException e) {
        }
        return null;
    }



    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", "token " + accessToken)
                .url(userUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubuser=JSON.parseObject(string, GithubUser.class);
            return githubuser;
        } catch (IOException e){
        }
        return null;
    }
}

