package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static List<String> linkList = new ArrayList<>();
    public static String filePath = "src/main/resources/SiteMap.txt";

    public static void main(String[] args) throws IOException {
        Node root = new Node(null, "https://skillbox.ru/");
        new ForkJoinPool(12).invoke(new LinksParser(root));
        for (int i = 0; i < LinksParser.links.size(); i++) {
            PrintSiteMap(LinksParser.links.get(i), 0);
        }
        SiteMapBuilder siteMap = new SiteMapBuilder(filePath, linkList);
        siteMap.fileSiteMapCreator(siteMap.getLinks(), siteMap.getFilePath());
    }

    public static void PrintSiteMap(Node node, int depth) {
        String linksNode = "";
        PrintTab(depth);
        linksNode += PrintTab(depth);
        linksNode += node.getLink();
        linksNode += "\n";
        linkList.add(linksNode);
        System.out.println(node.getLink());
        for (Node childNode : node.getChildNodes()) {
            if (childNode.getChildNodes().size() != 0) {
                PrintSiteMap(childNode, ++depth);
            }
        }
    }

    public static String PrintTab(int count) {
        return "\t".repeat(Math.max(0, count));
    }
}