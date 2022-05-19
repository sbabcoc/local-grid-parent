package com.nordstrom.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNoException;
import static com.nordstrom.automation.selenium.examples.ExamplePage.*;
import static org.junit.Assert.assertArrayEquals;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import com.nordstrom.automation.selenium.DriverPlugin;
import com.nordstrom.automation.selenium.SeleniumConfig;
import com.nordstrom.automation.selenium.annotations.InitialPage;
import com.nordstrom.automation.selenium.core.GridUtility;
import com.nordstrom.automation.selenium.core.SeleniumGrid;
import com.nordstrom.automation.selenium.examples.ExamplePage;
import com.nordstrom.automation.selenium.examples.FrameComponent;
import com.nordstrom.automation.selenium.examples.ShadowRootComponent;
import com.nordstrom.automation.selenium.examples.TableComponent;
import com.nordstrom.automation.selenium.exceptions.ShadowRootContextException;
import com.nordstrom.automation.selenium.interfaces.DriverProvider;
import com.nordstrom.automation.selenium.junit.JUnitBase;

@InitialPage(ExamplePage.class)
public abstract class AbstractGridTest extends JUnitBase implements DriverProvider {
    
    private static SeleniumGrid seleniumGrid = null;
    
    @Before
    public void beforeTest() {
        launchSeleniumGrid();
        ExamplePage.setHubAsTarget();
    }

    @Test
    public void testBasicPage() {
        ExamplePage page = getInitialPage();
        assertEquals(page.getTitle(), TITLE);
    }
    
    @Test
    public void testParagraphs() {
        ExamplePage page = getInitialPage();
        List<String> paraList = page.getParagraphs();
        assertEquals(paraList.size(), 3);
        assertArrayEquals(paraList.toArray(), PARAS);
    }
    
    @Test
    public void testTable() {
        ExamplePage page = getInitialPage();
        TableComponent component = page.getTable();
        verifyTable(component);
    }
    
    /**
     * Verify the contents of the specified table component
     * 
     * @param component table component to be verified
     */
    private static void verifyTable(TableComponent component) {
        assertArrayEquals(component.getHeadings().toArray(), HEADINGS);
        List<List<String>> content = component.getContent();
        assertEquals(content.size(), 3);
        assertArrayEquals(content.get(0).toArray(), CONTENT[0]);
        assertArrayEquals(content.get(1).toArray(), CONTENT[1]);
        assertArrayEquals(content.get(2).toArray(), CONTENT[2]);
    }
    
    @Test
    public void testFrameByLocator() {
        ExamplePage page = getInitialPage();
        FrameComponent component = page.getFrameByLocator();
        assertEquals(component.getPageContent(), FRAME_A);
    }
    
    @Test
    public void testFrameByElement() {
        ExamplePage page = getInitialPage();
        FrameComponent component = page.getFrameByElement();
        assertEquals(component.getPageContent(), FRAME_B);
    }
    
    @Test
    public void testFrameByIndex() {
        ExamplePage page = getInitialPage();
        FrameComponent component = page.getFrameByIndex();
        assertEquals(component.getPageContent(), FRAME_C);
    }
    
    @Test
    @Ignore
    public void testFrameById() {
        ExamplePage page = getInitialPage();
        FrameComponent component = page.getFrameById();
        assertEquals(component.getPageContent(), FRAME_D);
    }
    
    @Test
    public void testComponentList() {
        ExamplePage page = getInitialPage();
        List<TableComponent> componentList = page.getTableList();
        verifyTable(componentList.get(0));
    }
    
    @Test
    public void testComponentMap() {
        ExamplePage page = getInitialPage();
        Map<Object, TableComponent> componentMap = page.getTableMap();
        verifyTable(componentMap.get(TABLE_ID));
    }
    
    @Test
    public void testFrameList() {
        ExamplePage page = getInitialPage();
        List<FrameComponent> frameList = page.getFrameList();
        assertEquals(frameList.size(), 4);
        assertEquals(frameList.get(0).getPageContent(), FRAME_A);
        assertEquals(frameList.get(1).getPageContent(), FRAME_B);
        assertEquals(frameList.get(2).getPageContent(), FRAME_C);
        assertEquals(frameList.get(3).getPageContent(), FRAME_D);
    }
    
    @Test
    public void testFrameMap() {
        ExamplePage page = getInitialPage();
        Map<Object, FrameComponent> frameMap = page.getFrameMap();
        assertEquals(frameMap.size(), 4);
        assertEquals(frameMap.get(FRAME_A).getPageContent(), FRAME_A);
        assertEquals(frameMap.get(FRAME_B).getPageContent(), FRAME_B);
        assertEquals(frameMap.get(FRAME_C).getPageContent(), FRAME_C);
        assertEquals(frameMap.get(FRAME_D).getPageContent(), FRAME_D);
    }
    
