package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.RailsIHM;
import fr.umontpellier.iut.rails.mecanique.Joueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.min;

/**
 * Cette classe affiche les scores en fin de partie.
 * On peut éventuellement proposer de rejouer, et donc de revenir à la fenêtre principale
 *
 */
public class VueResultats extends Pane {

    private RailsIHM ihm;
    private Pane back;
    private VBox informations;

    public VueResultats(RailsIHM ihm) {
        this.ihm = ihm;
        setStyle(
                "-fx-background-image: url(" +
                        "'images/fin-background.png'"+
                        ");"+
                        "-fx-background-size: stretch;"
        );

        informations = new VBox();
        initFinPersonnage();
        getChildren().add(informations);
        informations.prefWidthProperty().bind(widthProperty());
        informations.prefHeightProperty().bind(heightProperty());
        informations.setAlignment(Pos.CENTER);
        informations.setSpacing(100.0/ihm.getJeu().getJoueurs().size());


        Button rejouer = new Button("Rejouer");
        rejouer.setOnAction(actionEvent -> {
            ihm.closeStage();
            ihm.debuterJeu();
        });
        rejouer.setAlignment(Pos.BOTTOM_RIGHT);
        rejouer.layoutXProperty().bind(widthProperty().subtract(rejouer.widthProperty()).subtract(180));
        rejouer.layoutYProperty().bind(heightProperty().subtract(rejouer.heightProperty()).subtract(120));
        getChildren().add(rejouer);

    }

    private void initFinPersonnage() {

        int max = MIN_VALUE;

        for(Joueur ij: getPodium(new ArrayList<>(ihm.getJeu().getJoueurs()))){

            ImageView img;
            Label nomJoueur;

            img = new ImageView("images/cartesWagons/avatar-" + ij.getCouleur() + ".png");
            img.setFitHeight(83);
            img.setFitWidth(105);

            nomJoueur = new Label(ij.getNom());
            nomJoueur.prefWidthProperty().bind(img.fitWidthProperty());
            nomJoueur.setAlignment(Pos.CENTER);
            nomJoueur.setPadding(new Insets(5));
            nomJoueur.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 2; -fx-border-width: 1 ;");

            VBox vb = new VBox(img, nomJoueur);
            vb.setSpacing(10);

            Separator sep = new Separator(Orientation.VERTICAL);
            sep.setStyle(
                    "-fx-background-color: #fba76c;"+
                "-fx-pref-height: 5;"+
                "-fx-border-color: null ;"
            );

            Label points = new Label(""+ij.calculerScoreFinal());
            points.setAlignment(Pos.CENTER);
            VBox labs = new VBox(new Label("Points finaux : "), points);
            points.prefWidthProperty().bind(labs.widthProperty());
            labs.setSpacing(10);

            HBox hb = new HBox(vb, sep , labs);
            hb.setSpacing(50);
            hb.setTranslateX(-50);
            hb.setAlignment(Pos.CENTER);

            labs.prefHeightProperty().bind(hb.heightProperty());
            labs.setAlignment(Pos.CENTER);

            if(ij.calculerScoreFinal() >= max){
                max = ij.calculerScoreFinal();
                ImageView premier = new ImageView("images/premier.png");
                premier.setFitHeight(40);
                premier.setFitWidth(40);
                hb.getChildren().add(premier);
            }

            informations.getChildren().add(hb);
        }
    }

    private List<Joueur> getPodium(List<Joueur> lst){

        if(lst.isEmpty()){return new ArrayList<>();}

        ArrayList<Joueur> joueurs = new ArrayList<>();

        int max = MIN_VALUE;

        for(Joueur j: lst){
            if(j.calculerScoreFinal() > max){
                max = j.calculerScoreFinal();
                joueurs.clear();
                joueurs.add(j);
            }else if(j.calculerScoreFinal() == max){
                joueurs.add(j);
            }
        }

        lst.removeAll(joueurs);
        joueurs.addAll(getPodium(lst));

        return joueurs;

    }

}
