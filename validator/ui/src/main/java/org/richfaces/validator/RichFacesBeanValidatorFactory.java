/**
 *
 */
package org.richfaces.validator;

import java.lang.annotation.Annotation;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.ConstraintDescriptor;

/**
 * @author asmirnov
 *
 */
public class RichFacesBeanValidatorFactory implements BeanValidatorFactory {
    private ValidatorFactory validatorFactory;
    private ValidatorContext validatorContext;

    public RichFacesBeanValidatorFactory() {
        // Enforce class to load
        ValidatorFactory.class.getName();
    }

    public void init() throws InitializationException {
        // Check Factory, to avoid instantiation errors
        // https://jira.jboss.org/jira/browse/RF-7226
        try {
            validatorFactory = Validation.buildDefaultValidatorFactory();
            validatorContext = validatorFactory.usingContext();
            MessageInterpolator jsfMessageInterpolator = new JsfMessageInterpolator(validatorFactory.getMessageInterpolator());
            validatorContext.messageInterpolator(jsfMessageInterpolator);
        } catch (ValidationException e) {
            throw new InitializationException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.BeanValidatorFactory#getValidator(javax.faces.context.FacesContext)
     */
    public Validator getValidator(FacesContext context) {
        return validatorContext.getValidator();
    }

    public FacesMessage interpolateMessage(FacesContext context, final ConstraintDescriptor<? extends Annotation> constrain) {
        if (constrain.getAttributes().containsKey("message")) {
            Object object = constrain.getAttributes().get("message");
            String interpolatedMessage;
            interpolatedMessage = validatorFactory.getMessageInterpolator().interpolate(object.toString(), new Context() {
                public Object getValidatedValue() {
                    return "{0}";
                }

                public ConstraintDescriptor<?> getConstraintDescriptor() {
                    return constrain;
                }
            }, MessageFactory.getCurrentLocale(context));
            return new FacesMessage(interpolatedMessage);
        } else {
            return MessageFactory.createMessage(context, UIInput.UPDATE_MESSAGE_ID);
        }
    }

    private static final class JsfMessageInterpolator implements MessageInterpolator {
        private MessageInterpolator delegate;

        public JsfMessageInterpolator(MessageInterpolator delegate) {
            this.delegate = delegate;
        }

        public String interpolate(String messageTemplate, Context context) {

            Locale locale = MessageFactory.getCurrentLocale(FacesContext.getCurrentInstance());
            if (null != locale) {
                return delegate.interpolate(messageTemplate, context, locale);
            } else {
                return delegate.interpolate(messageTemplate, context);
            }
        }

        public String interpolate(String messageTemplate, Context context, Locale locale) {
            Locale faceslocale = MessageFactory.getCurrentLocale(FacesContext.getCurrentInstance());
            if (null != faceslocale) {
                return delegate.interpolate(messageTemplate, context, faceslocale);
            } else {
                return delegate.interpolate(messageTemplate, context, locale);
            }
        }
    }
}
