/**
 *
 */
package org.richfaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.BigIntegerConverter;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.ByteConverter;
import javax.faces.convert.CharacterConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.DoubleConverter;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FloatConverter;
import javax.faces.convert.IntegerConverter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.convert.ShortConverter;

/**
 * @author asmirnov
 *
 */
public class ConverterServiceImpl extends FacesServiceBase<Converter> implements FacesConverterService {
    private static final String DEFAULT_CONVERTER_MESSAGE_ID = UIInput.CONVERSION_MESSAGE_ID;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.validator.FacesConverterService#getConverterDescription(javax.faces.context.FacesContext,
     * javax.faces.convert.Converter)
     */
    public ConverterDescriptor getConverterDescription(FacesContext context, EditableValueHolder input, Converter converter,
        String converterMessage) {
        // determine converter message.
        FacesMessage message = getMessage(context, converter, input, converterMessage);
        ConverterDescriptorImpl descriptor = new ConverterDescriptorImpl(converter.getClass(), message);
        fillParameters(descriptor, converter);
        descriptor.makeImmutable();
        return descriptor;
    }

    @Override
    protected String getMessageId(Converter converter) {
        String messageId;
        if (converter instanceof BigDecimalConverter) {
            messageId = BigDecimalConverter.DECIMAL_ID;
        } else if (converter instanceof BigIntegerConverter) {
            messageId = BigIntegerConverter.BIGINTEGER_ID;
        } else if (converter instanceof BooleanConverter) {
            messageId = BooleanConverter.BOOLEAN_ID;
        } else if (converter instanceof ByteConverter) {
            messageId = ByteConverter.BYTE_ID;
        } else if (converter instanceof CharacterConverter) {
            messageId = CharacterConverter.CHARACTER_ID;
        } else if (converter instanceof DateTimeConverter) {
            // TODO - distinguish Date, Time, and DateTime.
            messageId = DateTimeConverter.DATETIME_ID;
        } else if (converter instanceof DoubleConverter) {
            messageId = DoubleConverter.DOUBLE_ID;
        } else if (converter instanceof EnumConverter) {
            messageId = EnumConverter.ENUM_ID;
        } else if (converter instanceof FloatConverter) {
            messageId = FloatConverter.FLOAT_ID;
        } else if (converter instanceof IntegerConverter) {
            messageId = IntegerConverter.INTEGER_ID;
        } else if (converter instanceof LongConverter) {
            messageId = LongConverter.LONG_ID;
        } else if (converter instanceof NumberConverter) {
            // TODO - detect case ( currency, percent etc ).
            messageId = NumberConverter.NUMBER_ID;
        } else if (converter instanceof ShortConverter) {
            messageId = ShortConverter.SHORT_ID;
        } else {
            messageId = DEFAULT_CONVERTER_MESSAGE_ID;
        }
        return messageId;
    }
}
