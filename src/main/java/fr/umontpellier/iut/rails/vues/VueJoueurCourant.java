package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    private Label nomJoueur;
    private VBox cartesEnMain;

    public VueJoueurCourant(){
        nomJoueur = new Label();
        cartesEnMain = new VBox();
        getChildren().addAll(nomJoueur,cartesEnMain);

    }
    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{
        nomJoueur.setText(joueurCourant.getNom());
        getChildren().clear();
        for(ICarteTransport c : joueurCourant.getCartesTransport()){
               // String ancre = (c.estWagon() && c.getAncre()) ? "-ANCRE" : "";
                if(c.getAncre() && c.estWagon()){
                    System.out.println("ANCRE" + " " + c.getStringCouleur());
                }
                if(c.estWagon()){
                    System.out.println("WAGON" + " " +  c.getStringCouleur());
                }
                else{
                    System.out.println("BATEAU"+ " " + c.getStringCouleur());
                }
                if(c.estBateau() && c.estDouble()){
                    System.out.println("BATEAU"+ "DOUBLE"+ " " + c.getStringCouleur());
                }
                Label carte = new Label();
                cartesEnMain.getChildren().add(carte);
        }
    };
    public void creerBindings(){
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourantAChange);

    }

}
