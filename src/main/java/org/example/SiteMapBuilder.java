package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SiteMapBuilder {
    private final String filePath;
    private final List<String> links;

    public SiteMapBuilder(String filePath, List<String> links) {
        this.filePath = filePath;
        this.links = links;
    }

    public void fileSiteMapCreator(List<String> links, String filePath) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        for (String linksNode : links){
            out.write(linksNode.getBytes());
        }
        out.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getLinks() {
        return links;
    }
}
