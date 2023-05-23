package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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




    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        passer = new Button("Passer");
        instruction = new Label();
        listeDestination = new HBox();
        joueurCourant=new VueJoueurCourant();
        jeu.destinationsInitialesProperty().addListener(destinationsInitiales);
        getChildren().addAll(plateau,instruction,passer,listeDestination,joueurCourant);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, actionPasserParDefaut);
        instruction.textProperty().bind(jeu.instructionProperty());
        joueurCourant.creerBindings();
        plateau.creerBindings();
    }
    ListChangeListener<IDestination> destinationsInitiales = new ListChangeListener<IDestination>() {
         @Override
         public void onChanged(Change<? extends IDestination> change) {
             while(change.next()){
                 if(change.wasAdded()) {
                     for (IDestination d : change.getAddedSubList()) {
//                         Label destliste = new Label(d.getVilles().toString());
//                         listeDestination.getChildren().add(destliste);
                         Button button = new Button(d.getVilles().toString());
                         listeDestination.getChildren().add(button);
                     }
                 }
                 else if(change.wasRemoved()){
                     for(IDestination d : change.getRemoved()){
                         Button b = trouverBoutonDestination(listeDestination.getChildren(),d);
                         listeDestination.getChildren().remove(b);
                         Button buttonToRemove = trouverBoutonDestination(listeDestination.getChildren(), d);
                         listeDestination.getChildren().remove(buttonToRemove);
                     }
                     }
                 }
             }
         };


//    private Label trouverLabelDestination(List<Node> contienteltgraphique, IDestination d){
//       for(Node n : contienteltgraphique){
//           Label l = (Label) n;
//           if(l.getText().equals(d.getVilles().toString())){
//               return l;
//           }
//       }
//       return null;
//    }
    private Button trouverBoutonDestination(List<Node> contienteltgraphique, IDestination d) {
        for (Node n : contienteltgraphique) {
        Button button = (Button) n;
        if (button.getText().equals(d.getVilles().toString())) {
             return button;
          }
        }
        return null;
        }
    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
