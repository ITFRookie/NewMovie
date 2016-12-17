package com.example.hello.hellomovie.Beans;

/**
 * Created by Hello on 2016/12/14.
 * 保存电影的一个预告片的bean
 */

public class MoviePreviewBean {
    private String id;
    private String key;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
