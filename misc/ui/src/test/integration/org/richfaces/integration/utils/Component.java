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
package org.richfaces.integration.utils;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;

/**
 * Helper class for creating component and its String representation.
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Component {

    private final String name;
    private ComponentsList componentsInBody = new ComponentsList();
    private Set<Attribute> attributes = Sets.<Attribute>newLinkedHashSet();
    private XMLNS namespace;

    private Component(Component component) {
        this.name = component.name;
        this.componentsInBody.addAll(component.componentsInBody);
        this.namespace = component.namespace;
        this.attributes.addAll(component.attributes);
    }

    public Component(String name) {
        this.name = name;
    }

    public Component(String name, XMLNS namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    protected boolean addAtribute(Attribute a) {
        return attributes.add(a);
    }

    public String getName() {
        return name;
    }

    public Component addToBody(Component... components) {
        Component result = new Component(this);
        result.componentsInBody.addAll(Arrays.asList(components));
        return result;
    }

    public Component addToBody(Component component) {
        Component result = new Component(this);
        result.componentsInBody.add(component);
        return result;
    }

    public Set<XMLNS> getAllNamespaces() {
        Set<XMLNS> result = Sets.<XMLNS>newLinkedHashSet();
        if (namespace != null) {
            result.add(namespace);
        }
        for (Component c : componentsInBody) {
            result.addAll(c.getAllNamespaces());
        }
        result.remove(XMLNS.jsfCore());
        result.remove(XMLNS.jsfHtml());
        return result;
    }

    public Component usingNameSpace(XMLNS namespace) {
        Component component = new Component(this);
        component.namespace = namespace;
        return component;
    }

    public Component withAttribute(Attribute a) {
        Component component = new Component(this);
        component.attributes.remove(a);
        component.addAtribute(a);
        return component;
    }

    public Component withId(String id) {
        Component component = new Component(this);
        component.addAtribute(new Attribute("id", id));
        return component;
    }

    public Component withValue(String value) {
        Component component = new Component(this);
        component.addAtribute(new Attribute("value", value));
        return component;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        if (namespace != null && !namespace.name.isEmpty()) {
            sb.append(namespace.name).append(":");
        }
        sb.append(name).append(" ");
        for (Attribute attribute : attributes) {
            sb.append(attribute);
        }
        if (componentsInBody == null || componentsInBody.isEmpty()) {
            sb.append("/>");
        } else {//has body
            sb.append(">").append("\n").append(componentsInBody).append("\n</");
            if (namespace != null && !namespace.name.isEmpty()) {
                sb.append(namespace.name).append(":");
            }
            sb.append(name).append(">");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributes, componentsInBody, name, namespace);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Component other = (Component) obj;
        return Objects.equal(this.attributes, other.attributes)
                && Objects.equal(this.componentsInBody, other.componentsInBody)
                && Objects.equal(this.name, other.name)
                && Objects.equal(this.namespace, other.namespace);
    }
}
