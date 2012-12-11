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
    Input getFirstInput() {
        return firstInput;
    }

    public Input getSecondInput() {
        return secondInput;
    }

    @Test
    @Override
    public void testConverter() {
        super.testConverter();
    }

    @Test
    @Override
    public void testDefaultAttributes() {
        super.testDefaultAttributes();
    }

    @Test
    @Override
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Override
    public void testSelector() {
        super.testSelector();
    }

    @Test
    @Override
    public void testSelectorEmpty() {
        super.testSelectorEmpty();
    }

    @Test
    @Override
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    @Override
    public void when_input_with_placeholder_gains_focus_then_placeholder_is_removed() {
        super.when_input_with_placeholder_gains_focus_then_placeholder_is_removed();
    }

    @Test
    @Override
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
        super.when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes();
    }

    @Test
    @Override
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
        super.when_text_is_cleared_then_input_gets_placeholder_text_and_style_again();
    }

    @Test
    @Override
    public void when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved() {
        super.when_text_is_changed_and_input_is_blurred_then_typed_text_is_preserved();
    }

    @Test
    @Override
    public void testAjaxSendsEmptyValue() {
        super.testAjaxSendsEmptyValue();
    }

    @Test
    @Override
    public void testAjaxSendsTextValue() {
        super.testAjaxSendsTextValue();
    }

    @Test
    @Override
    public void testSubmitEmptyValue() {
        super.testSubmitEmptyValue();
    }

    @Test
    @Override
    public void testSubmitTextValue() {
        super.testSubmitTextValue();
    }
}
