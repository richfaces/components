/**
 *
 */
package org.richfaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.RegexValidator;
import javax.faces.validator.RequiredValidator;
import javax.faces.validator.Validator;

/**
 * @author asmirnov
 *
 */
public class FacesValidatorServiceImpl extends FacesServiceBase<Validator> implements FacesValidatorService {
    private static final String PATTERN = "pattern";
    private static final String MINIMUM = "min";
    private static final String MAXIMUM = "max";

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.FacesValidatorService#getValidatorDescription(javax.faces.context.FacesContext,
     * javax.faces.validator.Validator)
     */
    public ValidatorDescriptor getValidatorDescription(FacesContext context, EditableValueHolder input, Validator validator,
        String validatorMessage) {
        FacesMessage message = getMessage(context, validator, input, validatorMessage);
        FacesValidatorDescriptor descriptor = new FacesValidatorDescriptor(validator.getClass(), message);
        setLabelParameter(input, descriptor);
        fillParameters(descriptor, validator);
        descriptor.makeImmutable();
        return descriptor;
    }

    @Override
    protected String getMessageId(Validator component) {
        // TODO: all messages should be passed to client side using js function RichFaces.csv.addMessage
        String messageId;
        if (component instanceof DoubleRangeValidator) {
            DoubleRangeValidator validator = (DoubleRangeValidator) component;
            if (validator.getMaximum() < Double.MAX_VALUE) {
                if (validator.getMinimum() > Double.MIN_VALUE) {
                    messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
                } else {
                    messageId = DoubleRangeValidator.MAXIMUM_MESSAGE_ID;
                }
            } else if (validator.getMinimum() > Double.MIN_VALUE) {
                messageId = DoubleRangeValidator.MINIMUM_MESSAGE_ID;
            } else {
                messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;// What to use for that case ( no min/max set,
                                                                         // validator always pass ).
            }
        } else if (component instanceof LengthValidator) {
            LengthValidator validator = (LengthValidator) component;
            if (validator.getMaximum() > 0) {
                if (validator.getMinimum() > 0) {
                    messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
                } else {
                    messageId = LengthValidator.MAXIMUM_MESSAGE_ID;
                }
            } else if (validator.getMinimum() > 0) {
                messageId = LengthValidator.MINIMUM_MESSAGE_ID;
            } else {
                messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;// What to use for that case ( no min/max set,
                                                                         // validator always pass ).
            }
        } else if (component instanceof LongRangeValidator) {
            LongRangeValidator validator = (LongRangeValidator) component;
            if (validator.getMaximum() != 0) {
                if (validator.getMinimum() != 0) {
                    messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;
                } else {
                    messageId = LongRangeValidator.MAXIMUM_MESSAGE_ID;
                }
            } else if (validator.getMinimum() != 0) {
                messageId = LongRangeValidator.MINIMUM_MESSAGE_ID;
            } else {
                messageId = DoubleRangeValidator.NOT_IN_RANGE_MESSAGE_ID;// What to use for that case ( no min/max set,
                                                                         // validator always pass ).
            }
        } else if (component instanceof RegexValidator) {
            messageId = RegexValidator.NOT_MATCHED_MESSAGE_ID;
        } else if (component instanceof RequiredValidator) {
            messageId = UIInput.REQUIRED_MESSAGE_ID;
        } else {
            messageId = UIInput.UPDATE_MESSAGE_ID;
        }
        return messageId;
    }

    @Override
    protected void fillParameters(BaseFacesObjectDescriptor<Validator> descriptor, Validator component) {
        if (component instanceof DoubleRangeValidator) {
            DoubleRangeValidator validator = (DoubleRangeValidator) component;
            if (validator.getMaximum() < Double.MAX_VALUE) {
                descriptor.addParameter(MAXIMUM, validator.getMaximum());
            }
            if (validator.getMinimum() > Double.MIN_VALUE) {
                descriptor.addParameter(MINIMUM, validator.getMinimum());
            }
        } else if (component instanceof LengthValidator) {
            LengthValidator validator = (LengthValidator) component;
            if (validator.getMaximum() > 0) {
                descriptor.addParameter(MAXIMUM, validator.getMaximum());
            }
            if (validator.getMinimum() > 0) {
                descriptor.addParameter(MINIMUM, validator.getMinimum());
            }
        } else if (component instanceof LongRangeValidator) {
            LongRangeValidator validator = (LongRangeValidator) component;
            if (validator.getMaximum() != 0) {
                descriptor.addParameter(MAXIMUM, validator.getMaximum());
            }
            if (validator.getMinimum() != 0) {
                descriptor.addParameter(MINIMUM, validator.getMinimum());
            }
        } else if (component instanceof RegexValidator) {
            RegexValidator validator = (RegexValidator) component;
            descriptor.addParameter(PATTERN, validator.getPattern());
        } else if (component instanceof RequiredValidator) {
            // do nothing.
        } else {
            super.fillParameters(descriptor, component);
        }
    }
}
