package com.example.aeroport;

public class AeroportMondial {
    private String nom;
    private double latitude;
    private double longitude;
    private String country;
    private String IATA; // Code IATA

    // Constructeur
    public AeroportMondial(String nom, double latitude, double longitude, String country, String IATA) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.IATA = IATA;
    }

    // Getter pour IATA
    public String getIATA() {
        return IATA;
    }

    // Getter pour latitude
    public double getLatitude() {
        return latitude;
    }

    // Getter pour longitude
    public double getLongitude() {
        return longitude;
    }

    // Getter pour nom
    public String getNom() {
        return nom;  // Retourne le nom de l'aéroport
    }

    // Méthode pour calculer la distance
    public double calculDistance(AeroportMondial other) {
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLong = Math.toRadians(other.longitude - this.longitude);
        double avgLat = Math.toRadians((this.latitude + other.latitude) / 2);
        return Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLong * Math.cos(avgLat), 2));
    }

    // Surcharge de la méthode toString
    @Override
    public String toString() {
        return "AeroportMondial{" +
                "nom='" + nom + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", IATA='" + IATA + '\'' +
                '}';
    }
}
