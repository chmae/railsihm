package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends GridPane {

    private ObjectProperty<IJoueur> joueurCourant;
    private Label nomJoueur;
    private GridPane cartesEnMain;
    private GridPane carteDestinationEnMain;
    private ImageView img;


    public VueJoueurCourant(IJeu jeu){
        joueurCourant = jeu.joueurCourantProperty();
        joueurCourant.addListener(joueurCourantAChange);

        nomJoueur = new Label();
        cartesEnMain = new GridPane();

        img = new ImageView();

        VBox v = new VBox(img, nomJoueur);
        add(v, 0, 0);
        add(cartesEnMain, 1, 0);


    }

    ListChangeListener<ICarteTransport> cartesTransportsChange = new ListChangeListener<ICarteTransport>() {
        @Override
        public void onChanged(Change<? extends ICarteTransport> change) {
            while(change.next()){
                for(ICarteTransport c: change.getAddedSubList()){
                    int i;
                    int j;

                    i = (joueurCourant.get().getCartesTransport().size()-1)/5;
                    j = (joueurCourant.get().getCartesTransport().size()-1)%5;
                    System.out.println(j);

                    VueCarteTransport v = new VueCarteTransport(c);
                    cartesEnMain.add(v,j,i);
                }
            }
        }
    };

    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{
        nomJoueur.setText(joueurCourant.getNom());

        img.setImage(new Image("images/cartesWagons/avatar-" + joueurCourant.getCouleur().name()+ ".png"));
        img.setFitHeight(83);
        img.setFitWidth(105);

        cartesEnMain.getChildren().clear();
        int j=0;
        int i = 0;
        for(ICarteTransport c : joueurCourant.getCartesTransport()){
            VueCarteTransport v = new VueCarteTransport(c);
            cartesEnMain.add(v,j,i);
            j++;
            if(j == joueurCourant.getCartesTransport().size()/2 || j == 5){
                j=0;
                i++;
            }
        }

        if(joueurCourant.getCartesTransport().size()!=0) {
            joueurCourant.cartesTransportProperty().addListener(cartesTransportsChange);
        }

    };

    private void reInitCarteTransports(IJoueur joueurCourant){

    }

    public GridPane getCartesEnMain(){return cartesEnMain;}


}
