package br.com.infnet.tp2.pages;

import br.com.infnet.tp2.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class ProductsPage extends BasePage {

    private final By viewProductLinks = By.cssSelector("a[href*='/product_details/']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductDetailsPage openFirstProductDetails() {
        List<WebElement> links = $$(viewProductLinks);
        if (links.isEmpty()) throw new IllegalStateException("Nenhum 'View Product' encontrado na ProductsPage.");
        safeClick(links.get(0));
        return new ProductDetailsPage(driver);
    }

    public AddToCartModal addProductToCartById(int productId) {
        By addBtns = By.cssSelector("a.add-to-cart[data-product-id='" + productId + "']");
        List<WebElement> btns = driver.findElements(addBtns);

        WebElement target = btns.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nenhum Add to cart visível para productId=" + productId));

        scrollIntoView(target);
        safeClick(target);
        return new AddToCartModal(driver);
    }

    public AddToCartModal addFirstVisibleProductToCart() {
        By addBtnAny = By.xpath("(//a[contains(@class,'add-to-cart')])[1]");
        WebElement el = wait.until(d -> d.findElement(addBtnAny));
        scrollIntoView(el);
        safeClick(el);
        return new AddToCartModal(driver);
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void safeClick(WebElement el) {
        try {
            wait.until(d -> el.isDisplayed() && el.isEnabled());
            el.click();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
