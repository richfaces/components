/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.AutocompleteHandler"), renderer = @JsfRenderer(type = "org.richfaces.AutocompleteRenderer"))
public abstract class AbstractAutocomplete extends UIInput implements MetaComponentResolver, MetaComponentEncoder {
    public static final String ITEMS_META_COMPONENT_ID = "items";
    public static final String COMPONENT_TYPE = "org.richfaces.Autocomplete";
    public static final String COMPONENT_FAMILY = UIInput.COMPONENT_FAMILY;

    // TODO nick - change to Object - https://jira.jboss.org/browse/RF-8897
    @Attribute()
    public abstract Object getAutocompleteList();

    @Attribute(signature = @Signature(returnType = Object.class, parameters = { FacesContext.class, UIComponent.class,
            String.class }))
    public abstract MethodExpression getAutocompleteMethod();

    public abstract void setAutocompleteMethod(MethodExpression expression);

    @Attribute(literal = true)
    public abstract String getVar();

    // TODO nick - el-only?
    @Attribute(literal = false)
    public abstract Object getFetchValue();

    @Attribute
    public abstract int getMinChars();

    @Attribute
    public abstract String getFilterFunction();

    @Attribute(defaultValue = "rf-au-itm-sel")
    public abstract String getSelectedItemClass();

    @Attribute()
    public abstract String getPopupClass();

    @Attribute()
    public abstract String getInputClass();

    @Attribute
    public abstract AutocompleteMode getMode();

    @Attribute
    public abstract String getLayout();

    @Attribute
    public abstract String getTokens();

    @Attribute(defaultValue = "true")
    public abstract boolean isAutofill();

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract boolean isShowButton();

    @Attribute(defaultValue = "true")
    public abstract boolean isSelectFirst();

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("keypress"))
    public abstract String getOnkeypress();

    @Attribute(events = @EventName("keydown"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName("keyup"))
    public abstract String getOnkeyup();

    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();

    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();

    @Attribute(events = @EventName("selectitem"))
    public abstract String getOnselectitem();

    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();

    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();

    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();

    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();

    @Attribute
    public abstract String getClientFilterFunction();

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }

        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {

        return null;
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                    ITEMS_META_COMPONENT_ID);
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
}
