package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

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
    private HBox carteTrans;
    private Button afficheCarteVisible;
    private Button afficherCarteWagon;
    private Button afficherCarteBateau;
    private ImageView carteTVisible;
    private TextField textFieldPions;
    private VBox joueursAvatar;
    private HBox top;



    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        top = new HBox();
        plateau = new VuePlateau();
        carteTrans = new HBox();
        carteVisible = new HBox();
        carteVisible.setAlignment(Pos.CENTER);
        passer = new Button("Passer");
        instruction = new Label();
        listeDestination = new HBox();
        listeDestination.setAlignment(Pos.CENTER);
        joueurCourant=new VueJoueurCourant(jeu);
        textFieldPions = new TextField();
        textFieldPions.setMaxWidth(100);
        joueursAvatar = new VBox();
        afficherCarteBateau = new Button();
        afficherCarteWagon = new Button();
        initAvatar();
        resizeBind();

        ImageView imageCarteW = new ImageView();
        ImageView imageCarteB = new ImageView();
        imageCarteW.setImage(new Image("images/cartesWagons/dos-WAGON.png"));
        imageCarteB.setImage(new Image("images/cartesWagons/dos-BATEAU.png"));
        imageCarteW.setFitHeight(150);
        imageCarteW.setFitWidth(100);
        imageCarteB.setFitHeight(150);
        imageCarteB.setFitWidth(100);
        afficherCarteWagon.setGraphic(imageCarteW);
        afficherCarteBateau.setGraphic(imageCarteB);
        carteTrans.setAlignment(Pos.BOTTOM_CENTER);
        afficherCarteBateau.setOnAction(actionEvent -> ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteBateauAEtePiochee());
        afficherCarteWagon.setOnAction(actionEvent -> ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteWagonAEtePiochee());
        carteTrans.getChildren().addAll(afficherCarteBateau,afficherCarteWagon);


        top.getChildren().addAll(plateau, joueursAvatar);
        top.setSpacing(0);
        jeu.destinationsInitialesProperty().addListener(destinationsInitiales);
        getChildren().addAll(top,passer,instruction, textFieldPions, listeDestination,carteVisible, joueurCourant,carteTrans);

        //joueurCourant.afficherCartes();

    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        passer.setOnAction(actionEvent -> jeu.passerAEteChoisi() );
        instruction.textProperty().bind(jeu.instructionProperty());
        joueurCourant.creerBindings();
        plateau.creerBindings();
        getJeu().cartesTransportVisiblesProperty().addListener(ecouteCartesVisibles);
        for (int i = 0; i < getJeu().getJoueurs().size(); i++) {
            getJeu().getJoueurs().get(i).cartesTransportProperty().addListener(ecouterCartesJoueur);
        }
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
    ListChangeListener<ICarteTransport> ecouterCartesJoueur = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends ICarteTransport> change) {
            change.next();
            Platform.runLater(() -> {
                joueurCourant.afficherCartes();
            });
        }
    };

    ListChangeListener<ICarteTransport> ecouteCartesVisibles = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends ICarteTransport> change) {
            Platform.runLater(() -> {
                while (change.next()) {
                    if (!change.getAddedSubList().isEmpty()) {
                        for (ICarteTransport c : change.getAddedSubList()) {
                            afficheCarteVisible = new Button(); //a voir si a modifier
                            StringBuilder stringBuilder = new StringBuilder();
                            if(c.estBateau()){
                                stringBuilder.append("-BATEAU");
                            }else if(c.estWagon()){
                                stringBuilder.append("-WAGON");
                            }else if(c.estDouble()){
                                stringBuilder.append("-DOUBLE");
                            }else{
                                stringBuilder.append("-JOKER");
                            }

                            stringBuilder.append("-").append(c.getStringCouleur());

                            if(c.getAncre()){
                                stringBuilder.append("-").append("A");
                            }

                            stringBuilder.append(".png");
                            carteTVisible = new ImageView("images/cartesWagons/carte" + stringBuilder);
                            carteTVisible.setFitWidth(160);
                            carteTVisible.setFitHeight(100);
                            afficheCarteVisible.setGraphic(carteTVisible);
                            carteVisible.getChildren().add(afficheCarteVisible);

                        }
                    }
                    textFieldPions.setVisible(carteVisible.getChildren().isEmpty());
                }
            });
        }
    };

    ChangeListener<String> textFieldPionsListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            jeu.leNombreDePionsSouhaiteAEteRenseigne(textFieldPions.getText());
        }
    };

    ChangeListener<IJoueur> changeJoueur = (observableValue, iJoueur, t1) -> initAvatar();

    private void initAvatar(){
        joueursAvatar.getChildren().clear();
        ImageView img;

        for(IJoueur j: jeu.getJoueurs()){

            GridPane p = new GridPane();

            if(j != jeu.joueurCourantProperty().get()) {


                img = new ImageView("images/cartesWagons/avatar-" + j.getCouleur() + ".png");
                img.setFitHeight(83);
                img.setFitWidth(105);
                p.add(img, 0, 0);
                p.add(new Label(j.getNom()), 0, 1);

                joueursAvatar.getChildren().add(p);
            }
        }
    }


    public IJeu getJeu() {
        return jeu;
    }


}
