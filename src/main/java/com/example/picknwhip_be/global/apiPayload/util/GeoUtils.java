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

  /**
   * 좌표 범위 보정: - lat는 [-90, 90] 범위로 clamp - lon은 [-180, 180] 범위로 clamp (극단 케이스 방어) - min/max 역전 방지
   */
  public static BoundingBox normalize(BoundingBox box) {
    double minLat = clamp(box.minLat(), -90.0, 90.0);
    double maxLat = clamp(box.maxLat(), -90.0, 90.0);

    double minLon = clamp(box.minLon(), -180.0, 180.0);
    double maxLon = clamp(box.maxLon(), -180.0, 180.0);

    if (minLat > maxLat) {
      double tmp = minLat;
      minLat = maxLat;
      maxLat = tmp;
    }
    if (minLon > maxLon) {
      double tmp = minLon;
      minLon = maxLon;
      maxLon = tmp;
    }

    return new BoundingBox(minLat, maxLat, minLon, maxLon);
  }

  private static double clamp(double v, double min, double max) {
    return Math.max(min, Math.min(max, v));
  }

  public record BoundingBox(double minLat, double maxLat, double minLon, double maxLon) {}
}
