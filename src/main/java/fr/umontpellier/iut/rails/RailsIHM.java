package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.mecanique.Jeu;
import fr.umontpellier.iut.rails.vues.DonneesGraphiques;
import fr.umontpellier.iut.rails.vues.VueChoixJoueurs;
import fr.umontpellier.iut.rails.vues.VueDuJeu;
import fr.umontpellier.iut.rails.vues.VueResultats;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RailsIHM extends Application {

    private VueChoixJoueurs vueChoixJoueurs;
    private Stage primaryStage;
    private Jeu jeu;
    private RailsIHM railsIHM;

    private final boolean avecVueChoixJoueurs = true;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        railsIHM = this;
        debuterJeu();
    }

    public void debuterJeu() {
        if (avecVueChoixJoueurs) {
            vueChoixJoueurs = new VueChoixJoueurs();
            vueChoixJoueurs.setNomsDesJoueursDefinisListener(quandLesNomsJoueursSontDefinis);
            vueChoixJoueurs.show();
            vueChoixJoueurs.setOnCloseRequest(event -> {
                this.arreterJeu();
                event.consume();
            });

        } else {
            demarrerPartie();
        }
    }

    public void demarrerPartie() {
        List<String> nomsJoueurs;
        if (avecVueChoixJoueurs)
            nomsJoueurs = vueChoixJoueurs.getNomsJoueurs();
        else {
            nomsJoueurs = new ArrayList<>();
            nomsJoueurs.add("Guybrush");
            nomsJoueurs.add("Largo");
            nomsJoueurs.add("LeChuck");
            nomsJoueurs.add("Elaine");
        }
        jeu = new Jeu(nomsJoueurs.toArray(new String[0]));
        VueDuJeu vueDuJeu = new VueDuJeu(jeu);
        Scene scene = new Scene(vueDuJeu, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran); // la scene doit être créée avant de mettre en place les bindings

        vueDuJeu.creerBindings();
        jeu.run(); // le jeu doit être démarré après que les bindings ont été mis en place
        primaryStage.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 2 + 100);
        primaryStage.setMinHeight(Screen.getPrimary().getBounds().getHeight() / 2 + 340);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rails");
        primaryStage.centerOnScreen();
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
        primaryStage.show();
        jeu.finDePartieProperty().addListener(finDujeux);

    }

    ChangeListener<Boolean> finDujeux = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if(t1){
                VueResultats VR = new VueResultats(railsIHM);
                closeStage();
                primaryStage.setScene(new Scene(VR, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran));
                openStage();
            }
        }
    };

    public void closeStage(){primaryStage.close();}
    public void openStage(){primaryStage.show();}

    private final ListChangeListener<String> quandLesNomsJoueursSontDefinis = change -> {
        if (!vueChoixJoueurs.getNomsJoueurs().isEmpty())
            demarrerPartie();
    };

    public void arreterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("On arrête de jouer ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public static void main(String[] args) {
        launch(args);
    }

}