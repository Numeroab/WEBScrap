package com.exemplo.scraping;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class App {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        try {
            String termo = "java developer cascavel";

            System.out.println("=== GLASSDOOR ===");
            List<Vaga> glassdoor = GlassdoorScraper.buscar(driver, termo);
            glassdoor.forEach(System.out::println);

            System.out.println("\n=== BNE ===");
            List<Vaga> bne = BneScraper.buscar(driver, termo);
            bne.forEach(System.out::println);

            System.out.println("\n=== INDEED ===");
            List<Vaga> indeed = IndeedScraper.buscar(driver, termo);
            indeed.forEach(System.out::println);

        } finally {
            driver.quit();
        }
    }
}
