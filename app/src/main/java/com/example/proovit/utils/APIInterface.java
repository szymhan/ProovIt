package com.example.proovit.utils;

import com.example.proovit.data.Article;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("reports/article")
    Call<Article> reportArticle(@Body Article article);
}
