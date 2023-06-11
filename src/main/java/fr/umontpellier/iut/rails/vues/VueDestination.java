package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends Pane {

    private final IDestination destination;
    private final Label lab;

    public VueDestination(IDestination destination) {
        this.destination = destination;
        lab = new Label(destination.getVilles().toString());
        lab.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 2; -fx-border-width: 1 ;");
        getChildren().add(lab);
    }

    public IDestination getDestination() {
        return destination;
    }

    public Label getLab() {
        return lab;
    }
}
