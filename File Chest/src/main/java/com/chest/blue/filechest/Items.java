package com.chest.blue.filechest;

/**
 * Created by Blue on 6/28/2015.
 */
public class Items {
    private final String name;
    private final String id;
    private final String fileid;
    private final String parentid;
    private final String modified;

    public Items(final String name, final String id,String fileid, String parentid, String modified ) {
        this.name = name;
        this.id = id;
        this.fileid = fileid;
        this.parentid = parentid;
        this.modified = modified;
    }


    public String getName() {return name;}

    public String getId() {
        return id;
    }

    public String getFileId() {
        return fileid;
    }

    public String getParentId() {return parentid;}

    public String getModified() {return modified;}
}
