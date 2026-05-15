package com.ecodrop.backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoutingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RoutingService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Calcula la distancia en kilómetros entre dos coordenadas usando OSRM (OpenStreetMap).
     * Retorna la distancia en km o null si no se puede calcular.
     */
    @SuppressWarnings("null")
    public Double calcularDistancia(Double latOrigen, Double lonOrigen, Double latDestino, Double lonDestino) {
        try {
            String url = String.format(
                "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=false",
                lonOrigen, latOrigen, lonDestino, latDestino
            );

            String respuesta = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(respuesta);

            if (root != null && root.has("routes") && root.get("routes").isArray() && root.get("routes").size() > 0) {
                double distanciaMetros = root.get("routes").get(0).get("distance").asDouble();
                return distanciaMetros / 1000.0;
            }
        } catch (Exception e) {
            System.err.println("Error calculando ruta: " + e.getMessage());
        }
        return null;
    }
}
