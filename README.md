# slack-sound-integration

This is a micro-server that acts as a "Slack Integration".
Specifically it;
- Intercepts a Slack "slash command" (configured in Slack) where the given query is the sound to search for
- Finds a corresponding sound hosted on the server this software runs on (if there are multiple, one is picked at random), and generates a web link to it
- Pushes a message to a user/channel containing that link (which can be formatted as per the config file), via a Slack Webhook (configured in Slack)

Started off as a bit of a joke, but I figured I'd make it more generic and useful for other people to use and contribute to.

It is built in Java, using the Spark Micro-web framework (http://sparkjava.com/), and Maven (http://maven.apache.org/).

## Dependencies:
- Java 8+ (Spark 2 runs on Java 8+ only)
- Maven 2+ (uses for distribution builds)
- bash (use cygwin if on Windows...or buy a real computer)
- zip (comes on most *nix machines by default)

## To build:
Run "build" (e.g. ./build)

This will produce a zip file containing compiled fat-jar, and "server" script.

## To run:
With "server" alongside "sound-server.jar", run "server" (e.g. ./server start --resource-dir=/sounds)

## Notes:
Slack does not (yet) play sounds natively. Clicking a link produced by this will point your browser to the sound file.
And if you're using Chrome, it'll play automatically.
There are a good few tweets regarding inbuilt sounds, and several feature requests, so hopefully we'll get this soon.

To configure Slack to use something like this, see "https://api.slack.com/slash-commands" and "https://api.slack.com/incoming-webhooks".
If you'd like message content to be sent to a recipient/channel, make sure your slash command is set to POST. GET behaviour is to only return content to you, rather than posting to a recipient.


## Wishlist:
- Proper logging, to /var/log
- Installation script (will make "server" a service)
- Possibly S3 integration

Any thoughts?
