package com.example.proovit.utils;

import com.example.proovit.data.Article;
import com.example.proovit.data.ArticleCountInDatabase;
import com.example.proovit.data.Domain;
import com.example.proovit.data.User;
import com.example.proovit.data.UserUUID;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("reports/article")
    Call<Article> reportArticle(@Body Article article);
    @GET("reports/stat")
    Call<ArticleCountInDatabase> checkNumberOfArticleReports(@Query("link") String address);
    @POST("users/login")
    Call<UserUUID> loginUser(@Body User user);
    @GET("domain/user")
    Call<List<String>> getDomains(@Query("userUUID") String userUUID);
    @GET("domain/not-user")
    Call<List<String>> getAllDomains(@Query("userUUID") String userUUID);
}
