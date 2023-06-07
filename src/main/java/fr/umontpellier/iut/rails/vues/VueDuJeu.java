package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends VBox {

    private final IJeu jeu;
    private VuePlateau plateau;

    private Button passer;
    private Label instruction;
    private HBox listeDestination;
    private VueJoueurCourant joueurCourant;

    private HBox carteVisible;
    private HBox carteTrans_Dest;
    private VueCarteTransport afficheCarteVisible;
    private Button afficherCarteWagon;
    private Button afficherCarteBateau;
    private Button afficherDestination;
    private ImageView carteTVisible;
    private TextField textFieldPions;
    private VBox joueursAvatar;
    private HBox top;
    private VBox middle;
    private HBox boxPionsImg;
    private ImageView imgPionsBateau;
    private ImageView imgPionsWagon;


    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        top = new HBox();
        middle = new VBox();
        plateau = new VuePlateau();
        carteTrans_Dest = new HBox();
        carteVisible = new HBox();
        carteVisible.setAlignment(Pos.CENTER);
//        carteVisible.setTranslateY(-35);
        passer = new Button("Passer");
        instruction = new Label();
        listeDestination = new HBox();
        listeDestination.setAlignment(Pos.CENTER);
        joueurCourant=new VueJoueurCourant(jeu);
//        joueurCourant.setTranslateY(65);
        textFieldPions = new TextField();
        textFieldPions.setMaxWidth(100);
        joueursAvatar = new VBox();
        afficherCarteBateau = new Button();
        afficherCarteWagon = new Button();
//        afficheCarteVisible = new Button();
        afficherDestination = new Button();

        imgPionsBateau = new ImageView("images/bouton-pions-bateau.png");
        imgPionsBateau.setFitWidth(49);
        imgPionsBateau.setFitHeight(48.5);

        imgPionsWagon = new ImageView("images/bouton-pions-wagon.png");
        imgPionsWagon.setFitWidth(49);
        imgPionsWagon.setFitHeight(48.5);

        boxPionsImg = new HBox(imgPionsWagon, new Separator(Orientation.VERTICAL), imgPionsBateau);
        boxPionsImg.setPadding(new Insets(5));
        boxPionsImg.setAlignment(Pos.CENTER);
        boxPionsImg.setSpacing(10);
        joueursAvatar.getChildren().add(boxPionsImg);


        initAvatar();
        resizeBind();

        ImageView imageCarteW = new ImageView();
        ImageView imageCarteB = new ImageView();
        ImageView dest = new ImageView();
        imageCarteW.setImage(new Image("images/cartesWagons/dos-WAGON.png"));
        imageCarteB.setImage(new Image("images/cartesWagons/dos-BATEAU.png"));
        dest.setImage(new Image("images/cartesWagons/destinations.png"));
        imageCarteW.setFitHeight(150);
        imageCarteW.setFitWidth(100);
        imageCarteB.setFitHeight(150);
        imageCarteB.setFitWidth(100);
        dest.setFitWidth(150);
        dest.setFitHeight(100);
        afficherCarteWagon.setGraphic(imageCarteW);
        afficherCarteBateau.setGraphic(imageCarteB);
        afficherDestination.setGraphic(dest);
        carteTrans_Dest.setAlignment(Pos.BOTTOM_RIGHT);

        afficherCarteBateau.setOnAction(actionEvent -> ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteBateauAEtePiochee());
        if(afficherCarteBateau==null){
            ((VueDuJeu) getScene().getRoot()).getJeu().piocheBateauVideProperty();
        }
        if(afficherCarteWagon==null){
            ((VueDuJeu) getScene().getRoot()).getJeu().piocheWagonVideProperty();
        }
        afficherCarteWagon.setOnAction(actionEvent -> ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEtePiochee());
        afficherDestination.setOnAction(actionEvent ->((VueDuJeu) getScene().getRoot()).getJeu().nouvelleDestinationDemandee());
        if(afficherDestination == null){
            ((VueDuJeu) getScene().getRoot()).getJeu().piocheDestinationVideProperty();
        }
        carteTrans_Dest.getChildren().addAll(afficherCarteBateau,afficherCarteWagon,afficherDestination);



        top.getChildren().addAll(plateau, joueursAvatar);
        top.setSpacing(0);

        VBox m = new VBox(carteVisible, joueurCourant);
        m.setSpacing(100);
        middle.getChildren().addAll(passer, instruction, textFieldPions, listeDestination, m, carteTrans_Dest);

        jeu.destinationsInitialesProperty().addListener(destinationsInitiales);
        getChildren().addAll(top,middle);

        //joueurCourant.afficherCartes();

    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        passer.setOnAction(actionEvent -> jeu.passerAEteChoisi() );
        instruction.textProperty().bind(jeu.instructionProperty());
        plateau.creerBindings();
        getJeu().cartesTransportVisiblesProperty().addListener(ecouteCartesVisibles);

        textFieldPions.textProperty().addListener(textFieldPionsListener);
        jeu.joueurCourantProperty().addListener(changeJoueur);

    }

    public void resizeBind(){

        joueursAvatar.prefWidthProperty().bind(widthProperty().multiply(0.5));
        joueursAvatar.prefHeightProperty().bind(heightProperty());

        joueurCourant.minHeightProperty().bind(heightProperty().multiply(0.12));
        joueurCourant.maxHeightProperty().bind(heightProperty().multiply(0.12));

    }

    ListChangeListener<IDestination> destinationsInitiales = new ListChangeListener<>() {
         @Override
         public void onChanged(Change<? extends IDestination> change) {
             while(change.next()){
                 if(change.wasAdded()) {
                     for (IDestination d : change.getAddedSubList()) {
                         Button button = new Button(d.getVilles().toString());
                         button.setOnAction(actionEvent -> jeu.uneDestinationAEteChoisie(d));
                         listeDestination.getChildren().add(button);
                     }
                 }
                 else if(change.wasRemoved()){
                     for(IDestination d : change.getRemoved()){
                         Button b = trouverBoutonDestination(listeDestination.getChildren(),d);
                         listeDestination.getChildren().remove(b);
                     }
                 }
             }

             textFieldPions.setVisible(listeDestination.getChildren().isEmpty());
         }
    };

    private Button trouverBoutonDestination(List<Node> contienteltgraphique, IDestination d) {
        for (Node n : contienteltgraphique) {
        Button button = (Button) n;
        if (button.getText().equals(d.getVilles().toString())) {
             return button;
          }
        }
        return null;
    }

    private VueCarteTransport remplacerCarteVisible(List<Node> contienteltgraphique,ICarteTransport c) {
        for (Node n : contienteltgraphique) {
            VueCarteTransport VCT = (VueCarteTransport) n;
            if (VCT.getCarteTransport().equals(c)){
                return VCT;
            }
        }
        return null;
    }


    ListChangeListener<ICarteTransport> ecouteCartesVisibles = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends ICarteTransport> change) {
            Platform.runLater(() -> {
                while (change.next()) {

                    if (change.wasAdded()) {
                        for (ICarteTransport c : change.getAddedSubList()) {
                            afficheCarteVisible = new VueCarteTransport(c, 0);
                            carteVisible.getChildren().add(afficheCarteVisible);
                            afficheCarteVisible.setOnMouseClicked(actionEvent -> ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(c));

                        }
                    }
                    else if (change.wasRemoved()) {
                        for (ICarteTransport c : change.getRemoved()) {
                            VueCarteTransport VCT = remplacerCarteVisible(carteVisible.getChildren(), c);
                            carteVisible.getChildren().remove(VCT);

                        }

                    }


                    textFieldPions.setVisible(carteVisible.getChildren().isEmpty());

                }
            });
        }
    };

    ChangeListener<String> textFieldPionsListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            jeu.leNombreDePionsSouhaiteAEteRenseigne(textFieldPions.getText());
        }
    };

    ChangeListener<IJoueur> changeJoueur = (observableValue, iJoueur, t1) -> initAvatar();

    private void initAvatar(){
        joueursAvatar.getChildren().clear();
        joueursAvatar.getChildren().add(boxPionsImg);
        joueursAvatar.getChildren().add(new Separator());
        ImageView img;

        for(IJoueur j: jeu.getJoueurs()){

            HBox p = new HBox();
            VBox vb = new VBox();

            if(j != jeu.joueurCourantProperty().get()) {


                img = new ImageView("images/cartesWagons/avatar-" + j.getCouleur() + ".png");
                img.setFitHeight(83);
                img.setFitWidth(105);
                vb.getChildren().add(img);

                Label nom = new Label(j.getNom());
                nom.prefWidthProperty().bind(img.fitWidthProperty());
                nom.setAlignment(Pos.CENTER);
                nom.setPadding(new Insets(5));
                vb.getChildren().add(nom);

                p.getChildren().add(vb);

                Label nbPions = new Label("            "+j.getNbPionsWagon());
                nbPions.prefHeightProperty().bind(vb.heightProperty());
                nbPions.setAlignment(Pos.CENTER);

                p.getChildren().add(nbPions); //12 espaces

                Separator sep = new Separator();
                sep.setOrientation(Orientation.VERTICAL);
                sep.setTranslateX(26);
                p.getChildren().add(sep);

                nbPions = new Label("            "+j.getNbPionsBateau());
                nbPions.prefHeightProperty().bind(vb.heightProperty());
                nbPions.setAlignment(Pos.CENTER);
                p.getChildren().add(nbPions);

                joueursAvatar.getChildren().add(p);
                joueursAvatar.getChildren().add(new Separator());
            }
        }
    }


    public IJeu getJeu() {
        return jeu;
    }


}
