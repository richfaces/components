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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.TooltipLayout;
import org.richfaces.TooltipMode;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * @author amarkhel
 * @since 2010-10-24
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.TooltipRenderer"), attributes = {
        "tooltip-props.xml", "ajax-props.xml" })
public abstract class AbstractTooltip extends UIOutput implements AbstractDivPanel, MetaComponentResolver, MetaComponentEncoder {
    public static final String COMPONENT_TYPE = "org.richfaces.Tooltip";
    public static final String COMPONENT_FAMILY = "org.richfaces.Tooltip";
    public static final String CONTENT_META_COMPONENT_ID = "content";

    protected AbstractTooltip() {
        setRendererType("org.richfaces.TooltipRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // ------------------------------------------------ Component Attributes
    enum Properties {
        target
    }

    @Attribute(generate = false)
    public String getTarget() {
        UIComponent parent2 = getParent();
        String id2 = parent2.getId();
        if (id2 == null) {
            parent2.getClientId();
            id2 = parent2.getId();
        }
        return (String) getStateHelper().eval(Properties.target, id2);
    }

    public void setTarget(String target) {
        getStateHelper().put(Properties.target, target);
    }

    /*
     * @Attribute public abstract String getValue();
     */

    @Attribute(defaultValue = "TooltipLayout.DEFAULT")
    public abstract TooltipLayout getLayout();

    @Attribute(defaultValue = "true")
    public abstract boolean isAttached();

    @Attribute(defaultValue = "Positioning.DEFAULT")
    public abstract Positioning getJointPoint();

    @Attribute(defaultValue = "Positioning.DEFAULT")
    public abstract Positioning getDirection();

    @Attribute(defaultValue = "true")
    public abstract boolean isFollowMouse();

    @Attribute(defaultValue = "0")
    public abstract int getHideDelay();

    @Attribute(defaultValue = "mouseleave")
    public abstract String getHideEvent();

    @Attribute(defaultValue = "10")
    public abstract int getHorizontalOffset();

    @Attribute(defaultValue = "TooltipMode.DEFAULT")
    public abstract TooltipMode getMode();

    @Attribute(defaultValue = "0")
    public abstract int getShowDelay();

    @Attribute(defaultValue = "mouseenter")
    public abstract String getShowEvent();

    @Attribute(defaultValue = "10")
    public abstract int getVerticalOffset();

    // ------------------------------------------------ Html Attributes

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract int getZindex();

    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();

    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();

    @Attribute(events = @EventName("beforehide"))
    public abstract String getOnbeforehide();

    @Attribute(events = @EventName("beforeshow"))
    public abstract String getOnbeforeshow();

    // ------------------------------------------------ Html Attributes End

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                    CONTENT_META_COMPONENT_ID);
                if (result == VisitResult.COMPLETE) {
                    return true;
                } else if (result == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitTree(context, callback);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    public String getContentClientId(FacesContext context) {
        return getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + CONTENT_META_COMPONENT_ID;
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (CONTENT_META_COMPONENT_ID.equals(metaComponentId)) {
            return ((AbstractTooltip) contextComponent).getContentClientId(facesContext);
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }
}
