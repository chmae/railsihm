package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.CarteTransport;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends Pane {

    private ObjectProperty<IJoueur> joueurCourant;
    private Label nomJoueur;
    private HBox cartesEnMain;
    private HBox carteDestinationEnMain;
    private ImageView img;

    private Label labWagon;
    private Label labBateau;


    public VueJoueurCourant(IJeu jeu){

        joueurCourant = jeu.joueurCourantProperty();
        joueurCourant.addListener(joueurCourantAChange);

        nomJoueur = new Label();
        nomJoueur.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 2; -fx-border-width: 1 ;");
        cartesEnMain = new HBox();

        img = new ImageView();

        nomJoueur.prefWidthProperty().bind(img.fitWidthProperty());
        nomJoueur.setAlignment(Pos.CENTER);
        nomJoueur.setPadding(new Insets(5));

        VBox v1 = new VBox(img, nomJoueur);
        v1.setSpacing(5);
        HBox h1 = new HBox(v1, cartesEnMain);

        ImageView imgPionsBateau = new ImageView("images/bouton-pions-bateau.png");
        imgPionsBateau.setFitWidth(49);
        imgPionsBateau.setFitHeight(48.5);
        labBateau = new Label();
        labBateau.prefHeightProperty().bind(imgPionsBateau.fitHeightProperty());
        labBateau.prefWidthProperty().bind(imgPionsBateau.fitWidthProperty());
        labBateau.setAlignment(Pos.CENTER);
        labBateau.setTranslateX(30);
        labBateau.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;-fx-background-radius: 40px; -fx-border-radius: 40px;");

        ImageView imgPionsWagon = new ImageView("images/bouton-pions-wagon.png");
        imgPionsWagon.setFitWidth(49);
        imgPionsWagon.setFitHeight(48.5);
        labWagon = new Label();
        labWagon.prefHeightProperty().bind(imgPionsWagon.fitHeightProperty());
        labWagon.prefWidthProperty().bind(imgPionsWagon.fitWidthProperty());
        labWagon.setAlignment(Pos.CENTER);
        labWagon.setTranslateX(30);
        labWagon.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-border-width: 1 ;-fx-background-radius: 40px; -fx-border-radius: 40px;");

        carteDestinationEnMain = new HBox();
        carteDestinationEnMain.prefWidthProperty().bind(VueDuJeu.getWidthProperty().subtract(200));
        carteDestinationEnMain.prefHeightProperty().bind(cartesEnMain.heightProperty());
//        carteDestinationEnMain.setAlignment(Pos.CENTER);
        carteDestinationEnMain.setTranslateX(60);


        VBox v2 = new VBox(h1, new HBox(imgPionsWagon, labWagon, carteDestinationEnMain), new HBox(imgPionsBateau, labBateau));
        v2.setSpacing(5);

        v2.prefHeightProperty().bind(heightProperty());
        v2.prefWidthProperty().bind(widthProperty());
        v2.minHeightProperty().bind(heightProperty());

        getChildren().add(v2);

    }

    ListChangeListener<ICarteTransport> cartesTransportsChange = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends ICarteTransport> change) {
            while(change.next()){
                for(ICarteTransport c: change.getAddedSubList()) {

                    boolean b = false;
                    for (Node node : cartesEnMain.getChildren()) {
                        VueCarteTransport VCT = (VueCarteTransport) node;

                        if (VCT.getCarteTransport().equals(c)){
                            VCT.setNbCarte(VCT.getNbCarte().get()+1);
                            b = true;
                            break;
                        }
                    }
                    if(!b){
                        cartesEnMain.getChildren().add(new VueCarteTransport(c,1));
                        break;
                    }
                }
            }
        }
    };

    ChangeListener<IJoueur> joueurCourantAChange= (observableValue, ancienJoueur, joueurCourant) ->{
        nomJoueur.setText(joueurCourant.getNom() + " : " + joueurCourant.getScore());

        img.setImage(new Image("images/cartesWagons/avatar-" + joueurCourant.getCouleur().name()+ ".png"));
        img.setFitHeight(83);
        img.setFitWidth(105);

        cartesEnMain.getChildren().clear();

        labWagon.textProperty().bind(new SimpleIntegerProperty(joueurCourant.getNbPionsWagon()).asString());
        labBateau.textProperty().bind(new SimpleIntegerProperty(joueurCourant.getNbPionsBateau()).asString());

        for(ICarteTransport c: joueurCourant.getCartesTransport()) {

            boolean b = false;
            for (Node node : cartesEnMain.getChildren()) {
                VueCarteTransport VCT = (VueCarteTransport) node;

                if (VCT.getCarteTransport().equals(c)){
                    VCT.setNbCarte(VCT.getNbCarte().get()+1);
                    b = true;
                    break;
                }
            }
            if(!b){cartesEnMain.getChildren().add(new VueCarteTransport(c,1));}
        }
        joueurCourant.getNbPorts();

        if(joueurCourant.getCartesTransport().size()!=0) {
            joueurCourant.cartesTransportProperty().removeListener(cartesTransportsChange);
            joueurCourant.cartesTransportProperty().addListener(cartesTransportsChange);
        }

        carteDestinationEnMain.getChildren().clear();

        for(IDestination d: joueurCourant.getDestinations()){

            VueDestination des = new VueDestination(d);
            carteDestinationEnMain.getChildren().add(des);
        }

    };
}
