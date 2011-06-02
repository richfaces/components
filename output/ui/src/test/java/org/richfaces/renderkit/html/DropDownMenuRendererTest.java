/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.Test;
import org.richfaces.component.DropDownMenuBean;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DropDownMenuRendererTest extends RendererTestBase {
    @Override
    public void setUp() throws URISyntaxException {
        environment = new HtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/component/faces-config.xml");
        environment.start();
    }

    @Test
    public void testDoEncodeServerMode() throws IOException, SAXException {
        doTest("dropDownMenu_serverMode", "form:ddmenu");
    }

    @Test
    public void testDoEncodeAjaxMode() throws IOException, SAXException {
        doTest("dropDownMenu_ajaxMode", "form:ddmenu");
    }

    @Test
    public void testServerClick() throws IOException, SAXException {
        HtmlPage page = environment.getPage("/dropDownMenu_serverMode.jsf");
        HtmlDivision item = (HtmlDivision) page.getElementById("form:saveAll");
        assertNotNull(item);
        DropDownMenuBean.setCurrent("none");
        item.click();

        item = (HtmlDivision) page.getElementById("form:saveAll");
        assertNotNull(item);
        assertEquals("action", DropDownMenuBean.getCurrent());
    }

    @Test
    public void testAjaxClick() throws IOException, SAXException {
        HtmlPage page = environment.getPage("/dropDownMenu_ajaxMode.jsf");
        HtmlDivision item = (HtmlDivision) page.getElementById("form:saveAll");
        assertNotNull(item);
        DropDownMenuBean.setCurrent("none");
        item.click();

        item = (HtmlDivision) page.getElementById("form:saveAll");
        assertNotNull(item);
        assertEquals("action", DropDownMenuBean.getCurrent());
    }
}
