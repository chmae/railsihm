package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private IJoueur joueurCourant;
    private Label nomJoueur;
    private VBox cartesEnMain;

    private HBox cartesJoueurC;

    private ImageView img;

    private String color;

    public VueJoueurCourant(IJeu jeu){
        joueurCourant = jeu.joueurCourantProperty().get();
        nomJoueur = new Label();
        cartesEnMain = new VBox();
        cartesJoueurC = new HBox();
        img = new ImageView();
        color = "";
        getChildren().addAll(nomJoueur,cartesEnMain);

    }
    /*A REVOIR CETTE ECOUTEUR */
    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{
        nomJoueur.setText(joueurCourant.getNom());
        getChildren().clear();
        for(ICarteTransport c : joueurCourant.getCartesTransport()){
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
                if(c.estJoker()){
                    System.out.println("JOKER" + " " + c.getStringCouleur());
                }
                Label carte = new Label();
                cartesEnMain.getChildren().add(carte);

            img = new ImageView("images/cartesWagons/avatar-" + joueurCourant.getCouleur()+ ".png");
            img.setFitHeight(83);
            img.setFitWidth(105);
            color = joueurCourant.getCouleur().name();
        }
    };

    public void afficherCartes(){
        cartesJoueurC.getChildren().clear();
        for(int i = 0; i < ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().getValue().getCartesTransport().size(); i++) {
            VueCarteTransport c = new VueCarteTransport(((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().getValue().getCartesTransport().get(i),6);
            cartesJoueurC.getChildren().add(c);
        }
    }

    public void creerBindings(){
        //((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(joueurCourantAChange);

    }


    public String getColor() {
        return color;
    }

    public ImageView getImg() {
        return img;
    }

    public Label getNom() {
        return nomJoueur;
    }
}
