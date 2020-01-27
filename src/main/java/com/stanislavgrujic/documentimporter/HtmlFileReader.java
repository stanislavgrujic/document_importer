package com.stanislavgrujic.documentimporter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
public class HtmlFileReader {

//  @PostConstruct
  public void readFile() throws IOException {
    ClassPathResource classPathResource = new ClassPathResource("KnowledgeBlocksConceptLeveltestAPI.html");
    File file = classPathResource.getFile();
    Document document = Jsoup.parse(file, "UTF-8");

    Elements allElements = document.body().getAllElements();
    allElements.select(".c2").forEach(element -> System.out.println(element.getAllElements().get(0).childNodes().get(0).toString()));
  }

  public void read() throws IOException {
    File file = new File("KnowledgeBlocksConceptLeveltestAPI.html");
    Jsoup.parse(file, "UTF-8");
  }
}
