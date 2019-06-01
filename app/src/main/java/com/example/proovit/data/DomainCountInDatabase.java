package com.example.proovit.data;

import com.google.gson.annotations.SerializedName;

public class DomainCountInDatabase {
    @SerializedName("domainScore")
   private Integer count;

    public DomainCountInDatabase(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
