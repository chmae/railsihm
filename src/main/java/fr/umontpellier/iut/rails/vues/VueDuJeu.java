package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

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
    private Button demandePionsBateau;
    private Button demandePionsWagon;
    private Button afficherDestination;
    private TextField textFieldPions;
    private VBox joueursAvatar;
    private HBox top;
    private VBox middle;
    private HBox boxPionsImg;
    private ImageView imgPionsBateau;
    private ImageView imgPionsWagon;
    private TextField textFieldPionsBateauWagon;
    private SimpleIntegerProperty nbCourant;
    private ImageView imgports;

    private ImageView getImgPionsBateau;
    private  ImageView getImgPionsWagon;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        setStyle(
                "-fx-background-image: url(" +
                        "'images/background.jpg'"+
                        ");"+
                        "-fx-background-size: cover;"
        );
        top = new HBox();
        middle = new VBox();
        plateau = new VuePlateau();
        carteTrans_Dest = new HBox();
        carteVisible = new HBox();
        carteVisible.setAlignment(Pos.CENTER);
        passer = new Button("Passer");
        instruction = new Label();
        listeDestination = new HBox();
        demandePionsBateau = new Button();
        demandePionsWagon = new Button();
        listeDestination.setAlignment(Pos.CENTER);
        joueurCourant=new VueJoueurCourant(jeu);
        textFieldPions = new TextField();
        textFieldPions.setMaxWidth(100);
        joueursAvatar = new VBox();
        afficherCarteBateau = new Button();
        afficherCarteWagon = new Button();
        afficherDestination = new Button();
        textFieldPionsBateauWagon = new TextField();




        imgPionsBateau = new ImageView("images/bouton-pions-bateau.png");
        imgPionsBateau.setFitWidth(49);
        imgPionsBateau.setFitHeight(48.5);

        imgPionsWagon = new ImageView("images/bouton-pions-wagon.png");
        imgPionsWagon.setFitWidth(49);
        imgPionsWagon.setFitHeight(48.5);


        boxPionsImg = new HBox(imgPionsWagon, imgPionsBateau);
        boxPionsImg.setPadding(new Insets(5));
        boxPionsImg.setAlignment(Pos.CENTER);
        boxPionsImg.setSpacing(20);

        joueursAvatar.getChildren().add(boxPionsImg);


        initAvatar();
        resizeBind();

        ImageView imageCarteW = new ImageView();
        ImageView imageCarteB = new ImageView();
        ImageView dest = new ImageView();
        imageCarteW.setImage(new Image("images/cartesWagons/dos-WAGON.png"));
        imageCarteB.setImage(new Image("images/cartesWagons/dos-BATEAU.png"));
        dest.setImage(new Image("images/cartesWagons/destinations.png"));
        imageCarteW.setFitHeight(110);
        imageCarteW.setFitWidth(60);
        imageCarteB.setFitHeight(110);
        imageCarteB.setFitWidth(60);
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
        getImgPionsBateau = new ImageView("images/bouton-pions-bateau.png");
        getImgPionsWagon = new ImageView("images/bouton-pions-wagon.png");

        getImgPionsBateau.setFitWidth(49);
        getImgPionsBateau.setFitHeight(48.5);
        getImgPionsWagon.setFitWidth(49);
        getImgPionsWagon.setFitHeight(48.5);

        demandePionsBateau.setGraphic(getImgPionsBateau);
        demandePionsWagon.setGraphic(getImgPionsWagon);

        demandePionsBateau.setShape(new Circle(40));
        demandePionsWagon.setShape(new Circle(40));

        imgports = new ImageView("images/port.png");
        imgports.setFitWidth(40);
        imgports.setFitHeight(40);

        demandePionsBateau.setOnAction(event -> {
            if (!carteTrans_Dest.getChildren().contains(textFieldPionsBateauWagon)) {
                carteTrans_Dest.getChildren().add(textFieldPionsBateauWagon);
                textFieldPionsBateauWagon.setMinWidth(50);
                textFieldPionsBateauWagon.setMaxWidth(50);
            }

            textFieldPionsBateauWagon.setVisible(true);
            demandePionsBateau.setDisable(true);
            demandePionsWagon.setDisable(true);
            jeu.nouveauxPionsBateauxDemandes();

            textFieldPionsBateauWagon.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String input = textFieldPionsBateauWagon.getText();
                    jeu.leNombreDePionsSouhaiteAEteRenseigne(input);
                    textFieldPionsBateauWagon.clear();
                    textFieldPionsBateauWagon.setVisible(false);
                    demandePionsBateau.setDisable(false);
                    demandePionsWagon.setDisable(false);
                    carteTrans_Dest.getChildren().remove(textFieldPionsBateauWagon);
                }
            });
        });

        demandePionsWagon.setOnAction(event -> {
            if (!carteTrans_Dest.getChildren().contains(textFieldPionsBateauWagon)) {
                carteTrans_Dest.getChildren().add(textFieldPionsBateauWagon);
                textFieldPionsBateauWagon.setMinWidth(50);
                textFieldPionsBateauWagon.setMaxWidth(50);
            }

            textFieldPionsBateauWagon.setVisible(true);
            demandePionsWagon.setDisable(true);
            demandePionsBateau.setDisable(true);
            jeu.nouveauxPionsWagonsDemandes();

            textFieldPionsBateauWagon.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String input = textFieldPionsBateauWagon.getText();
                    jeu.leNombreDePionsSouhaiteAEteRenseigne(input);
                    textFieldPionsBateauWagon.clear();
                    textFieldPionsBateauWagon.setVisible(false);
                    demandePionsWagon.setDisable(false);
                    demandePionsBateau.setDisable(false);
                    carteTrans_Dest.getChildren().remove(textFieldPionsBateauWagon);
                }
            });
        });

        carteTrans_Dest.getChildren().addAll(afficherCarteBateau,afficherCarteWagon,afficherDestination,demandePionsBateau,demandePionsWagon);


        top.getChildren().addAll(plateau, joueursAvatar);
        top.setSpacing(0);

        VBox m = new VBox(carteVisible, joueurCourant);
        m.setSpacing(100);
        middle.getChildren().addAll(passer, instruction, textFieldPions, listeDestination, m, carteTrans_Dest);

        jeu.destinationsInitialesProperty().addListener(destinationsInitiales);
        getChildren().addAll(top,middle);


    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        passer.setOnAction(actionEvent -> jeu.passerAEteChoisi());
        instruction.textProperty().bind(jeu.instructionProperty());
        plateau.creerBindings();
        getJeu().cartesTransportVisiblesProperty().addListener(ecouteCartesVisibles);


        nbCourant = new SimpleIntegerProperty(0);
        textFieldPions.textProperty().addListener(textFieldPionsListener);
        textFieldPions.disableProperty().addListener(textFieldPionsDisable);
        carteTrans_Dest.disableProperty().bind(jeu.jeuEnPreparationProperty());
        carteTrans_Dest.visibleProperty().bind(jeu.jeuEnPreparationProperty().not());
    }

    public void resizeBind(){

        joueursAvatar.prefWidthProperty().bind(widthProperty().multiply(0.5));

        middle.prefWidthProperty().bind(widthProperty());
        middle.prefHeightProperty().bind(heightProperty());

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

                 textFieldPions.setDisable(!listeDestination.getChildren().isEmpty());
                 textFieldPions.setVisible(listeDestination.getChildren().isEmpty());


             }
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

                    textFieldPions.setDisable(!carteVisible.getChildren().isEmpty());
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

    ChangeListener<Boolean> textFieldPionsDisable = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if(!aBoolean) {
                nbCourant.setValue(nbCourant.get() + 1);
                if (nbCourant.get() > jeu.getJoueurs().size()) {
                    middle.getChildren().remove(textFieldPions);
                }
            }
        }
    };
    

    private void initAvatar(){

        VBox v =  new VBox();
        for(IJoueur j: jeu.getJoueurs()){
            if(jeu.getJoueurs().indexOf(j) != 0) {

                v.getChildren().add(new VueAutresJoueurs(jeu.joueurCourantProperty(), j));
            }
        }

        v.setSpacing(50);
        joueursAvatar.getChildren().add(v);
    }


    public IJeu getJeu() {
        return jeu;
    }


}
