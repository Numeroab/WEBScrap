package com.exemplo.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GlassdoorScraper {

    public static List<Vaga> buscar(WebDriver driver, String termo) {
        List<Vaga> resultados = new ArrayList<>();

        try {
            String url = "https://www.glassdoor.com.br/Vaga/cascavel-pr-" + termo + "-vagas-SRCH_KO0,4.htm";
            driver.get(url);

            // Espera até que as vagas estejam visíveis
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".react-job-listing")));

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements vagas = doc.select(".react-job-listing");

            for (Element vaga : vagas) {
                // Título da vaga
                String titulo = vaga.select("[data-test=job-title]").text();

                // Local da vaga (empresa ou cidade)
                String local = vaga.select("div[id^=job-employer] span").text();

                // Salário (opcional)
                String salarioText = vaga.select("[data-test=detailSalary]").text();
                Float salario = null;

                if (!salarioText.isEmpty()) {
                    salarioText = salarioText.replaceAll("[^0-9,\\.]", ""); // remove tudo que não for número ou vírgula/ponto
                    salarioText = salarioText.replace(",", "."); // converte vírgula em ponto
                    try {
                        salario = Float.parseFloat(salarioText);
                    } catch (NumberFormatException e) {
                        salario = null; // caso não consiga converter
                    }
                }

                if (!titulo.isEmpty()) {
                    resultados.add(new Vaga(titulo, local, salario)); // ajusta de acordo com seu construtor
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar no Glassdoor: " + e.getMessage());
        }

        return resultados;
    }

}
