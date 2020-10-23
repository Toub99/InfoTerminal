/**
 * 
 */
package TerminalWindow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tobias Kaiser
 *
 */
public class HttpClientClass {
    
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2)
            .build();
    
    
    public LinkedList<DepartureModel> sendGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://marudor.de/api/iris/v1/abfahrten/8005990"))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // print status code
        System.out.println(response.statusCode());
        //System.out.print(response.body());

        // return response body
        ObjectMapper mapper = new ObjectMapper();
        JsonNode topLevelNode = mapper.readTree(response.body());
        JsonNode departures = topLevelNode.get("departures");
        LinkedList<DepartureModel> model = new LinkedList<DepartureModel>();
        int counter = response.body().split("\"o\"").length - 1;
        for (int i = 0; i < counter; i++) {
            JsonNode departure = departures.get(i);
            if (departure.get("o") != null) {
                JsonNode destination = departure.get("scheduledDestination");
                JsonNode delay = departure.get("departure").get("delay");
                JsonNode train = departure.get("train").get("name");
                JsonNode time = departure.get("departure").get("time");
                if (destination != null && delay != null && train != null && time != null) {
                    model.add(createDeparture(time.asLong(), destination.asText(), delay.asText(), train.asText()));
                } else if (delay == null) {
                    model.add(createDeparture(time.asLong(), destination.asText(), "0", train.asText()));
                } 
            }
        }
        return model;

    }
    
    private DepartureModel createDeparture(final long departureTimestamp, final String destination, final String delay, final String train) {
        ZoneId timeZone = Clock.systemDefaultZone().getZone();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN).withZone(timeZone);
        Instant instant = Instant.ofEpochMilli(departureTimestamp);
        String departure = formatter.format(instant);
        DepartureModel model = new DepartureModel(departure, destination, delay, train);
        return model;
    }
}
