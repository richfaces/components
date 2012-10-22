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

import java.util.concurrent.atomic.AtomicReference;

import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * Base for table panels which have named items (subclasses of {@link AbstractTogglePanelTitledItem}s).
 *
 * @author lfryc
 */
public abstract class NamedTogglePanel extends AbstractTogglePanel {

    /**
     * Returns name of the active panel item.
     *
     * If active panel item is disabled, it returns first non-disabled item in the list of panel's items.
     */
    @Override
    public String getActiveItem() {
        String activeItemName = super.getActiveItem();

        if (activeItemName == null) {
            return getFirstNonDisabledItemName();
        } else {
            AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) getItem(activeItemName);
            if (item != null && item.isDisabled()) {
                return getFirstNonDisabledItemName();
            }
        }

        return activeItemName;
    }

    /**
     * Returns name of first non-disabled item in the list of panel's items.
     */
    private String getFirstNonDisabledItemName() {
        AbstractTogglePanelTitledItem firstNonDisabledItem = getFirstNonDisabledItem();
        if (firstNonDisabledItem == null) {
            return null;
        }

        return firstNonDisabledItem.getName();
    }

    /**
     * Returns first non-disabled item in the list of panel's items.
     */
    public AbstractTogglePanelTitledItem getFirstNonDisabledItem() {
        final AtomicReference<AbstractTogglePanelTitledItem> result = new AtomicReference<AbstractTogglePanelTitledItem>(null);

        visitTogglePanelItems(this, new TogglePanelVisitCallback() {
            @Override
            public VisitResult visit(FacesContext facesContext, TogglePanelVisitState visitState) {
                AbstractTogglePanelTitledItem item = (AbstractTogglePanelTitledItem) visitState.getItem();
                if (!item.isDisabled()) {
                    result.set(item);
                    return VisitResult.COMPLETE;
                } else {
                    return VisitResult.ACCEPT;
                }
            }
        });

        return result.get();
    }
}
