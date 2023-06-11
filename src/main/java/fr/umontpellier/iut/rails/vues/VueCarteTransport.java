package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Pane {

    private final ICarteTransport carteTransport;
    private final ImageView img;

    private SimpleIntegerProperty nbCarte;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCarte) {
        this.carteTransport = carteTransport;
        img = getImage(carteTransport);
        img.setFitWidth(160*0.7);
        img.setFitHeight(100*0.7);
        getChildren().add(img);

        if(nbCarte != 0) {
            this.nbCarte = new SimpleIntegerProperty(nbCarte);

            Circle nbCircle = new Circle(10);
            nbCircle.setFill(Color.BLACK);
            nbCircle.setCenterX(4);
            nbCircle.setCenterY(8);

            Label nbTxt = new Label();
            nbTxt.textProperty().bind(this.nbCarte.asString());
            nbTxt.setTextFill(Color.WHITE);

            getChildren().addAll(nbCircle, nbTxt);

//            setOnMouseClicked(mouseEvent -> {
//                System.out.println("ok cliquer");
//                ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteDuJoueurEstJouee(carteTransport);
//            });
        }

    }

     static ImageView getImage(ICarteTransport carteTransport) {
        StringBuilder stringBuilder = new StringBuilder();

        if (carteTransport.estDouble()) {
            stringBuilder.append("-DOUBLE");
        } else if (carteTransport.estWagon()) {
            stringBuilder.append("-WAGON");
        } else if (carteTransport.estBateau()) {
            stringBuilder.append("-BATEAU");
        }else{
            stringBuilder.append("-JOKER");
        }


         stringBuilder.append("-").append(carteTransport.getStringCouleur());

        if(carteTransport.getAncre()){
            stringBuilder.append("-").append("A");
        }

        stringBuilder.append(".png");

        return new ImageView("images/cartesWagons/carte" + stringBuilder);
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

    public SimpleIntegerProperty getNbCarte() {
        return nbCarte;
    }

    public void setNbCarte(int nbCarte) {
        this.nbCarte.setValue(nbCarte);
    }
}
