package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

public class LinksParser extends RecursiveAction {

    private final Node parentNode;

    private static final String LINKS_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@/%?=~_|!:,.;]*[-a-zA-Z0-9+&@/%=~_|]";

    public static final CopyOnWriteArrayList<Node> links = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArraySet<String> linksHashSet = new CopyOnWriteArraySet<>();

    public LinksParser(Node parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    protected void compute() {
        List<LinksParser> taskList = new ArrayList<>();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {

            if (checkDuplicateLinks(parentNode.getLink()) && checkCycleLink(parentNode)) {
                Document doc = getSitePage(parentNode.getLink());
                setChildLinks(filterElementsCreator(doc), parentNode);
                System.out.println(parentNode.getLink());
            }


        } catch (IOException e) {
            System.out.println("Error connection");
            System.out.println(parentNode.getLink());
        }

        for (Node child : parentNode.getChildNodes()) {
            LinksParser task = new LinksParser(child);
            if (checkDuplicateTask(task)) {
                taskList.add(task);
                links.add(task.parentNode);
            }
        }
        invokeAll(taskList);
    }

    private boolean checkDuplicateTask(LinksParser taskList) {
        for (Node node : links) {
            if (node.getLink().compareTo(taskList.parentNode.getLink()) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDuplicateNode(Node currentNode) {
        for (Node node : links) {
            if (node.getLink().compareTo(currentNode.getLink()) == 0) {
                return false;
            }
        }
        return true;
    }

    private Document getSitePage(String url) throws IOException {
        return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .referrer("http://www.google.com").get();
    }

    private Elements filterElementsCreator(Document document) {
        return document.select("a[href]").not("noscript");
    }

    private boolean checkCycleLink(Node node) {
        if (node.getParentNode() != null) {
            for (Node childNode : node.getChildNodes()) {
                if (childNode.getLink().compareTo(node.getParentNode().getLink()) == 0 ||
                        childNode.getLink().compareTo(node.getLink()) == 0) {
                    return false;
                }
            }
            new Thread(() -> checkCycleLink(node.getParentNode())).start();
        }
        return true;
    }

    private boolean checkDuplicateLinks(String currentLink) {
        for (String link : linksHashSet) {
            if (link.compareTo(currentLink) == 0) {
                return false;
            }
        }
        return true;
    }

    private void setChildLinks(Elements elements, Node node) {
        CopyOnWriteArrayList<Node> nodes = new CopyOnWriteArrayList<>();
        if (checkDuplicateNode(node)){
            links.add(node);
        }
        linksHashSet.add(node.getLink());
        for (Element element : elements) {
            String link = element.attr("abs:href");
            if (link.matches(LINKS_REGEX) && link.contains("https://skillbox.ru/") && !link.contains(".pdf") && !link.contains(".com")) {
                Node nodeChild = new Node(node, link);
                if (checkDuplicateNode(nodeChild)){
                    nodes.add(nodeChild);
                }
            }
            node.setChildNodes(nodes);
        }
    }
}
