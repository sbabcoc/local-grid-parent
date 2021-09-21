package com.nordstrom.utility;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.DriverManager;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.core.SeleniumGrid.GridServer;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.junit.JUnitBase;
import com.nordstrom.automation.selenium.model.Page;

public abstract class AbstractGridTest extends JUnitBase {
    
    private static URI targetUri = null;
    
    @Before
    public void beforeTest() {
        DriverManager.injectDriver(this, null);
    }

    @Test
    public void test() {
        InitialPage initialPage = this.getClass().getAnnotation(InitialPage.class);
        ExamplePage page = Page.openInitialPage(initialPage, getDriver(), getTargetUri());
        assertEquals(ExamplePage.TITLE, page.getTitle());
    }
    
    @After
    public void afterTest() throws InterruptedException {
        DriverManager.closeDriver(this);
    }
    
    public URI getTargetUri() {
        if (targetUri == null) {
            try {
                SeleniumGrid seleniumGrid = (SeleniumGrid)
                        MethodUtils.invokeExactStaticMethod(this.getClass(), "getSeleniumGrid");
                GridServer hubServer = seleniumGrid.getHubServer();
                URL hubUrl = hubServer.getUrl();
                targetUri = URI.create(hubUrl.getProtocol() + "://" + hubUrl.getAuthority());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(
                        "Failed getting selenium grid from class: " + this.getClass().getName(), e);
            }
        }
        return targetUri;
    }
    
    
}
