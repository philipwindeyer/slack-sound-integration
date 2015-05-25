package au.com.uwotm8.webhook;

import au.com.uwotm8.util.Config;

public class WebHookPayload {

    /**
     * The text to appear in recipients Slack client
     */
    private String text;

    /**
     * The channel/user to post to.
     * Note that although its not documented, the "channel_id" from a slack command can be used here.
     */
    private String channel;

    /**
     * The username of the person sending the message.
     */
    private String username;

    /**
     * The (optional) Emoji Avatar for the message.
     */
    private String icon_emoji = Config.getProperty(Config.Property.WEBHOOK_ICON_EMOJI);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon_emoji() {
        return icon_emoji;
    }
}
