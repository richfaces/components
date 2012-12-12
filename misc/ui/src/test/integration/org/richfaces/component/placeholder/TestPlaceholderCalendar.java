/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.component.placeholder;

import java.awt.Color;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceholderCalendar extends AbstractPlaceholderTest {

    @FindBy(css = INPUT_SELECTOR + " > span > input")
    private Input firstInput;
    @FindBy(css = SECOND_INPUT_SELECTOR + " > span > input")
    private Input secondInput;

    @Override
    String testedComponent() {
        return "calendar";
    }

    @Override
    protected String getTestedValueResponse() {
        return "Wed Dec 12 00:00:00 CET 2012";
    }

    @Override
    protected String getTestedValue() {
        return "Dec 12, 2012";
    }

    @Override
    protected Color getDefaultInputColor() {
        return new Color(26, 26, 26);
    }

    @Override
    Input getFirstInput() {
        return firstInput;
    }

    public Input getSecondInput() {
        return secondInput;
    }

    @Test
    public void testConverter() {
        super.testConverter();
    }

    @Test
    public void testDefaultAttributes() {
        super.testDefaultAttributes();
    }

    @Test
    public void testRendered() {
        super.testRendered();
    }

    @Test
    public void testSelector() {
        super.testSelector();
    }

    @Test
    public void testSelectorEmpty() {
        super.testSelectorEmpty();
    }

    @Test
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    public void when_input_with_placeholder_gains_focus_then_placeholder_is_removed() {
        super.when_input_with_placeholder_gains_focus_then_placeholder_is_removed();
    }

    @Test
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        super.when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes();
    }

    @Test
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        super.when_text_is_cleared_then_input_gets_placeholder_text_and_style_again();
    }

    @Test
    public void when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved() {
        super.when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved();
    }

    @Test
    public void testAjaxSendsEmptyValue() {
        super.testAjaxSendsEmptyValue();
    }

    @Test
    public void testAjaxSendsTextValue() {
        super.testAjaxSendsTextValue();
    }

    @Test
    public void testSubmitEmptyValue() {
        super.testSubmitEmptyValue();
    }

    @Ignore("calendar date conversion problem")
    @Test
    public void testSubmitTextValue() {
        super.testSubmitTextValue();
    }
}
