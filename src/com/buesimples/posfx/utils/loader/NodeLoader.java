package com.buesimples.posfx.utils.loader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.buesimples.posfx.animations.Animations;
import com.buesimples.posfx.utils.constants.Constants;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NodeLoader {

    private static NodeLoader instance = null;

    private Map<String, Node> loadedNodes = new HashMap<>();

    public NodeLoader() {
        instance = this;
    }

    public static NodeLoader getInstance() {
        if (instance == null) {
            instance = new NodeLoader();
        }
        return instance;
    }

    public <T> T load(AnchorPane container, String rootFxml) {
        try {
            // Limpar os filhos do container antes de adicionar um novo nó
            container.getChildren().clear();

            String fxmlView = rootFxml.contains(".") ? rootFxml.replace('.', '/') : rootFxml;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Constants.VIEWS_PACKAGE + fxmlView + "View.fxml"));
            Parent root = fxmlLoader.load();

            // Parent root = FXMLLoader.load(getClass().getResource(Constants.VIEWS_PACKAGE
            // + rootFxml + "View.fxml"));
            container.getChildren().add(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

            Animations.fadeInUp(container, 2.0);

            return fxmlLoader.getController();

        } catch (IOException ex) {
            Logger.getLogger(NodeLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Parent getNode(String fxml) throws IOException {
        return FXMLLoader.load(getClass().getResource(Constants.VIEWS_PACKAGE + fxml + "View.fxml"));
    }

    public void loadNode(AnchorPane node, String nodeName) {
        load(node, nodeName);
    }

    public void loadNodeById(AnchorPane node, String id) {
        load(node, Constants.VIEW_NODE_MAP.get(id));
    }

    public void loadNode1(AnchorPane parent, String nodeName) {
        if (loadedNodes.containsKey(nodeName)) {
            parent.getChildren().clear();
            parent.getChildren().add(loadedNodes.get(nodeName));
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.view(nodeName)));
                Node node = loader.load();
                loadedNodes.put(nodeName, node);
                parent.getChildren().clear();
                parent.getChildren().add(node);
                AnchorPane.setTopAnchor(node, 0.0);
                AnchorPane.setRightAnchor(node, 0.0);
                AnchorPane.setBottomAnchor(node, 0.0);
                AnchorPane.setLeftAnchor(node, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearNode(String nodeName) {
        loadedNodes.remove(nodeName);
    }

    public void clearAllNodes() {
        loadedNodes.clear();
    }

    public void showWindow(Stage stage, String view) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Constants.view(view)));

            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(Constants.TITLE);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Error in Main: " + e.getMessage() + " Linha de Erro: 47");
        }
    }

}
