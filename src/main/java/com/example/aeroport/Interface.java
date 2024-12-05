package com.example.aeroport;

import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Interface extends Application {

    private double initialMouseY; // Position initiale de la souris pour calculer le delta
    private final Translate cameraTranslate = new Translate();

    @Override
    public void start(Stage primaryStage) {
        // Créer un objet Earth
        Earth3D earth = new Earth3D();

        // Ajouter une caméra
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000); // Position initiale de la caméra
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);

        // Ajouter une transformation pour déplacer la caméra
        camera.getTransforms().add(cameraTranslate);

        // Créer une scène en utilisant la racine de Earth3D
        Scene scene = new Scene(earth.getRoot(), 800, 600, true);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);

        // Ajouter les événements pour détecter les interactions utilisateur
        scene.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                // Stocke la position Y de la souris lorsqu'elle est pressée
                initialMouseY = event.getSceneY();
            }

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                // Calcul du déplacement vertical de la souris
                double deltaY = event.getSceneY() - initialMouseY;

                // Appliquer une transformation à la caméra en fonction du déplacement
                cameraTranslate.setZ(cameraTranslate.getZ() + deltaY * 0.1);

                // Mettre à jour initialMouseY pour permettre un zoom continu
                initialMouseY = event.getSceneY();
            }
        });

        // Configurer la fenêtre principale
        primaryStage.setTitle("Terre 3D avec Zoom");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
