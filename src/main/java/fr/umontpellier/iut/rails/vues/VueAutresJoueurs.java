package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends Pane {

    private ObjectProperty<IJoueur> joueurCourant;
    private IJoueur joueurVue;
    private Label nomJoueur;
    private ImageView img;
    private Label nbPionsW;
    private Label nbPionsB;

    private VBox vb;


    public VueAutresJoueurs(ObjectProperty<IJoueur> joueurCourant, IJoueur joueurVue) {

        this.joueurCourant = joueurCourant;
        joueurCourant.addListener(joueurCourantAChange);
        this.joueurVue = joueurVue;

        img = new ImageView("images/cartesWagons/avatar-" + joueurVue.getCouleur() + ".png");
        img.setFitHeight(83);
        img.setFitWidth(105);

        nomJoueur = new Label(joueurVue.getNom());
        nomJoueur.prefWidthProperty().bind(img.fitWidthProperty());
        nomJoueur.setAlignment(Pos.CENTER);
        nomJoueur.setPadding(new Insets(5));
        nomJoueur.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 2; -fx-border-width: 1 ;");

        vb = new VBox(img, nomJoueur);

        HBox p = new HBox(vb);

        nbPionsW = new Label();
        nbPionsW.setText(""+joueurVue.getNbPionsWagon());
        nbPionsW.prefWidthProperty().bind(widthProperty().divide(4.6));
        nbPionsW.prefHeightProperty().bind(p.heightProperty());
        nbPionsW.setAlignment(Pos.CENTER);
        nbPionsW.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;");

        nbPionsB = new Label();
        nbPionsB.setText(""+joueurVue.getNbPionsBateau());
        nbPionsB.prefWidthProperty().bind(widthProperty().divide(4.6));
        nbPionsB.prefHeightProperty().bind(p.heightProperty());
        nbPionsB.setAlignment(Pos.CENTER);
        nbPionsB.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;");

        Separator sep = new Separator(Orientation.VERTICAL);
        sep.setHalignment(Pos.CENTER.getHpos());

        p.getChildren().addAll(nbPionsW, nbPionsB);
        getChildren().add(p);
    }


    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{

        if(joueurCourant == joueurVue){
            joueurVue = ancienJoueur;

            img.setImage(new Image("images/cartesWagons/avatar-" + joueurVue.getCouleur() + ".png"));
            img.setFitHeight(83);
            img.setFitWidth(105);

            nomJoueur.setText(joueurVue.getNom());

            nbPionsW.setText(""+joueurVue.getNbPionsWagon());
            nbPionsB.setText(""+joueurVue.getNbPionsBateau());

        }

    };
}
