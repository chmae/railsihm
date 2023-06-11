package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private HBox p;


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
        nomJoueur.setPadding(new Insets(10));
        nomJoueur.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 2; -fx-border-width: 1 ;");

        vb = new VBox(img, nomJoueur);

        p = new HBox(vb);

        nbPionsW = new Label();
        nbPionsW.setText("Wagons: "+joueurVue.getNbPionsWagon());
        nbPionsW.prefWidthProperty().bind(widthProperty().divide(4.6));
        nbPionsW.prefHeightProperty().bind(p.heightProperty());
        nbPionsW.setAlignment(Pos.CENTER);
        nbPionsW.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;");

        nbPionsB = new Label();
        nbPionsB.setText("Bateaux: "+joueurVue.getNbPionsBateau());
        nbPionsB.prefWidthProperty().bind(widthProperty().divide(4.6));
        nbPionsB.prefHeightProperty().bind(p.heightProperty());
        nbPionsB.setAlignment(Pos.CENTER);
        nbPionsB.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;");

        nbPionsW.widthProperty().addListener(changeWidth);

        Separator sep = new Separator(Orientation.VERTICAL);
        sep.setHalignment(Pos.CENTER.getHpos());

//        ImageView imgPionsBateau = new ImageView("images/bouton-pions-bateau.png");
//        imgPionsBateau.setFitWidth(49.0/1.5);
//        imgPionsBateau.setFitHeight(48.5/1.5);
//
//        ImageView imgPionsWagon = new ImageView("images/bouton-pions-wagon.png");
//        imgPionsWagon.setFitWidth(49.0/1.5);
//        imgPionsWagon.setFitHeight(48.5/1.5);


        p.getChildren().addAll(nbPionsW,nbPionsB);
        p.setSpacing(10);
        getChildren().add(p);

    }

    ChangeListener<Number> changeWidth = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            if (t1.intValue() < 65) {
                nbPionsW.setText("" + joueurVue.getNbPionsWagon());
                nbPionsB.setText("" + joueurVue.getNbPionsBateau());
            } else {
                nbPionsW.setText("Wagons: " + joueurVue.getNbPionsWagon());
                nbPionsB.setText("Bateaux: " + joueurVue.getNbPionsBateau());
            }
        }
    };

    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{

        if(joueurCourant == joueurVue){
            joueurVue = ancienJoueur;

            img.setImage(new Image("images/cartesWagons/avatar-" + joueurVue.getCouleur() + ".png"));
            img.setFitHeight(83);
            img.setFitWidth(105);

            nomJoueur.setText(joueurVue.getNom());

            if(nbPionsW.getWidth() < 65){
                nbPionsW.setText("" + joueurVue.getNbPionsWagon());
                nbPionsB.setText("" + joueurVue.getNbPionsBateau());
            }else {
                nbPionsW.setText("Wagons: " + joueurVue.getNbPionsWagon());
                nbPionsB.setText("Bateaux: " + joueurVue.getNbPionsBateau());
            }

        }

    };
}
