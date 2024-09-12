package com.buesimples.posfx.alerts;

import java.io.IOException;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.alerts.AlertBuilderController;
import com.buesimples.posfx.controllers.alerts.ConfirmationController;
import com.buesimples.posfx.controllers.alerts.PlaceOrderController;
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

    private static JFXDialog dialogGenerator;

    private static IndexController instanceIndex = null;

    private static AlertBuilder instance;
    
    private static StackPane dialogContainer;
    
    private static AnchorPane nodeToBlur;

    public AlertBuilder() {
        instance = this;
    }

    public static AlertBuilder build() {
        new AlertBuilder();
        
        if (instanceIndex == null) {
            instanceIndex = IndexController.getInstance();
        }
        
        dialogContainer = instanceIndex.getRootNode();
        nodeToBlur = instanceIndex.getExtendedNode();
        
        return instance;
    }

    public void create(AlertType type, Node nodeToDisable, String body) {
        try {
            setFunction(type);



            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource(Constants.view("alerts.AlertBuilder")));

            AnchorPane root = fxml.load();
            root.setStyle("-fx-background-color: #2B2738");
            AlertBuilderController alertBuilder = fxml.getController();

            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

            JFXDialog dialog = createDialog(dialogContainer, root);

            alertBuilder.setAlertData(title, body, dialog);

            dialog.show();
            actionDialog(dialog, nodeToDisable, nodeToBlur);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confirm(AlertType type, Node nodeToDisable, String body) {
        try {
            setFunction(type);

            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource(Constants.view("alerts.Confirmation")));

            AnchorPane root = fxml.load();
            root.setStyle("-fx-background-color: #2B2738");
            ConfirmationController alertBuilder = fxml.getController();

            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

            JFXDialog dialog = createDialog(dialogContainer, root);

            alertBuilder.setAlertData(title, body, dialog);

            dialog.show();
            actionDialog(dialog, nodeToDisable, nodeToBlur);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void placeOrder(AlertType type, Node nodeToDisable, String body) {
        try {
            setFunction(type);

            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource(Constants.view("alerts.PlaceOrder")));

            AnchorPane root = fxml.load();
            root.setStyle("-fx-background-color: #2B2738");
            PlaceOrderController orderController  = fxml.getController();

            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

            JFXDialog dialog = createDialog(dialogContainer, root);

            orderController.setAlertData(title, body, dialog);

            dialog.show();
            actionDialog(dialog, nodeToDisable, nodeToBlur);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private JFXDialog createDialog(StackPane dialogContainer, AnchorPane root) {            
        dialogGenerator = new JFXDialog();
        dialogGenerator.setContent(root);
        dialogGenerator.setDialogContainer(dialogContainer);
        dialogGenerator.setBackground(Background.EMPTY);
        dialogGenerator.getStyleClass().add("jfx-dialog-overlay-pane");
        dialogGenerator.setTransitionType(DialogTransition.TOP);
        
        return dialogGenerator;
    }
    
    void actionDialog(JFXDialog dialog, Node nodeToDisable, AnchorPane nodeToBlur) {
        dialog.setOnDialogOpened(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
        });

        dialog.setOnDialogClosed(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }

    public static void close() {
        if (dialogGenerator != null) {
            dialogGenerator.close();
        }
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCESS:
                title = "Successo!";
                break;

            case ERROR:
                title = "Erro!";
                break;
            case INFORMATION:
                title = "Info!";
                break;
            case WARNING:
                title = "Aviso!";
                break;
        }
    }


}
