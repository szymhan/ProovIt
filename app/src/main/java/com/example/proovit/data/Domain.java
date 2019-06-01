package com.example.proovit.data;

import com.google.gson.annotations.SerializedName;

public class Domain {
    @SerializedName("")
    private java.lang.String domainLink;

    public Domain(String domainLink) {
        this.domainLink = domainLink;
    }

    public String getDomainLink() {
        return domainLink;
    }

    public void setDomainLink(String domainLink) {
        this.domainLink = domainLink;
    }
}
