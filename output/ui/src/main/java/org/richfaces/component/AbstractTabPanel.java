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
package org.richfaces.component;

import org.richfaces.HeaderAlignment;
import org.richfaces.HeaderPosition;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p>The &lt;rich:tabPanel&gt; component provides a set of tabbed panels for displaying one panel of content at a time.
 * The tabs can be highly customized and themed. Each tab within a &lt;rich:tabPanel&gt; container is a &lt;rich:tab&gt;
 * component.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.html.TogglePanelTagHandler"), renderer = @JsfRenderer(type = "org.richfaces.TabPanelRenderer"), attributes = {
        "core-props.xml", "events-mouse-props.xml", "i18n-props.xml" })
public abstract class AbstractTabPanel extends NamedTogglePanel {
    public static final String COMPONENT_TYPE = "org.richfaces.TabPanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.TabPanel";

    protected AbstractTabPanel() {
        setRendererType("org.richfaces.TabPanelRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Holds the active tab name. This name is a reference to the name identifier of the active child &lt;rich:tab&gt;
     * component.
     */
    @Override
    @Attribute(generate = false)
    public String getActiveItem() {
        return super.getActiveItem();
    }

    /**
     * The position of the header: top, bottom, left, right
     */
    @Attribute
    public abstract HeaderPosition getHeaderPosition();

    /**
     * The alignment of the tab panel header: left, center, right, bottom, top
     */
    @Attribute
    public abstract HeaderAlignment getHeaderAlignment();

    /**
     * Space-separated list of CSS style class(es) for active tab header.
     */
    @Attribute
    public abstract String getTabActiveHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for disabled tab headers.
     */
    @Attribute
    public abstract String getTabDisabledHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for inactive tab headers.
     */
    @Attribute
    public abstract String getTabInactiveHeaderClass();

    /**
     * Space-separated list of CSS style class(es) for tab content
     */
    @Attribute
    public abstract String getTabContentClass();

    /**
     * Space-separated list of CSS style class(es) for tab headers.
     */
    @Attribute
    public abstract String getTabHeaderClass();

    @Attribute(hidden = true)
    public abstract boolean isLimitRender();

    @Attribute(hidden = true)
    public abstract Object getData();

    @Attribute(hidden = true)
    public abstract String getStatus();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(hidden = true)
    public abstract Object getRender();

    public boolean isHeaderPositionedTop() {
        return (null == this.getHeaderPosition()) || (this.getHeaderPosition().equals(HeaderPosition.top));
    }

    public boolean isHeaderAlignedLeft() {
        return (null == this.getHeaderAlignment()) || (this.getHeaderAlignment().equals(HeaderAlignment.left));
    }
}
