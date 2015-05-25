package au.com.uwotm8.request;

import spark.Request;

public class RequestParser {

    private RequestParser() {
    }

    public synchronized static SlackRequest parseRequest(Request request) {
        
        SlackRequest slackRequest = new SlackRequest();

        slackRequest.setToken(request.queryParams("token"));
        slackRequest.setTeamId(request.queryParams("team_id"));
        slackRequest.setTeamDomain(request.queryParams("team_domain"));
        slackRequest.setChannelId(request.queryParams("channel_id"));
        slackRequest.setChannelName(request.queryParams("channel_name"));
        slackRequest.setUserId(request.queryParams("user_id"));
        slackRequest.setUserName(request.queryParams("user_name"));
        slackRequest.setCommand(request.queryParams("command"));
        slackRequest.setText(request.queryParams("text"));

        return slackRequest;
    }
}
