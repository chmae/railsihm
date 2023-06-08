package fr.umontpellier.iut.rails.vues;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 *
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private SimpleIntegerProperty nbJoueurs;
    private Pane principalPane;
    private HBox boxSelectName;

    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        nbJoueurs = new SimpleIntegerProperty(2); //max 5
        setChangementDuNombreDeJoueursListener(quandLeNombreDeJoueursChange);

        boxSelectName = new HBox(new TextField(), new TextField());
        boxSelectName.setSpacing(20);
        boxSelectName.setAlignment(Pos.CENTER);

        Button butMoins = new Button("-");
        butMoins.setDisable(true);
        Button butPlus = new Button("+");

        butMoins.setOnAction(actionEvent -> {
            if(nbJoueurs.get() > 2) {
                nbJoueurs.setValue(nbJoueurs.getValue() - 1);
                butPlus.setDisable(false);

                if(nbJoueurs.get() == 2){
                    butMoins.setDisable(true);
                }
            }
        });

        butPlus.setOnAction(actionEvent -> {
            if(nbJoueurs.get() < 5) {
                nbJoueurs.setValue(nbJoueurs.getValue() + 1);
                butMoins.setDisable(false);

                if(nbJoueurs.get() == 5){
                    butPlus.setDisable(true);
                }
            }
        });
        HBox boxAddJoueur = new HBox(butMoins, butPlus);
        boxAddJoueur.setSpacing(10);
        boxAddJoueur.setAlignment(Pos.CENTER);

        Button valider = new Button("Valider");

        VBox vb = new VBox(boxSelectName, boxAddJoueur, valider);
        vb.prefWidthProperty().bind(widthProperty());
        vb.prefHeightProperty().bind(heightProperty());
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);

        principalPane = new Pane(vb);

        setScene(new Scene(principalPane));
        setTitle("ChoixDesJoueurs");
        centerOnScreen();

        setMinWidth(Screen.getPrimary().getBounds().getWidth() / 2);
        setMinHeight(Screen.getPrimary().getBounds().getHeight() / 2);

        initAvatar();
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }


    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {
        nbJoueurs.asObject().addListener(quandLeNombreDeJoueursChange);
    }

    ChangeListener<Integer> quandLeNombreDeJoueursChange = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
            if(t1 > integer) {
                boxSelectName.getChildren().add(new TextField());
            }else{
                boxSelectName.getChildren().remove(boxSelectName.getChildren().get(boxSelectName.getChildren().size()-1));
            }
        }
    };

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return nbJoueurs.get();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return "";
    }

    private void initAvatar(){
        //joueursAvatar.getChildren().clear();
        ImageView img;

        for (String j : nomsJoueurs) {

            HBox p = new HBox();
            VBox vb = new VBox();

            img = new ImageView("images/cartesWagons/avatar-" + j + ".png");
            img.setFitHeight(83);
            img.setFitWidth(105);
            vb.getChildren().add(img);

            Label nom = new Label(j);
            nom.prefWidthProperty().bind(img.fitWidthProperty());
            nom.setAlignment(Pos.CENTER);
            vb.getChildren().add(nom);

            p.getChildren().add(vb);




        }
    }


}
