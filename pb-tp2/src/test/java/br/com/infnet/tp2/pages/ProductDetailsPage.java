package br.com.infnet.tp2.pages;

import br.com.infnet.tp2.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By quantityInput = By.cssSelector("input#quantity");
    private final By addToCartBtn = By.cssSelector("button[class*='cart']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailsPage setQuantity(int qty) {
        var el = $(quantityInput);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(String.valueOf(qty));
        return this;
    }

    public AddToCartModal addToCart() {
        click(addToCartBtn);
        return new AddToCartModal(driver);
    }
}
