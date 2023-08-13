package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node {
    private final Node parentNode;
    private CopyOnWriteArrayList<Node> childNodes;

    private final String link;

    public Node(Node parentNode, String link) {
        this.parentNode = parentNode;
        this.link = link;
        this.childNodes = new CopyOnWriteArrayList<>();
    }

    //Getters

    public Node getParentNode() {
        return parentNode;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public String getLink() {
        return link;
    }

    // Setters

    public void setChildNodes(CopyOnWriteArrayList<Node> childNodes) {
        this.childNodes = childNodes;
    }

}
