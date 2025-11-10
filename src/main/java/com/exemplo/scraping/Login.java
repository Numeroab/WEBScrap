package com.exemplo.scraping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Login {

    public static void loginGlassdoor(WebDriver driver) throws Exception {

        String glassURL = "https://www.glassdoor.com.br/index.htm";
        driver.get(glassURL);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Espera o campo de e-mail aparecer
        WebElement emailField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("inlineUserEmail"))
        );
        emailField.sendKeys(Env.getEmail());

        //Clica no botão "Continuar" (ajuste o seletor conforme o HTML real)
        WebElement continuarBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        continuarBtn.click();

        WebElement senhaField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("inlineUserPassword"))
        );
        senhaField.sendKeys(Env.getSenha());

        //Clica novamente no botão "Entrar"
        WebElement entrarBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        entrarBtn.click();

        //Espera o carregamento final
        Thread.sleep(5000);
        System.out.println(" Login Glassdoor realizado com sucesso!");
        
    }

}