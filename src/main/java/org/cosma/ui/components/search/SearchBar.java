package org.cosma.ui.components.search;


import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.cosma.ui.utils.UI;

import java.util.ArrayList;
import java.util.List;

import static org.cosma.ui.utils.Animation.fadeTransition;
import static org.cosma.ui.utils.UI.SEARCH_TRANSITION_TIME;

public class SearchBar {

    private static TextField search;
    private static StackPane resetSearchIcon;

    private static List<Transition> currentTransitions;

    public static void initialize(TextField search, StackPane resetSearchIcon) {
        SearchBar.search = search;
        SearchBar.resetSearchIcon = resetSearchIcon;
        SearchBar.currentTransitions = new ArrayList<>();

        search.setPromptText("Search files...");

        search.setOpacity(0);
        resetSearchIcon.setOpacity(0);

        search.textProperty().addListener((observable, oldValue, newValue) -> check());

        resetSearchIcon.setOnMouseClicked(SearchBar::reset);
    }

    private static void addTransition(Transition transition) {
        if (transition != null) {
            currentTransitions.add(transition);
            transition.setOnFinished(e -> currentTransitions.remove(transition));
        }
    }

    private static void check() {
        if (!UI.isVisible(search))
            return;

        String newValue = search.getText();

        addTransition(fadeTransition(newValue.isBlank(), SEARCH_TRANSITION_TIME, resetSearchIcon));

        //TODO: callback to search
    }


    public static void hide() {
        if (UI.isVisible(search))
            toggle();
    }

    public static void toggle() {
        resetSearchIcon.requestFocus();

        currentTransitions.forEach(Animation::stop);

        search.clear();

        if (UI.isVisible(search))
            addTransition(fadeTransition(true, SEARCH_TRANSITION_TIME, resetSearchIcon));
        else
            search.selectHome();

        addTransition(fadeTransition(UI.isVisible(search), SEARCH_TRANSITION_TIME, search));
    }

    private static void reset(MouseEvent event) {
        resetSearchIcon.setOpacity(0);
        search.clear();
        event.consume();
    }
}
