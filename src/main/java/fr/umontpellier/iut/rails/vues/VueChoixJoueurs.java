package fr.umontpellier.iut.rails.vues;

import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.SocketImpl;
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
    private VBox boxSelectName;
    private HBox unSelectName;
    private HBox deuxSelectName;

    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    public VueChoixJoueurs() {


        BorderPane root = new BorderPane();

        nomsJoueurs = FXCollections.observableArrayList();
        nbJoueurs = new SimpleIntegerProperty(2); //max 5
        setChangementDuNombreDeJoueursListener(quandLeNombreDeJoueursChange);

        root.setStyle(
                "-fx-background-image: url(" +
                        "'images/background.jpg'"+
                        ");"+
                "-fx-background-size: stretch;"
        );

        //PARTIE TEXTFIELD POUR LE NOM DES JOUEURS

        TextField TF1 = new TextField();
        TF1.setOnAction(actionEvent -> setListeDesNomsDeJoueurs());
        TF1.setStyle("-fx-border-color: #fba76c; -fx-border-width: 2 ;-fx-background-radius: 5px; -fx-border-radius: 5px;");

        TextField TF2 = new TextField();
        TF2.setOnAction(actionEvent -> setListeDesNomsDeJoueurs());
        TF2.setStyle("-fx-border-color: #fba76c; -fx-border-width: 2 ;-fx-background-radius: 5px; -fx-border-radius: 5px;");

        unSelectName = new HBox(TF1, TF2);
        unSelectName.setAlignment(Pos.CENTER);
        unSelectName.setSpacing(50);
        unSelectName.setTranslateX(-50);

        deuxSelectName = new HBox();
        deuxSelectName.setSpacing(50);
        deuxSelectName.setAlignment(Pos.CENTER);
        deuxSelectName.setTranslateX(-50);

        boxSelectName = new VBox(unSelectName, deuxSelectName);
        boxSelectName.setSpacing(20);
        boxSelectName.setAlignment(Pos.CENTER);

        boxSelectName.setSpacing(150);


        root.setCenter(boxSelectName);

        //FIN DE LA PARTIE TEXTEFIELD POUR LE NOM DES JOUEURS

        //PARTIE CHOIX DU NOMBRE

        ToggleGroup group = new ToggleGroup();

        VBox boxAddJoueur = new VBox();

        for(int i=5; i>1; i--){
            RadioButton but = new RadioButton(""+i);
            but.setToggleGroup(group);
            if(i == 2){but.setSelected(true);}

            but.setOnAction(actionEvent -> {
                creationTxt(nbJoueurs.get(), Integer.parseInt(but.getText()));
            });
            but.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    creationTxt(nbJoueurs.get(), Integer.parseInt(but.getText()));
                }
            });

            boxAddJoueur.getChildren().add(but);
        }

        boxAddJoueur.setSpacing(10);
        boxAddJoueur.setAlignment(Pos.CENTER);

        Button valider = new Button("Valider");
        valider.setOnAction(actionEvent -> setListeDesNomsDeJoueurs());
        valider.setOnMousePressed(mouseEvent -> valider.setStyle("-fx-border-color: white; -fx-border-width: 1 ;-fx-background-radius: 40px; -fx-border-radius: 40px;"));
        valider.setOnMouseReleased(mouseEvent -> valider.setStyle("-fx-border-color: #fba76c; -fx-border-width: 1 ;-fx-background-radius: 40px; -fx-border-radius: 40px;"));
        valider.setStyle("-fx-border-color: #fba76c; -fx-border-width: 1 ; -fx-background-radius: 40px; -fx-border-radius: 40px;");

        Label lab = new Label("Combien de personnes vont jouer au jeu ?");
        lab.setStyle("-fx-background-color: white; -fx-border-color: #fba76c; -fx-padding: 5; -fx-border-width: 1 ;-fx-background-radius: 40px; -fx-border-radius: 40px;");
        VBox adETval = new VBox(lab,boxAddJoueur, valider);
        HBox adETvalETsep = new HBox(new Separator(Orientation.VERTICAL), adETval);

        adETval.setSpacing(50);
        adETval.setAlignment(Pos.CENTER);
        adETval.prefHeightProperty().bind(adETvalETsep.heightProperty());

        adETval.translateXProperty().bind(adETvalETsep.widthProperty().divide(4));
        adETvalETsep.setTranslateX(-100);

        root.setRight(adETvalETsep);

        //FIN PARTIE CHOIX DU NOMBRE

//        root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dcb688, #346364)");

        setScene(new Scene(root));
        setTitle("ChoixDesJoueurs");
        centerOnScreen();

        setMinWidth(Screen.getPrimary().getBounds().getWidth() / 1.8);
        setMinHeight(Screen.getPrimary().getBounds().getHeight() / 1.8);

    }

    private void creationTxt(int integer, int t1){
        if(t1 > integer) {

            for(int i=integer+1; i<=t1; i++) {

                TextField TF = new TextField();
                TF.setOnAction(actionEvent -> setListeDesNomsDeJoueurs());
                TF.setStyle("-fx-border-color: #fba76c; -fx-border-width: 2 ;-fx-background-radius: 5px; -fx-border-radius: 5px;");

                if(i > 3) {
                    deuxSelectName.getChildren().add(TF);
                }else{
                    unSelectName.getChildren().add(TF);
                }
            }
        }else{
            for(int i=integer; i>t1; i--) {
                if(i > 3) {
                    deuxSelectName.getChildren().remove(deuxSelectName.getChildren().get(deuxSelectName.getChildren().size() - 1));
                }else{
                    unSelectName.getChildren().remove(unSelectName.getChildren().get(unSelectName.getChildren().size() - 1));
                }
            }
        }

        nbJoueurs.setValue(t1);
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
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Number> quandLeNombreDeJoueursChange) {
        nbJoueurs.asObject().addListener(quandLeNombreDeJoueursChange);
    }

    ChangeListener<Number> quandLeNombreDeJoueursChange = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number integer, Number t1) {


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

        if(playerNumber > 3){
            return ((TextField)deuxSelectName.getChildren().get(playerNumber-4)).getText();
        }else{
            return ((TextField)unSelectName.getChildren().get(playerNumber-1)).getText();
        }
    }


}
