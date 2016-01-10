package com.chest.blue.filechest;

/**
 * Created by Blue on 7/26/2015.
 */
public class Links {

    private final String name;
    private final String link;
    private final String linkid;

    public Links(final String name, String linkid,String link) {
        this.name = name;
        this.linkid = linkid;
        this.link = link;
    }


    public String getName() {return name;}

    public String getLinkId() {
        return linkid;
    }

    public String getLink() {
        return link;
    }
}
