package au.com.uwotm8.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlShortener {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String URL_SHORTENER = "http://tiny-url.info/api/v1/random?url=";

    public synchronized static String getShortenedUrl(String longUrl) throws Exception {

        String requestUrl = URL_SHORTENER + longUrl;

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        String line;
        StringBuffer response = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        br.close();
        connection.disconnect();

        String shortenedUrl = response.toString();

        return shortenedUrl;
    }
}
