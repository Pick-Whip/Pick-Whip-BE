package com.example.picknwhip_be.global.apiPayload.util;

public final class GeoUtils {

    private static final double EARTH_RADIUS_M = 6371000.0;

    private GeoUtils() {}

    public static BoundingBox boundingBox(double lat, double lon, double radiusM) {
        double latRadius = Math.toDegrees(radiusM / EARTH_RADIUS_M);
        double lonRadius = Math.toDegrees(radiusM / (EARTH_RADIUS_M * Math.cos(Math.toRadians(lat))));

        double minLat = lat - latRadius;
        double maxLat = lat + latRadius;
        double minLon = lon - lonRadius;
        double maxLon = lon + lonRadius;

        return new BoundingBox(minLat, maxLat, minLon, maxLon);
    }

    public record BoundingBox(double minLat, double maxLat, double minLon, double maxLon) {}
}