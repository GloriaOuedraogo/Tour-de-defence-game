package logiciel.ecran;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ennemi {
    private Chemin chemin;
    private int noSegment;
    private double distance;

    private double vitesse;
    private double vitesseInitiale; // Stocke la vitesse d'origine
    private int pointVie;
    private int pointVieMax;

    private Tuile image;
    private int valeurArgent;

    /**
     * Constructeur pour créer un nouvel ennemi avec des caractéristiques données.
     *
     * @param chemin        Chemin que suit l'ennemi.
     * @param vitesse       Vitesse initiale de l'ennemi.
     * @param pointVie      Points de vie de l'ennemi.
     * @param valeurArgent  Argent donné à la destruction de l'ennemi.
     * @param image         Image représentant l'ennemi.
     */
    public Ennemi(Chemin chemin, double vitesse, int pointVie, int valeurArgent, Tuile image) {
        this.chemin = chemin;
        this.vitesse = vitesse;
        this.vitesseInitiale = vitesse; // Initialise la vitesse initiale
        this.pointVie = pointVie;
        this.pointVieMax = pointVie;
        this.valeurArgent = valeurArgent;
        this.image = image;
        this.distance = 0.0;
        this.noSegment = 0;
    }

    /**
     * Constructeur de copie pour dupliquer un ennemi existant.
     *
     * @param original L'ennemi à copier.
     */
    public Ennemi(Ennemi original) {
        this.chemin = original.chemin;
        this.noSegment = original.noSegment;
        this.distance = original.distance;
        this.vitesse = original.vitesse;
        this.vitesseInitiale = original.vitesseInitiale;
        this.pointVie = original.pointVie;
        this.pointVieMax = original.pointVieMax;
        this.valeurArgent = original.valeurArgent;
        this.image = original.image;
    }

    public static int comparer(Ennemi e1, Ennemi e2) {
        return Integer.compare(e1.distanceChateau(), e2.distanceChateau());
    }

    public boolean reduireVie(int dommage) {
        pointVie -= dommage;
        return pointVie <= 0;
    }

    public boolean aAtteintChateau() {
        return chemin.nombreSegment() <= noSegment;
    }

    public void avancer() {
        if (noSegment < chemin.nombreSegment()) {
            distance += vitesse;
            int longueur = chemin.getSegment(noSegment).longueur();
            if (longueur < distance) {
                distance -= longueur;
                ++noSegment;
            }
        }
    }

    public int distanceChateau() {
        return chemin.getLongueur() - ((int) distance);
    }

    public PositionPixel getPositionPixel() {
        return chemin.calculerPosition(noSegment, distance);
    }

    public void afficher(Graphics2D g2, AffineTransform affineTransform) {
        if (noSegment < chemin.nombreSegment()) {
            AffineTransform pCurseur = (AffineTransform) affineTransform.clone();
            PositionPixel position = getPositionPixel();
            pCurseur.translate(position.x(), position.y());
            g2.drawImage(image, pCurseur, null);
            pCurseur.translate(0, -2);
            int rPV = (pointVie * Constantes.FACTEUR_PV) / pointVieMax;
            g2.drawImage(Constantes.PV_ENNEMI[rPV], pCurseur, null);
        }
    }

    public int getValeurArgent() {
        return valeurArgent;
    }

    /**
     * Applique un ralentissement temporaire à l'ennemi.
     *
     * @param reduction Fraction de réduction de la vitesse (entre 0 et 1).
     * @throws IllegalArgumentException Si la réduction n'est pas entre 0 et 1.
     */
    public void appliquerRalentissement(double reduction) {
        if (reduction < 0 || reduction > 1) {
            throw new IllegalArgumentException("La réduction doit être entre 0 et 1.");
        }
        vitesse = vitesseInitiale * (1 - reduction);
    }

    /**
     * Réinitialise la vitesse de l'ennemi à sa valeur d'origine.
     */
    public void reinitialiserVitesse() {
        vitesse = vitesseInitiale;
    }

    /**
     * Renvoie une représentation textuelle de l'ennemi pour le débogage.
     *
     * @return Chaîne décrivant les attributs principaux de l'ennemi.
     */
    @Override
    public String toString() {
        return "Ennemi{" +
                "vitesse=" + vitesse +
                ", vitesseInitiale=" + vitesseInitiale +
                ", pointVie=" + pointVie +
                ", pointVieMax=" + pointVieMax +
                ", position=" + getPositionPixel() +
                '}';
    }
}
