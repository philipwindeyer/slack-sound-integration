package au.com.uwotm8.webhook;

import au.com.uwotm8.request.SlackRequest;
import au.com.uwotm8.util.Config;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebHook {

    private static final String URL;

    static {
        URL = Config.getProperty(Config.Property.SLACK_HOOK_URL);
    }

    public synchronized static void pushMessage(SlackRequest slackRequest, String resource) {

        WebHookPayload payload = new WebHookPayload();

        payload.setText(resource);
        payload.setUsername(slackRequest.getUserName());
        payload.setChannel(slackRequest.getChannelId());

        String json = new Gson().toJson(payload);

        StringBuffer jsonString = new StringBuffer();

        try {
            System.out.println(String.format("Sending payload to %s. Payload: %s", URL, json));

            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(json);
            writer.close();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            
            br.close();
            connection.disconnect();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println("Response from Slack: " + jsonString.toString());
    }
}
