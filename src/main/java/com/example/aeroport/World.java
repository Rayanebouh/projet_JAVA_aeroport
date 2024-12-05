package com.example.aeroport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<AeroportMondial> aeroportMondialList = new ArrayList<>();

    public World(String fileName) {
        try (BufferedReader buf = new BufferedReader(new FileReader(fileName))) {
            String s = buf.readLine();

            while (s != null) {
                s = s.replaceAll("\"", ""); // Enlève les guillemets
                String fields[] = s.split(",");

                // Vérifie si le fichier contient suffisamment de champs pour éviter les erreurs d'index
                if (fields.length >= 6 && fields[1].equals("large_airport")) {
                    String nom = fields[2];
                    double latitude = Double.parseDouble(fields[12]);
                    double longitude = Double.parseDouble(fields[11]);
                    String country = fields[8];
                    String IATA = fields[9];

                    // Ajoute un nouvel objet AeroportMondial à la liste
                    AeroportMondial aeroport = new AeroportMondial(nom, latitude, longitude, country, IATA);
                    aeroportMondialList.add(aeroport);
                }

                s = buf.readLine(); // Lit la prochaine ligne
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier. Vérifiez le chemin et le fichier.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format des données GPS dans le fichier.");
            e.printStackTrace();
        }
    }

    public int getNumberOfAirports() {
        return this.aeroportMondialList.size();
    }

    public static void main(String[] args) {
        // Charger les aéroports depuis le fichier CSV
        World w = new World("C:\\Users\\rayan\\IdeaProjects\\Aeroport\\src\\main\\resources\\airport-codes_no_comma (2).csv");
        System.out.println("Found " + w.getNumberOfAirports() + " airports.");

        // Trouver l'aéroport le plus proche
        AeroportMondial paris = w.findNearest(48.866, 2.316);
        System.out.println("Aéroport le plus proche de (48.866, 2.316) : " + paris.getNom());

        // Trouver un aéroport par code IATA (par exemple CDG)
        AeroportMondial cdg = w.findByCode("CDG");
        if (cdg != null) {
            System.out.println("Aéroport trouvé par code IATA (CDG) : " + cdg.getNom());
        } else {
            System.out.println("Aéroport avec le code IATA 'CDG' non trouvé.");
        }

        // Calcul de la distance entre l'aéroport le plus proche et une autre position
        double distance = w.distance(48.866, 2.316, paris.getLatitude(), paris.getLongitude());
        System.out.println("Distance entre (48.866, 2.316) et l'aéroport " + paris.getNom() + " : " + distance);

        // Calcul de la distance entre une autre position et CDG
        double distanceCDG = w.distance(48.866, 2.316, cdg.getLatitude(), cdg.getLongitude());
        System.out.println("Distance entre (48.866, 2.316) et l'aéroport " + cdg.getNom() + " : " + distanceCDG);
    }

    public AeroportMondial findByCode(String code) {
        for (AeroportMondial aeroport : aeroportMondialList) {
            if (aeroport.getIATA().equalsIgnoreCase(code)) {
                return aeroport;
            }
        }
        return null; // Retourne null si aucun aéroport n'est trouvé avec le code IATA
    }

    public AeroportMondial findNearest(double latitude, double longitude) {
        AeroportMondial reference = new AeroportMondial("", latitude, longitude, "", "");
        AeroportMondial nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (AeroportMondial aeroport : aeroportMondialList) {
            double distance = reference.calculDistance(aeroport);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = aeroport;
            }
        }
        return nearest;
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double deltaLat = lat2 - lat1;
        double deltaLong = long2 - long1;
        double latAverage = (lat1 + lat2) / 2;
        return deltaLat * deltaLat + (deltaLong * Math.cos(Math.toRadians(latAverage))) * (deltaLong * Math.cos(Math.toRadians(latAverage)));
    }
}
