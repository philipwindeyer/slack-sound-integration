package au.com.uwotm8;

import au.com.uwotm8.request.RequestHandler;

import java.net.URL;

import static spark.Spark.*;

public class Server {

    private static final String PATH_ARG = "--resource-dir";
    private static final String DEFAULT_RESOURCE_DIR = "public";

    public static void main(String[] args) {

        String path = "";
        URL defaultResourceDir = Server.class.getClassLoader().getResource(DEFAULT_RESOURCE_DIR);

        if (defaultResourceDir != null) {
            path = defaultResourceDir.getPath();
        }

        if (path.contains("target/classes")) {
            System.out.println("Running via IDE. Using resources in 'src/main/resources/public'");
            staticFileLocation(DEFAULT_RESOURCE_DIR);

        } else {

            path = null;

            for (String s : args) {
                if (s.startsWith(PATH_ARG)) {
                    String[] arg = s.split("=");
                    path = arg[1];
                    break;
                }
            }

            if (path == null) {
                System.err.println(String.format("Specify the directory containing resources to serve, in the format %s=<full-path>", PATH_ARG));
                System.exit(1);
            }

            externalStaticFileLocation(path);

            port(80); // Runs on port 80 when run as a standalone jar. Requires root privileges.
        }

        final String PATH = path;

        get("/", (request, response) -> new RequestHandler().handleRequest(request, RequestHandler.Verb.GET, PATH));
        post("/", ((request, response) -> new RequestHandler().handleRequest(request, RequestHandler.Verb.POST, PATH)));
    }
}
