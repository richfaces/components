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

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(type = UITreeModelRecursiveAdaptor.COMPONENT_TYPE, family = UITreeModelRecursiveAdaptor.COMPONENT_FAMILY, tag = @Tag(name = "treeModelRecursiveAdaptor", handler = "org.richfaces.view.facelets.TreeModelRecursiveAdaptorHandler"))
public class UITreeModelRecursiveAdaptor extends AbstractTreeModelAdaptor implements TreeModelRecursiveAdaptor {
    public static final String COMPONENT_TYPE = "org.richfaces.TreeModelRecursiveAdaptor";
    public static final String COMPONENT_FAMILY = "org.richfaces.TreeModelRecursiveAdaptor";

    private enum PropertyKeys {
        roots,
        nodes,
        leaf
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute
    public Object getRoots() {
        Object roots = getStateHelper().eval(PropertyKeys.roots);

        memoizeDefaultRowKeyConverter(roots);

        return roots;
    }

    public void setRoots(Object roots) {
        getStateHelper().put(PropertyKeys.roots, roots);
    }

    @Attribute
    public Object getNodes() {
        return getStateHelper().eval(PropertyKeys.nodes);
    }

    public void setNodes(Object nodes) {
        getStateHelper().put(PropertyKeys.nodes, nodes);
    }

    public String getRecursionOrder() {
        return null;
    }

    @Attribute
    public boolean isLeaf() {
        return (Boolean) getStateHelper().eval(PropertyKeys.leaf, false);
    }

    public void setLeaf(boolean leaf) {
        getStateHelper().put(PropertyKeys.leaf, leaf);
    }
}
