package com.example.proovit.data;

public class Article {

    private String userUUID;
    private String articleLink;
    private String reason;

    public Article(String userUUID, String articleLink, String reason) {
        this.userUUID = userUUID;
        this.articleLink = articleLink;
        this.reason = reason;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
