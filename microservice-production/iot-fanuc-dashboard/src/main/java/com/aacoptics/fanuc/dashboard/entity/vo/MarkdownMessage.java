package com.aacoptics.fanuc.dashboard.entity.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarkdownMessage {

    private String title;

    private List<String> contents;

    public MarkdownMessage() {
        this.contents = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = "### **" + title + "**";
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public void addContent(String content)
    {
        this.contents.add(content + "  \n");
    }

    public void addBlobContent(String content)
    {
        this.contents.add("**" + content + "**  \n");
    }

    public void addBlankLine(){
        this.contents.add("&nbsp;  \n");
    }

    @Override
    public String toString() {
        String markdown = title + "  \n";
        Iterator<String> contentIterator = contents.iterator();
        while (contentIterator.hasNext()) {
            String content = contentIterator.next();
            markdown += content;
        }
        return markdown;
    }
}
