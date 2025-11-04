package com.exemplo.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class IndeedScraper {

    public static List<Vaga> buscar(WebDriver driver, String termo) {
        List<Vaga> resultados = new ArrayList<>();
        try {
            String url = "https://br.indeed.com/jobs?q=" + termo + "&l=";
            driver.get(url);
            Thread.sleep(5000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements vagas = doc.select(".job_seen_beacon");

            for (Element vaga  : vagas) {
                String titulo = vaga.select("h2 span").text();
                String empresa = vaga.select(".companyName").text();
                String local = vaga.select(".companyLocation").text();
                Float salario = vaga.select(".salary-snippet").isEmpty() ? null : Float.parseFloat(vaga.select(".salary-snippet").text().replaceAll("[^\\d]", ""));

                if (!titulo.isEmpty())
                    resultados.add(new Vaga(titulo, empresa, local, salario));
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar no Indeed: " + e.getMessage());
        }
        return resultados;
    }
}
