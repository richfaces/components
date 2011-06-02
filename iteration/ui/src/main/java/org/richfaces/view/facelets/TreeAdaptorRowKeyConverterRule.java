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
package org.richfaces.view.facelets;

import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.TreeModelAdaptor;

/**
 */
public class TreeAdaptorRowKeyConverterRule extends MetaRule {
    public static final TreeAdaptorRowKeyConverterRule INSTANCE = new TreeAdaptorRowKeyConverterRule();

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
        if (meta.isTargetInstanceOf(TreeModelAdaptor.class)) {
            if ("rowKeyConverter".equals(name)) {
                if (attribute.isLiteral()) {
                    return new StaticConverterMetadata(attribute.getValue());
                } else {
                    return new DynamicConverterMetaData(attribute);
                }
            }
        }

        return null;
    }

    static final class DynamicConverterMetaData extends Metadata {
        private final TagAttribute attribute;

        public DynamicConverterMetaData(TagAttribute attribute) {
            super();
            this.attribute = attribute;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIComponent) instance).setValueExpression("rowKeyConverter", attribute.getValueExpression(ctx, Converter.class));
        }
    }

    static final class StaticConverterMetadata extends Metadata {
        private final String converterId;

        public StaticConverterMetadata(String converterId) {
            super();
            this.converterId = converterId;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            Converter converter = ctx.getFacesContext().getApplication().createConverter(converterId);

            ((TreeModelAdaptor) instance).setRowKeyConverter(converter);
        }
    }
}
