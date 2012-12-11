/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.richfaces.component.placeholder;

import com.google.common.base.Predicate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.impl.utils.URLUtils;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.arquillian.page.source.SourceChecker;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.integration.utils.AssetBuilder;
import org.richfaces.integration.utils.Attribute;
import org.richfaces.integration.utils.Component;
import org.richfaces.integration.utils.ELAttribute;
import org.richfaces.integration.utils.XMLNS;
import org.richfaces.utils.ColorUtils;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunAsClient
@RunWith(Arquillian.class)
public abstract class AbstractPlaceholderTest {

    protected static final Color DEFAULT_PLACEHOLDER_COLOR = new Color(119, 119, 119);
    protected static final String PLACEHOLDER_TEXT = "Placeholder Text";
    protected static final String PLACEHOLDER_CLASS = "rf-plhdr";
    protected static final String PLACEHOLDER_ID = "placeholderID";
    protected static final String INPUT_ID = "input";
    protected static final String INPUT_SELECTOR = "[id=input]";
    protected static final String SECOND_INPUT_ID = "second-input";
    protected static final String SECOND_INPUT_SELECTOR = "[id=second-input]";
    //
    @Drone
    WebDriver browser;
    //
    @ArquillianResource
    URL contextPath;
    //
    @ArquillianResource
    SourceChecker sourceChecker;
    //
    @FindBy(css = "[id$=ajaxSubmit]")
    WebElement a4jSubmitBtn;
    @FindBy(css = "[id$=httpSubmit]")
    WebElement httpSubmitBtn;
    @FindBy(css = "[id$=output]")
    WebElement output;
    //
    @FindBy(id = PLACEHOLDER_ID)
    WebElement placeholderElement;
    //
    private static final List<Component> inputComponents = new ArrayList<Component>();

