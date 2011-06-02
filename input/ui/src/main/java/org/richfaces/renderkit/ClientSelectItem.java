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
package org.richfaces.renderkit;

import java.io.IOException;

import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptUtils;

public final class ClientSelectItem implements ScriptString {
    private String clientId;
    private String label;
    private String convertedValue;

    public ClientSelectItem(String convertedValue, String label) {
        this(convertedValue, label, null);
    }

    public ClientSelectItem(String convertedValue, String label, String clientId) {
        super();
        this.convertedValue = convertedValue;
        this.label = label;
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLabel() {
        return label;
    }

    public String getConvertedValue() {
        return convertedValue;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(this.toScript());
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    public String toScript() {
        return "{ 'id' : " + ScriptUtils.toScript(clientId) + " , 'label' : " + ScriptUtils.toScript(label) + ", 'value' : "
            + ScriptUtils.toScript(convertedValue) + "}";
    }
}
