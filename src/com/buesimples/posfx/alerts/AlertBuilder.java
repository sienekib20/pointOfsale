package com.buesimples.posfx.alerts;

import java.io.IOException;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.alerts.AlertBuilderController;
import com.buesimples.posfx.utils.constants.Constants;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

public class AlertBuilder {

    private static String title;

    private static JFXDialog dialog;

    private static IndexController instanceIndex = null;

    private static AlertBuilder instance;

    public AlertBuilder() {
        instance = this;
    }

    public static AlertBuilder build() {
        new AlertBuilder();
        if (instanceIndex == null) {
            instanceIndex = IndexController.getInstance();
        }
        return instance;
    }

    public void create(AlertType type, Node nodeToDisable, String body) {
        try {
            setFunction(type);

            StackPane dialogContainer = instanceIndex.getRootNode();
            AnchorPane nodeToBlur = instanceIndex.getExtendedNode();

            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource(Constants.view("alerts.AlertBuilder")));

            AnchorPane root = fxml.load();
            root.setStyle("-fx-background-color: #2B2738");
            AlertBuilderController alertBuilder = fxml.getController();

            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

            dialog = new JFXDialog();
            dialog.setContent(root);
            dialog.setDialogContainer(dialogContainer);
            dialog.setBackground(Background.EMPTY);
            dialog.getStyleClass().add("jfx-dialog-overlay-pane");
            dialog.setTransitionType(DialogTransition.TOP);
            
            alertBuilder.setAlertData(title, body, dialog);

            dialog.show();

            dialog.setOnDialogOpened(e -> {
                nodeToDisable.setDisable(true);
                nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
            });

            dialog.setOnDialogClosed(e -> {
                nodeToDisable.setDisable(false);
                nodeToBlur.setEffect(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCESS:
                title = "Successo!";
                break;

            case ERROR:
                title = "Oops!";
                break;
        }
    }

}
