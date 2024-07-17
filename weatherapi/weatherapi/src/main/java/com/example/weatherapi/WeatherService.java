package com.example.weatherapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClientException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {

    private static final String API_KEY = "45AR37W7HBL99NSSDB8AGHY4S";

    private static final String WEATHER_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/{city}?unitGroup=us&include=current&key={API_KEY}&contentType=json";

    public String getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonResponse = restTemplate.getForObject(WEATHER_URL, String.class, city, API_KEY);

            ObjectMapper om = new ObjectMapper();
            JsonNode rootNode = om.readTree(jsonResponse);
            JsonNode currentConditionsNode = rootNode.path("currentConditions");

            double temp = (currentConditionsNode.path("temp").asDouble() - 32) * 5.0/9.0;
            String tempStr = String.format("%.2f", temp);

            return "City: " + city + ", Current temperature: " + tempStr + "°C";
        } catch (RestClientException | JsonProcessingException e) {
            e.printStackTrace();  // Log the stack trace
            return "Error occurred while fetching weather data: " + e.getMessage();
        }
    }
}

