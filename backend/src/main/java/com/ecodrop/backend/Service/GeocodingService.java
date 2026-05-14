package com.ecodrop.backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeocodingService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Convierte una dirección en coordenadas (lat, lon) usando Nominatim API de OpenStreetMap.
     * Retorna un array [lat, lon] o null si no se puede geocodificar.
     */
    public Double[] geocodificar(String direccion) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q="
                    + java.net.URLEncoder.encode(direccion, "UTF-8")
                    + "&format=json&limit=1";

            String respuesta = restTemplate.getForObject(url, String.class);

            JsonNode nodos = objectMapper.readTree(respuesta);
            if (nodos != null && nodos.isArray() && nodos.size() > 0) {
                JsonNode primerResultado = nodos.get(0);
                Double lat = primerResultado.get("lat").asDouble();
                Double lon = primerResultado.get("lon").asDouble();
                return new Double[]{lat, lon};
            }
        } catch (Exception e) {
            System.err.println("Error geocodificando dirección '" + direccion + "': " + e.getMessage());
        }
        return null;
    }
}
