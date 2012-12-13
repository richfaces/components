package org.richfaces.component.extendedDataTable;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.IterationDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;

@RunAsClient
@RunWith(Arquillian.class)
public class TestCellWidthWithMaxTableWidth {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "myForm:edt")
    private WebElement edt;

    @FindBy(id = "myForm:edt:0:n")
    private WebElement firstRow;

    @Deployment
    public static WebArchive createDeployment() {
        IterationDeployment deployment = new IterationDeployment(TestCellWidthWithMaxTableWidth.class);
        deployment.archive().addClass(IterationBean.class);
        addIndexPage(deployment);
        deployment.archive().addAsWebResource("css/table_width_max.css", "resources/css/table_width_max.css");

        return deployment.getFinalArchive();
    }

    @Test
    public void setting_column_width() {
        browser.get(contextPath.toExternalForm());
        Assert.assertEquals("101px", firstRow.findElement(By.cssSelector("td")).getCssValue("width"));
    }

    private static void addIndexPage(IterationDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.xmlns("rich", "http://richfaces.org/iteration");
        p.xmlns("a4j", "http://richfaces.org/a4j");

        p.body("<h:outputStylesheet library='css' name='table_width_max.css' />");
        p.body("<h:form id='myForm'>");
        p.body("    <rich:extendedDataTable id='edt' value='#{iterationBean.values}' var='bean'>");
        p.body("        <rich:column id='column1'>");
        p.body("            <h:outputText value='Bean:' />");
        p.body("        </rich:column>");
        p.body("        <rich:column id='column2'>");
        p.body("            <h:outputText value='#{bean}' />");
        p.body("        </rich:column>");
        p.body("    </rich:extendedDataTable>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
