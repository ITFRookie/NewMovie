package com.example.hello.hellomovie.Utils;

import com.example.hello.hellomovie.Beans.MovieCommentBean;
import com.example.hello.hellomovie.Beans.MovieInfoBean;
import com.example.hello.hellomovie.Beans.MoviePreviewBean;
import com.example.hello.hellomovie.Beans.MovieTimeBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Hello on 2016/12/12
 * 其实三个方法有很多相似的地方  可以合并成一个  但是因为是最后添加的 所以为了避免出现bug
 * 没合并.
 */

public class MovieJsonAndBeanTran {
    private MovieJsonAndBeanTran() {
    }

    ;

    public static ArrayList parseJsonFromNet(String jsonData) {
        ArrayList<MovieInfoBean> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(jsonData);
        JsonObject mainObject = null;
        if (ele.isJsonObject())
            mainObject = ele.getAsJsonObject();
        if (mainObject != null) {
            JsonArray result = mainObject.getAsJsonArray("results");
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                JsonElement element = (JsonElement) iterator.next();
                if (element.isJsonObject()) {
                    MovieInfoBean bean = new MovieInfoBean();
                    bean = gson.fromJson(element, MovieInfoBean.class);
                    list.add(bean);


                }

            }


        }

        return list;
    }

    public static ArrayList parseJsonToPreview(String jsonData) {
        ArrayList<MoviePreviewBean> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(jsonData);
        JsonObject mainObject = null;
        if (ele.isJsonObject())
            mainObject = ele.getAsJsonObject();
        if (mainObject != null) {
            JsonArray result = mainObject.getAsJsonArray("results");
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                JsonElement element = (JsonElement) iterator.next();
                if (element.isJsonObject()) {
                    MoviePreviewBean bean = new MoviePreviewBean();
                    bean = gson.fromJson(element, MoviePreviewBean.class);
                    list.add(bean);


                }

            }


        }

        return list;
    }

    public static ArrayList parseJsonToComment(String jsonData) {
        ArrayList<MovieCommentBean> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(jsonData);
        JsonObject mainObject = null;
        if (ele.isJsonObject())
            mainObject = ele.getAsJsonObject();
        if (mainObject != null) {
            JsonArray result = mainObject.getAsJsonArray("results");
            Iterator iterator = result.iterator();
            while (iterator.hasNext()) {
                JsonElement element = (JsonElement) iterator.next();
                if (element.isJsonObject()) {
                    MovieCommentBean bean = new MovieCommentBean();
                    bean = gson.fromJson(element, MovieCommentBean.class);
                    list.add(bean);


                }

            }


        }

        return list;
    }

    public static ArrayList parseJsonToLength(String jsonData) {
        ArrayList<MovieTimeBean> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement ele = parser.parse(jsonData);
        JsonObject mainObject = null;
        if (ele.isJsonObject())
            mainObject = ele.getAsJsonObject();
        if (mainObject != null) {
            MovieTimeBean timeBean = new MovieTimeBean();
            timeBean = gson.fromJson(mainObject, MovieTimeBean.class);

            list.add(timeBean);
        }

        return list;
    }
}
