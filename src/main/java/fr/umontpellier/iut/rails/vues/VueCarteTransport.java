package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Pane {

    private final ICarteTransport carteTransport;
    private final ImageView img;

    public VueCarteTransport(ICarteTransport carteTransport) {
        this.carteTransport = carteTransport;
        img = getImage(carteTransport);
        img.setFitWidth(160*0.7);
        img.setFitHeight(100*0.7);
        getChildren().add(img);

    }

    static ImageView getImage(ICarteTransport carteTransport) {
        StringBuilder stringBuilder = new StringBuilder();
        if(carteTransport.estBateau()){
            stringBuilder.append("-BATEAU");
        }else if(carteTransport.estWagon()){
            stringBuilder.append("-WAGON");
        }else if(carteTransport.estDouble()){
            stringBuilder.append("-DOUBLE");
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

}
