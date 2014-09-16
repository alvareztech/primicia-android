package com.danyalvarez.primicia.classes;

/**
 * Created by danyalvarez on 14/9/14.
 */
public class Resource {

    private long id;
    private String value;
    private String name;
    private int type;

    public Resource(long id, String name, int type) {
        this.id = id;
        this.value = "";
        this.name = name;
        this.type = type;
    }

    public Resource(String value, String name, int type) {
        this.id = 0;
        this.value = value;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
