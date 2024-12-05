package com.example.aeroport;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;  // Importer Point2D pour stocker la latitude et longitude

import javafx.geometry.Point2D;  // Importer Point2D pour stocker la latitude et longitude

public class Earth3D {
    private final Group root;
    private final Sphere earth;
    private final Rotate rotationY;
    private Timeline rotationTimeline;
    private final List<Sphere> redSpheres = new ArrayList<>();

    public Earth3D(Group root, Sphere earth, Rotate rotationY) {
        this.root = root;
        this.earth = earth;
        this.rotationY = rotationY;
    }

    public Earth3D() {
        // Création de la sphère de la Terre
        earth = new Sphere(300);

        // Application de la texture de la Terre
        Image earthTexture = new Image("file:/C:/Users/rayan/Downloads/earth_lights_4800.png");
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(earthTexture);
        earth.setMaterial(material);

        // Rotation de la Terre
        rotationY = new Rotate(0, Rotate.Y_AXIS);
        earth.getTransforms().add(rotationY);

        // Conteneur racine
        root = new Group(earth);

        // Lancer l'animation de rotation
        startRotation();

        // Gestion des clics sur la Terre
        earth.setOnMouseClicked(this::handleClick);
    }

    private void startRotation() {
        // Créer une Timeline qui va animer la rotation de la Terre
        rotationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.017), e -> rotateEarth())
        );
        rotationTimeline.setCycleCount(Timeline.INDEFINITE); // Rotation continue
        rotationTimeline.play(); // Démarrer l'animation
    }

    private void rotateEarth() {
        // Rotation de la Terre autour de l'axe Y
        rotationY.setAngle(rotationY.getAngle() + 0.2);  // Ajuste la vitesse de rotation

        // Recalculer la position des sphères rouges pour qu'elles suivent la rotation de la Terre
        for (Sphere redSphere : redSpheres) {
            // Récupérer les coordonnées stockées dans UserData (latitude et longitude)
            Point2D coordinates = (Point2D) redSphere.getUserData();
            if (coordinates != null) {
                double latitude = coordinates.getX();  // Récupérer la latitude
                double longitude = coordinates.getY(); // Récupérer la longitude

                // Appliquer la rotation de la Terre à la sphère rouge
                // Inverser le sens de la rotation pour que les sphères tournent dans le même sens que la Terre
                double adjustedLongitude = longitude - rotationX.getAngle(); // Inverser la rotation en soustrayant l'angle

                // Recalculez la position 3D de la sphère rouge en tenant compte de la rotation
                double x = 300 * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(adjustedLongitude));
                double y = 300 * Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(adjustedLongitude));
                double z = 300 * Math.sin(Math.toRadians(latitude));

                // Positionner la sphère rouge dans la scène 3D
                redSphere.setTranslateX(x);
                redSphere.setTranslateY(y);
                redSphere.setTranslateZ(z);
            }
        }
    }

    private void handleClick(MouseEvent event) {
        // Obtention des coordonnées du clic
        double clickX = event.getSceneX();
        double clickY = event.getSceneY();

        // Affichage de la position du clic
        System.out.println("Clic détecté à (" + clickX + ", " + clickY + ")");

        // Calcul de la position 3D sur la sphère à partir du clic
        double latitude = calculateLatitude(clickY);
        double longitude = calculateLongitude(clickX);

        // Affichage de la sphère rouge à la position calculée
        displayRedSphere(latitude, longitude);
    }

    private double calculateLatitude(double sceneY) {
        // La latitude va de -90° à +90° (haut-bas sur la sphère)
        double centerY = 300;  // position centrale de la sphère (ajustez selon votre scène)
        double radius = 300;   // rayon de la sphère (ajustez selon la taille réelle de la sphère)

        // Calcul de la latitude en fonction du clic
        double normalizedY = (sceneY - centerY) / radius; // normalisation de la position Y
        return normalizedY * 180;  // Conversion de la position Y en latitude allant de -90° à +90°
    }

    private double calculateLongitude(double sceneX) {
        // La longitude va de -180° à +180° (gauche-droite sur la sphère)
        double centerX = 400;  // position centrale de la sphère (ajustez selon votre scène)
        double radius = 400;   // rayon de la sphère (ajustez selon la taille réelle de la sphère)

        // Calcul de la longitude en fonction du clic
        double normalizedX = (sceneX - centerX) / radius; // normalisation de la position X
        return normalizedX * 360;  // Conversion de la position X en longitude allant de -180° à +180°
    }

    public void displayRedSphere(double latitude, double longitude) {
        // Création d'une sphère rouge pour marquer l'emplacement sur la Terre 3D
        Sphere redSphere = new Sphere(10);  // Sphère rouge pour marquer l'emplacement
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(javafx.scene.paint.Color.RED);
        redSphere.setMaterial(redMaterial);

        // Conversion des coordonnées géographiques (latitude, longitude) en coordonnées 3D
        double x = 300 * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitude));
        double y = 300 * Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude));
        double z = 300 * Math.sin(Math.toRadians(latitude));

        // Positionner la sphère rouge dans la scène 3D
        redSphere.setTranslateX(x);
        redSphere.setTranslateY(y);
        redSphere.setTranslateZ(z);

        // Stocker latitude et longitude dans un objet Point2D
        redSphere.setUserData(new Point2D(latitude, longitude));

        // Ajouter la sphère rouge à la scène
        root.getChildren().add(redSphere);  // Ajouter la sphère à la scène 3D
        redSpheres.add(redSphere); // Ajouter la sphère à la liste pour suivre la rotation
    }

    public Group getRoot() {
        return root;
    }

    public Sphere getSphere() {
        return earth;
    }
}
