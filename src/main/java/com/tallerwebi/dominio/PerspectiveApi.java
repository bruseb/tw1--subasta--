package com.tallerwebi.dominio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class PerspectiveApi {
    private static final String APY_KEY = "AIzaSyDzZYLVT4JJunw6ghDo3xJFzuP-XxlJ8Dc";
    private static final String ENDPOINT ="https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + APY_KEY;

    public boolean esTextoOfensivo (String texto) throws IOException, InterruptedException{
        String json = "{"
                + "\"comment\": {\"text\": \""+ escapeJson(texto) + "\"},"
                + "\"languages\": [\"es\"],"
                + "\"requestedAttributes\": {\"TOXICITY\": {}}"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        double score = jsonResponse
                .getJSONObject("attributeScores")
                .getJSONObject("TOXICITY")
                .getJSONObject("summaryScore")
                .getDouble("value");

        return score >=0.3;
    }

    private String escapeJson (String texto){
        return texto.replace("\"","\\\"");
    }
}