    static {
        inputComponents.add(new Component("inputText", new XMLNS("h", "")));
        inputComponents.add(new Component("autocomplete", XMLNS.richInput()));
        inputComponents.add(new Component("calendar",
                new XMLNS("rich", "http://richfaces.org/input")).withAttribute(new Attribute("enableManualInput", "true")));
        inputComponents.add(new Component("inplaceInput", XMLNS.richInput()));
        inputComponents.add(new Component("inplaceSelect", XMLNS.richInput()) //.withAttribute(new Attribute("editEvent", "click"))
                );
        inputComponents.add(new Component("editor", XMLNS.richInput()));
        inputComponents.add(new Component("select", XMLNS.richInput())
                .withAttribute(new Attribute("enableManualInput", "true")));
        inputComponents.add(new Component("inputTextarea", new XMLNS("h", "")));
    }

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(AbstractPlaceholderTest.class);
        addIndexPages(deployment);
        addSelectorPages(deployment);
        addEmptySelectorPages(deployment);
        addRenderedPages(deployment);
        addConverterPages(deployment);
        addSubmitPages(deployment);
        deployment.archive().addClass(PlaceHolderValueConverter.class)
                .addClass(PlaceHolderValue.class);
        return deployment.getFinalArchive();
    }

    abstract Input getFirstInput();

    abstract Input getSecondInput();

    abstract String testedComponent();

    protected Color getDefaultInputColor() {
        return Color.BLACK;
    }

    public void testComponentSourceWithSelector() throws Exception {
        URL selectorUrl = URLUtils.buildUrl(contextPath, "selector-" + testedComponent() + ".jsf?selector=input");
        sourceChecker.checkComponentSource(selectorUrl, "placeholder-with-selector.xmlunit.xml", By.cssSelector("body > span"));
    }

    public void testComponentSourceWithoutSelector() throws Exception {
        URL urL = new URL(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");
        sourceChecker.checkComponentSource(urL, "placeholder-without-selector.xmlunit.xml", By.cssSelector("body > span"));
    }

    public void testConverter() {
        // having
        browser.get(contextPath.toExternalForm() + "converter-" + testedComponent() + ".jsf");
        // then
        assertEquals(PlaceHolderValue.DEFAULT_VALUE, getFirstInput().getDefaultText());
    }

    public void testDefaultAttributes() {
        // having
        browser.get(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, getFirstInput().getTextColor());
        assertTrue("placeholder does not contain default class",
                getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    public void testRendered() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered-" + testedComponent() + ".jsf");
        // then
        assertFalse(Graphene.element(placeholderElement).isVisible().apply(browser));
    }

    /**
     * For inputText and textarea
     */
    public void testSelector() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "selector-" + testedComponent() + ".jsf?selector=" + INPUT_ID);
        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
    }

    public void testSelectorEmpty() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "emptySelector-" + testedComponent() + ".jsf");
        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
    }

    public void testStyleClass() {
        // having
        String className = "some-class";
        browser.navigate().to(contextPath.toExternalForm()
                + "index-" + testedComponent() + ".jsf" + "?styleClass=" + className);
        // then
        assertTrue("input should contain placeholder's default class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertTrue("input should contain specified class", getFirstInput().getStyleClass().contains(className));
    }

    public void when_input_with_placeholder_gains_focus_then_placeholder_is_removed() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().clickOnInput();
        // then
        assertEquals("", getFirstInput().getEditedText());
    }

    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");
        // when
        getFirstInput().sendKeys("a");
        waiting(100L);

        // then
        assertFalse("input should not contain placeholder class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
        assertEquals(getDefaultInputColor(), getFirstInput().getTextColor());
        assertEquals("a", getFirstInput().getEditedText());
    }

    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().sendKeys("a");
        getFirstInput().clearText();
        getSecondInput().clickOnInput();

        // then
        assertEquals(PLACEHOLDER_TEXT, getFirstInput().getDefaultText());
        assertEquals(DEFAULT_PLACEHOLDER_COLOR, getFirstInput().getTextColor());
        assertTrue("input should contain placeholder's default class", getFirstInput().getStyleClass().contains(PLACEHOLDER_CLASS));
    }

    public void when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved() {
        // having
        final String text = "some-text";
        browser.navigate().to(contextPath.toExternalForm() + "index-" + testedComponent() + ".jsf");

        // when
        getFirstInput().sendKeys(text);
        waiting(100L);
        getSecondInput().clickOnInput();

        // then
        assertEquals(text, getFirstInput().getEditedText());
        assertEquals(getDefaultInputColor(), getFirstInput().getTextColor());
    }

    public void testSubmitEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");

        // when
        Graphene.guardHttp(httpSubmitBtn).click();

        // then
        assertEquals("", output.getText());
    }

    public void testSubmitTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");

        // when
        getFirstInput().clickOnInput();
        getFirstInput().sendKeys("xyz");
        Graphene.guardHttp(httpSubmitBtn).click();

        // then
        assertEquals("xyz", output.getText());
    }

    public void testAjaxSendsEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");
        getFirstInput().clickOnInput();
        getFirstInput().sendKeys("xyz");
        Graphene.guardXhr(a4jSubmitBtn).click();
        Graphene.waitAjax().until().element(output).text().equalTo("xyz");

        // when
        getFirstInput().clearText();
        Graphene.guardXhr(a4jSubmitBtn).click();

        // then
        Graphene.waitAjax().until().element(output).text().equalTo("");
    }

    public void testAjaxSendsTextValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit-" + testedComponent() + ".jsf");
        // when
        getFirstInput().clickOnInput();
        getFirstInput().sendKeys("xyz");
        Graphene.guardXhr(a4jSubmitBtn).click();

        // then
        Graphene.waitAjax().until().element(output).text().equalTo("xyz");
    }

    private static void addIndexPages(MiscDeployment deployment) {
        for (Component c : inputComponents) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .addToBody(new Placeholder()
                    .withId(PLACEHOLDER_ID)
                    .withAttribute(new ELAttribute("styleClass"))
                    .withAttribute(new Attribute("value", PLACEHOLDER_TEXT)));
            Component secondInput = c.withId(SECOND_INPUT_ID);
            Component wrapperSpan = new Component("span").addToBody(inputWithPlaceholder, secondInput);
            deployment.archive().addAsWebResource(new AssetBuilder().addComponents(wrapperSpan).build(),
                    "index-" + c.getName() + ".xhtml");
        }
    }

    private static void addSelectorPages(MiscDeployment deployment) {
        Component placeholder = new Placeholder()
                .withId(PLACEHOLDER_ID)
                .withAttribute(new Attribute("value", PLACEHOLDER_TEXT))
                .withAttribute(new ELAttribute("selector"));
        for (Component c : inputComponents) {
            Component input = c.withId(INPUT_ID);
            Component wrapperSpan = new Component("span").addToBody(input, placeholder);

            deployment.archive().addAsWebResource(new AssetBuilder().addComponents(wrapperSpan).build(),
                    "selector-" + c.getName() + ".xhtml");
        }
    }

    private static void addEmptySelectorPages(MiscDeployment deployment) {
        for (Component c : inputComponents) {
            Component inputWithPlaceholder = c.withId(INPUT_ID)
                    .addToBody(new Placeholder()
                    .withId(PLACEHOLDER_ID)
                    .withAttribute(new Attribute("selector", ""))
                    .withAttribute(new Attribute("value", PLACEHOLDER_TEXT)));
            Component secondInput = c.withId(SECOND_INPUT_ID);

            deployment.archive().addAsWebResource(new AssetBuilder().addComponents(inputWithPlaceholder, secondInput).build(),
                    "emptySelector-" + c.getName() + ".xhtml");
        }
    }

    private static void addRenderedPages(MiscDeployment deployment) {
        Component placeholder = new Placeholder()
                .withAttribute(new Attribute("rendered", "false"))
                .withAttribute(new Attribute("value", PLACEHOLDER_TEXT))
                .withAttribute(new Attribute("selector", INPUT_ID));
        for (Component c : inputComponents) {
            Component input = c.withId(INPUT_ID);
            deployment.archive().addAsWebResource(new AssetBuilder().addComponents(input, placeholder).build(),
                    "rendered-" + c.getName() + ".xhtml");
        }
    }

    private static void addConverterPages(MiscDeployment deployment) {
        for (Component c : inputComponents) {
            Component inputWithPlaceholder = c.withId(INPUT_ID).addToBody(
                    new Placeholder()
                    .withId(PLACEHOLDER_ID)
                    .withAttribute(new Attribute("value", "#{placeHolderValue}"))
                    .withAttribute(new Attribute("converter", "placeHolderValueConverter")));
            Component secondInput = c.withId(SECOND_INPUT_ID);
            deployment.archive().addAsWebResource(new AssetBuilder().addComponents(inputWithPlaceholder, secondInput).build(),
                    "converter-" + c.getName() + ".xhtml");
        }
    }

    private static void addSubmitPages(MiscDeployment deployment) {
        Component ajaxSubmitBtn = new Component("commandButton", XMLNS.a4j())
                .withId("ajaxSubmit").withValue("ajax submit")
                .withAttribute(new Attribute("execute", "@form"))
                .withAttribute(new Attribute("render", "output"));
        Component httpSubmitBtn = new Component("commandButton", XMLNS.jsfHtml()).withId("httpSubmit").withValue("http submit");
        Component br = new Component("br");
        Component output = new Component("outputPanel", XMLNS.a4j()).withId("outputPanel")
                .addToBody(new Component("outputText", XMLNS.jsfHtml()).withId("output").withValue("#{placeHolderValue.value2}"));
        for (Component c : inputComponents) {
            Component inputWithPlaceholder = c.withId(INPUT_ID).withValue("#{placeHolderValue.value2}").addToBody(
                    new Placeholder()
                    .withId(PLACEHOLDER_ID)
                    .withValue(PLACEHOLDER_TEXT));
            deployment.archive().addAsWebResource(new AssetBuilder()
                    .addComponentsToForm(inputWithPlaceholder, br, ajaxSubmitBtn, httpSubmitBtn, br, output).build(),
                    "submit-" + c.getName() + ".xhtml");
        }
    }

    public static class Input {

        @Root
        protected WebElement input;

        public String getEditedText() {
            return input.getAttribute("value");
        }

        public String getDefaultText() {
            return getEditedText();
        }

        public void clickOnInput() {
            input.click();
        }

        public void clearText() {
            input.clear();
        }

        public void sendKeys(String keysToSend) {
            input.sendKeys(keysToSend);
        }

        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(input.getCssValue("color"));
        }

        public String getStyleClass() {
            return input.getAttribute("class");
        }
    }

    public static class InplaceInput extends Input {

        @FindBy(css = "input[id$=Input]")
        WebElement inplaceInput;
        @FindBy(css = "span[id$=Label]")
        WebElement inplaceLabel;

        @Override
        public String getEditedText() {
            return inplaceInput.getAttribute("value");
        }

        @Override
        public String getDefaultText() {
            return inplaceLabel.getText();
        }

        @Override
        public void sendKeys(String keysToSend) {
            inplaceInput.click();
            inplaceInput.sendKeys(keysToSend);
        }

        @Override
        public void clearText() {
            inplaceInput.click();
            inplaceInput.clear();
        }

        @Override
        public Color getTextColor() {
            return ColorUtils.convertToAWTColor(inplaceLabel.getCssValue("color"));
        }

        @Override
        public String getStyleClass() {
            return inplaceLabel.getAttribute("class");
        }
    }

    private static class Placeholder extends Component {

        public Placeholder() {
            super("placeholder", XMLNS.richMisc());
        }
    }

    protected void waiting(Long time) {
        try {
            if (time == null) {
                Thread.sleep(20000);
            } else {
                Thread.sleep(time);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(AbstractPlaceholderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
