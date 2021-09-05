package org.launchcode.ouchy.controllers;



import org.json.JSONArray;
import org.json.JSONObject;
import org.launchcode.ouchy.models.Data.ProviderRepository;
import org.launchcode.ouchy.models.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class DistanceSearchController {

    private static final String URL_START = "https://maps.distancematrixapi.com/maps/api/distancematrix/json?origins=";
    private static final String URL_MIDDLE = "&destinations=";
    private static final String URL_END = "&departure_time=now&key=";
    private static final String API_KEY = "AlphaDMAbbQwYhDqcKFcg3nU8znJy3kkBwPDWNiN";

    public String convertAddressToURL(String addy) {
        String street = addy.substring(0,addy.indexOf(","));
        street = street.replace(" ","%20");
        addy = addy.substring(addy.indexOf(",")+1);
        while (addy.charAt(0) == ' ') {
            addy = addy.substring(1);
        }
        String city = addy.substring(0,addy.indexOf(","));
        city = city.replace(" ","%20");
        addy = addy.substring(addy.indexOf(",")+1);
        while (addy.charAt(0) == ' ') {
            addy = addy.substring(1);
        }
        addy = addy.replace(" ","%2C");
        String addyURI = street + "%2C" + city + "%2C" + addy;

        return  addyURI;
    }

    @Autowired
    ProviderRepository providerRepository;

    @GetMapping("Distance")
    public String calcDistanceHome(Model model) {
        model.addAttribute("title", "Distance API Test");

        return "distance_get";
    }

    @PostMapping("Distance")
    public String processCalcDistance(Model model, @RequestParam String clientAddress) throws IOException, InterruptedException {
        model.addAttribute("title", "Distance API Test");

        String origin = convertAddressToURL(clientAddress);
        String destination = "";

        List<HashMap> destinationData = new ArrayList<HashMap>();

        Iterable<Provider> providers = providerRepository.findAll();


        for(Provider provider : providers) {
            /** Converting provider address to format required by API */
            destination = convertAddressToURL(provider.getProviderAddress());

            /** Creating URL to access the API */
            String url = URL_START + origin + URL_MIDDLE + destination + URL_END + API_KEY;

            /** Starting the HashMap data created during each loop.  Will contain "Provider", "Distance" and "Time" */
            HashMap data = new HashMap();
            data.put("Name", provider.getProviderName());
            data.put("Address", provider.getProviderAddress());

            /** Sending the request to DistanceMatrixAPI to get JSON data */

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject object = new JSONObject(response.body().toString());
            Object rows = object.get("rows");
            JSONArray elementArray = new JSONArray(rows.toString());
            JSONObject elements = new JSONObject(elementArray.getJSONObject(0).toString());
            JSONArray detailsArray = new JSONArray(elements.getJSONArray("elements"));
            JSONObject detailsObject = new JSONObject(detailsArray.getJSONObject(0).toString());
            JSONObject durationText = detailsObject.getJSONObject("duration");

            /**  This is the time text we want! */
            System.out.println("Time:  " + durationText.get("text"));

            JSONObject distanceText = detailsObject.getJSONObject("distance");

            /**  This is the distance value we want!  */
            System.out.println("Distance  " + distanceText.get("value"));

            /**  Distance data is returned in meters, converting it to miles */
            Double milesToDest = distanceText.getDouble("value")/1609.34;

            data.put("Time", durationText.get("text"));
            data.put("Distance", milesToDest);

            destinationData.add(data);
        }

        model.addAttribute("destinationData",destinationData);

        return "distance_post";
    }
}