    @Test
    public void testShadowRootByLocator() {
        ExamplePage page = getInitialPage();
        
        try {
            ShadowRootComponent shadowRoot = page.getShadowRootByLocator();
            assertEquals(shadowRoot.getHeading(), SHADOW_DOM_A);
        } catch (ShadowRootContextException e) {
            assumeNoException(e);
        }
    }
    
    @Test
    public void testShadowRootByElement() {
        ExamplePage page = getInitialPage();
        
        try {
            ShadowRootComponent shadowRoot = page.getShadowRootByElement();
            assertEquals(shadowRoot.getHeading(), SHADOW_DOM_B);
        } catch (ShadowRootContextException e) {
            assumeNoException(e);
        }
    }
    
    @Test
    public void testShadowRootList() {
        ExamplePage page = getInitialPage();
        
        try {
            List<ShadowRootComponent> shadowRootList = page.getShadowRootList();
            assertEquals(shadowRootList.size(), 2);
            assertEquals(shadowRootList.get(0).getHeading(), SHADOW_DOM_A);
            assertEquals(shadowRootList.get(1).getHeading(), SHADOW_DOM_B);
        } catch (ShadowRootContextException e) {
            assumeNoException(e);
        }
    }
    
    @Test
    public void testShadowRootMap() {
        ExamplePage page = getInitialPage();
        
        try {
            Map<Object, ShadowRootComponent> shadowRootMap = page.getShadowRootMap();
            assertEquals(shadowRootMap.size(), 2);
            assertEquals(shadowRootMap.get(SHADOW_DOM_A).getHeading(), SHADOW_DOM_A);
            assertEquals(shadowRootMap.get(SHADOW_DOM_B).getHeading(), SHADOW_DOM_B);
        } catch (ShadowRootContextException e) {
            assumeNoException(e);
        }
    }
    
    /**
     * This test verifies that stale elements are automatically refreshed
     * and that the search context chain gets refreshed efficiently.
     */
    @Test
    public void testRefresh() {
        ExamplePage page = getInitialPage();
        // get the table component
        TableComponent component = page.getTable();
        // verify table contents
        verifyTable(component);
        
        // get current refresh counts
        int pageRefreshCount = page.getRefreshCount();
        int tableRefreshCount = component.getRefreshCount();
        int headRefreshCount = component.getHeadRefreshCount();
        int[] bodyRefreshCounts = component.getBodyRefreshCounts();
        
        // verify no initial refresh requests
        assertEquals(pageRefreshCount, 0);
        assertEquals(tableRefreshCount, 0);
        assertEquals(headRefreshCount, 0);
        assertArrayEquals(bodyRefreshCounts, new int[] {0, 0, 0});
        
        // refresh page to force DOM rebuild
        page.getDriver().navigate().refresh();
        // verify table contents
        // NOTE: This necessitates refreshing stale element references
        verifyTable(component);
        
        // get current refresh counts
        pageRefreshCount = page.getRefreshCount();
        tableRefreshCount = component.getRefreshCount();
        headRefreshCount = component.getHeadRefreshCount();
        bodyRefreshCounts = component.getBodyRefreshCounts();
        
        // 1 page refresh request from its table context
        assertEquals(pageRefreshCount, 1);
        // 1 table refresh request from each of its four row contexts
        assertEquals(tableRefreshCount, 4);
        // 1 head row refresh request from one of its web element contexts
        assertEquals(headRefreshCount, 1);
        // 1 refresh request per body row from one of its web element contexts
        assertArrayEquals(bodyRefreshCounts, new int[] {1, 1, 1});
        
        // verify table contents again
        // NOTE: No additional refresh requests are expected
        verifyTable(component);
        
        // get current refresh counts
        pageRefreshCount = page.getRefreshCount();
        tableRefreshCount = component.getRefreshCount();
        headRefreshCount = component.getHeadRefreshCount();
        bodyRefreshCounts = component.getBodyRefreshCounts();
        
        // verify no additional refresh requests
        assertEquals(pageRefreshCount, 1);
        assertEquals(tableRefreshCount, 4);
        assertEquals(headRefreshCount, 1);
        assertArrayEquals(bodyRefreshCounts, new int[] {1, 1, 1});
    }
    
    @Override
    public WebDriver provideDriver(Method method) {
        DriverPlugin plugin = getPlugin();
        String personality = plugin.getPersonalities().get(plugin.getBrowserName());
        
        SeleniumConfig config = SeleniumConfig.getConfig();
        Capabilities[] capabilities = config.getCapabilitiesForJson(personality);
        return GridUtility.getDriver(config.getHubUrl(), capabilities[0]);
    }

    private void launchSeleniumGrid() {
        if (seleniumGrid == null) {
            seleniumGrid = launchGrid();
        }
    }
    
    protected SeleniumGrid launchGrid() {
        return GridLauncher.launch(getPlugin());
    }
    
    public abstract DriverPlugin getPlugin();
    
}
