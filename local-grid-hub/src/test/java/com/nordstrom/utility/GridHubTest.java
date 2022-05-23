package com.nordstrom.utility;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.json.Json;
import com.nordstrom.automation.selenium.core.GridUtility;
import com.nordstrom.automation.selenium.core.SeleniumGrid;

public class GridHubTest {

    private static SeleniumGrid seleniumGrid = null;
    private static final String HUB_QUERY = "/grid/api/hub";
    
    @BeforeClass
    public static void beforeClass() {
        seleniumGrid = GridLauncher.launch();
    }

    @Test
    public void testBasicPage() throws IOException {
        URL hubUrl = seleniumGrid.getHubServer().getUrl();
        String json = queryHub(hubUrl);
        Map<String, Object> response = new Json().toType(json, Map.class);
        assertTrue((boolean) response.get("success"));
    }
    
    private static String queryHub(URL hubUrl) throws IOException {
        String json;
        String url = hubUrl.getProtocol() + "://" + hubUrl.getAuthority() + HUB_QUERY;
        try (InputStream is = new URL(url).openStream()) {
            json = GridUtility.readAvailable(is);
        }
        return json;
    }
    
    @AfterClass
    public static void afterClass() throws InterruptedException {
        seleniumGrid.shutdown(true);
    }
    
}
