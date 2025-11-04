# WEBScrap

** LOGIN  automatizado **

public static void login(WebDriver driver, String urlLogin, String email, String senha) throws Exception {
    driver.get(urlLogin);
    Thread.sleep(3000);

    driver.findElement(By.id("email")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys(senha);
    driver.findElement(By.cssSelector("button[type='submit']")).click();

    Thread.sleep(5000); // espera carregar após o login
}

** Sites que serão usados **

1. GlassDoor
2. Indeed
3. Banco Nacional de Emprego

** Conta teste **
golfinha157@gmail.com       senhaFORTE-


