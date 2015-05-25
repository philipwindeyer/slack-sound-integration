package au.com.uwotm8.request;

import au.com.uwotm8.util.Config;
import au.com.uwotm8.util.UrlShortener;
import au.com.uwotm8.webhook.WebHook;
import org.apache.commons.io.FileUtils;
import spark.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RequestHandler {

    public enum Verb {GET, POST}

    public String handleRequest(Request request, Verb verb, String path) {

        SlackRequest slackRequest = RequestParser.parseRequest(request);

        if (slackRequest.getText() == null || slackRequest.getText().equals("")) {
            return String.format("No query provided. Type '%s help' for more information", slackRequest.getCommand());
        }

        if (slackRequest.getText().equals("help")) {
            return String.format("Type '%s <query>' to get a sound file. For a list of available sounds, type '%s list'.\n" +
                            "If your query matches multiple files, one will be chosen at random.\n" +
                            "You can query with spaces and dashes. E.g. 'Du Hello', 'du-Hello', and 'DU_hello' are all the effectively the same query.",
                    slackRequest.getCommand(), slackRequest.getCommand());
        }

        if (slackRequest.getText().equals("list")) {
            StringBuilder sounds = new StringBuilder("Possible sounds:\n");

            List<File> files = getFiles(path);

            for (File f : files) {
                String name = f.getName();
                name = name.substring(0, name.lastIndexOf("."));

                sounds.append(name).append("\n");
            }

            return sounds.toString();
        }

        String sound = this.getResource(slackRequest, path);

        if (sound == null) {
            return String.format("No matching sound provided. Type '%s help' for more information", slackRequest.getCommand());
        }

        String soundUrl = String.format("%s%s", request.url(), sound);

        if (Config.getProperty(Config.Property.URL_SHORTEN).equals("true")) {
            String shortUrl = null;

            try {
                shortUrl = UrlShortener.getShortenedUrl(soundUrl);
                System.out.println("Shortened URL: " + shortUrl);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }

            if ((shortUrl != null) && (!shortUrl.equals(""))) {
                soundUrl = shortUrl;
            }
        }

        StringBuilder finalText = new StringBuilder(slackRequest.getCommand()).append(" ").append(slackRequest.getText()).append("\n");
        
        if (Config.getProperty(Config.Property.PRETTIFY_LINK).equals("true")) {
            finalText.append(String.format("<%s|%s>", soundUrl, sound.substring(0, sound.lastIndexOf("."))));

        } else {
            finalText.append(String.format("<%s>", soundUrl));
        }

        if (verb == Verb.POST) {
            WebHook.pushMessage(slackRequest, finalText.toString());
            return "";
        } else {
            return finalText.toString();
        }
    }

    private String getResource(SlackRequest slackRequest, String path) {

        String query = new String(slackRequest.getText());
        query = query.toLowerCase();
        query = query.replaceAll(" ", "-");
        query = query.replaceAll("_", "-");

        List<String> possibleSounds = new ArrayList<>();

        for (File f : this.getFiles(path)) {

            if (f.getName().contains(query)) {
                possibleSounds.add(f.getName());
            }

        }

        String result = null;

        if (possibleSounds.size() > 0) {
            System.out.println(String.format("Sourced %s sounds in total. Selecting 1 at random", possibleSounds.size()));
            result = possibleSounds.get(new Random().nextInt(possibleSounds.size()));
        }

        if (result != null) {
            return result;

        } else {
            return null;
        }
    }

    private List<File> getFiles(String path) {
        String[] extensions = Config.getProperty(Config.Property.FILE_EXTENSIONS).split(",");
        File file = new File(path);

        List<File> files = new ArrayList<>(FileUtils.listFiles(file, extensions, false));
        Collections.sort(files);

        return files;
    }
}
