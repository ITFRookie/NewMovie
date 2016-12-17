package com.example.hello.hellomovie.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hello on 2016/12/12.
 */

public class MovieGetJsonUtil {
    private MovieGetJsonUtil() {
    }

    ;

    public static String getJsonResult(String urlString) {
        StringBuilder sb = new StringBuilder("");
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String temp = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {   //返回状态码为200表示成功  开始读取json
                is = conn.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                while ((temp = br.readLine()) != null)
                    sb.append(temp);
                br.close();
                isr.close();
                is.close();

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }

    ;
}
