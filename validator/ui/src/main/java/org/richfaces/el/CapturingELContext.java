/**
 *
 */
package org.richfaces.el;

import java.beans.FeatureDescriptor;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.context.FacesContext;
import javax.faces.el.CompositeComponentExpressionHolder;

import org.richfaces.validator.GraphValidatorState;

/**
 * This class wraps original ELContext and capture whole call stack to the target object so it could be used to extract semantic
 * information like annotations or Jena Model properties.
 *
 * @author asmirnov
 *
 */
public class CapturingELContext extends ELContext {
    private final ELContext parent;
    private ValueReference reference = null;
    private final InterceptingResolver resolver;

    public CapturingELContext(ELContext parent) {
        this.parent = parent;
        resolver = new InterceptingResolver(parent.getELResolver());
    }

    public ValueReference getReference() {
        return reference;
    }

    private boolean isContainerObject(Object base) {
        return base instanceof Collection || base instanceof Map || base.getClass().isArray();
    }

    public boolean hasReferenceExpression() {
        return reference != null && reference.getBase() instanceof CompositeComponentExpressionHolder;
    }

    public ValueExpression getReferenceExpression() {
        CompositeComponentExpressionHolder expressionHolder = (CompositeComponentExpressionHolder) reference.getBase();
        return expressionHolder.getExpression(reference.getProperty().toString());
    }

    public ValueDescriptor getDescriptor() {
        ValueReference localReference = reference;

        while (true) {
            if (localReference == null || localReference.getBase() == null || localReference.getProperty() == null) {
                return null;
            }

            Object base = localReference.getBase();

            if (isContainerObject(base) && localReference.hasNext()) {
                localReference = localReference.next();
            } else {
                return new ValueDescriptor(base.getClass(), localReference.getProperty().toString());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ELContext#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getContext(Class key) {
        return parent.getContext(key);
    }

    @Override
    public FunctionMapper getFunctionMapper() {
        return parent.getFunctionMapper();
    }

    @Override
    public Locale getLocale() {
        return parent.getLocale();
    }

    @Override
    public VariableMapper getVariableMapper() {
        return parent.getVariableMapper();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void putContext(Class key, Object contextObject) {
        parent.putContext(key, contextObject);
    }

    @Override
    public void setLocale(Locale locale) {
        parent.setLocale(locale);
    }

    /**
     * This resolver records all intermediate objects from the EL-expression that can be used to detect Semantic Beans
     * annotations or Jena Model properties.
     *
     * @author asmirnov
     *
     */
    private final class InterceptingResolver extends ELResolver {
        private ELResolver delegate;
        private boolean clonedObject;

        public InterceptingResolver(ELResolver delegate) {
            this.delegate = delegate;
        }

        // Capture the base and property rather than write the value
        @Override
        public void setValue(ELContext context, Object base, Object property, Object value) {
            if (base != null) {
                // TODO - detect value object from inderect references ( e.g. data table variables ).
                if (this.clonedObject) {
                    delegate.setValue(context, base, property, value);
                }

                context.setPropertyResolved(true);
                reference = new ValueReference(base, property, reference);
            }
        }

        // The rest of the methods simply delegate to the existing context

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            reference = new ValueReference(base, property, reference);
            Object value = delegate.getValue(context, base, property);
            if (null != value && context.isPropertyResolved()) {
                FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
                Object clone = GraphValidatorState.getActiveClone(facesContext, value);
                if (null != clone) {
                    this.clonedObject = true;
                    return clone;
                }
            }
            return value;
        }

        @Override
        public Class<?> getType(ELContext context, Object base, Object property) {
            if (base != null) {
                context.setPropertyResolved(true);
                reference = new ValueReference(base, property, reference);
            }
            return delegate.getType(context, base, property);
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base, Object property) {
            return delegate.isReadOnly(context, base, property);
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
            return delegate.getFeatureDescriptors(context, base);
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object base) {
            return delegate.getCommonPropertyType(context, base);
        }
    }
}
