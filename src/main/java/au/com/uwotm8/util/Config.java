package au.com.uwotm8.util;

import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String FILE = "/config.properties";
    private static Properties properties;

    private Config() {
    }

    public enum Property {
        FILE_EXTENSIONS("file.extensions"),
        URL_SHORTEN("url.shorten"),
        PRETTIFY_LINK("prettify.link"),
        WEBHOOK_ICON_EMOJI("webhook.icon.emoji"),
        SLACK_HOOK_URL("slack.hook.url");

        private String property;

        Property(String property) {
            this.property = property;
        }

        public String getProperty() {
            return this.property;
        }
    }

    private static Properties getProperties() {

        if (properties == null) {

            try {

                System.out.println(String.format("Loading properties from %s", FILE));

                properties = new Properties();
                properties.load(Config.class.getResourceAsStream(FILE));

            } catch (IOException e) {
                e.printStackTrace(System.err);
                System.err.println(String.format("Cannot load %s file. Server cannot run without it. Shutting down.", FILE));
                System.exit(1);
            }
        }

        return properties;
    }

    public synchronized static String getProperty(Property key) {
        return getProperties().getProperty(key.getProperty());
    }
}
