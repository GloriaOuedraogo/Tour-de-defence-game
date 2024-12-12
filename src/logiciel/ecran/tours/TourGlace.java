package logiciel.ecran;

import java.util.List;

/**
 * Classe représentant une tour de glace qui ralentit les ennemis proches.
 */
public class TourGlace extends Tour {
    private static final int NB_CARACTERISTIQUES = 2;
    private static final int RAYON = 0;
    private static final int REDUCTION = 1;

    // Valeurs des caractéristiques
    private static final int[] VALEURS_RAYON = {24, 32, 40};
    private static final int[] COUTS_RAYON = {40, 60};

    private static final int[] VALEURS_REDUCTION = {20, 40};
    private static final int[] COUTS_REDUCTION = {60};

    /**
     * Constructeur pour créer une tour de glace à une position donnée.
     *
     * @param position Position de la tour dans la grille.
     */
    public TourGlace(PositionTuile position) {
        super(position);

        caracteristiques = new Caracteristique[NB_CARACTERISTIQUES];

        caracteristiques[RAYON] = new Caracteristique(
            "ray", VALEURS_RAYON, COUTS_RAYON, Constantes.POSITION_CARACTERISTIQUE_Y
        );
        caracteristiques[REDUCTION] = new Caracteristique(
            "red", VALEURS_REDUCTION, COUTS_REDUCTION, Constantes.POSITION_CARACTERISTIQUE_Y + 2
        );
    }

    /**
     * Applique le ralentissement à tous les ennemis dans le rayon d'action
     * et réinitialise la vitesse des ennemis hors du rayon.
     *
     * @param ennemis Liste des ennemis présents sur le terrain.
     * @return Toujours 0 (pas d'argent gagné par cette tour).
     */
    @Override
    public int animer(List<Ennemi> ennemis) {
        double reduction = caracteristiques[REDUCTION].getValeur() / 100.0;

        for (Ennemi ennemi : ennemis) {
            if (estDansLeRayon(ennemi)) {
                try {
                    ennemi.appliquerRalentissement(reduction);
                } catch (IllegalArgumentException e) {
                    // Log ou gestion de l'erreur (si nécessaire)
                }
            } else {
                ennemi.reinitialiserVitesse();
            }
        }
        return 0; // Pas d'argent généré par cette tour
    }

    /**
     * Vérifie si un ennemi est dans le rayon d'action de la tour.
     *
     * @param ennemi Ennemi à vérifier.
     * @return Vrai si l'ennemi est dans le rayon, sinon faux.
     */
    private boolean estDansLeRayon(Ennemi ennemi) {
        int distance = position.positionPixel().distance(ennemi.getPositionPixel());
        return distance <= caracteristiques[RAYON].getValeur();
    }

    /**
     * Fournit une représentation textuelle de la tour pour le débogage.
     *
     * @return Chaîne décrivant les caractéristiques principales de la tour.
     */
    @Override
    public String toString() {
        return "TourGlace{" +
                "rayon=" + caracteristiques[RAYON].getValeur() +
                ", reduction=" + caracteristiques[REDUCTION].getValeur() +
                ", position=" + position +
                '}';
    }
}
