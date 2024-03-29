package com.acme.c2p9.internal.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueValidationException;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueValidator;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.Validator;

import java.text.ParseException;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcela Cunha
 * @author Pedro Queiroz
 */
@Component(
        property = "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.DATE,
        service = DDMFormFieldValueValidator.class
)
public class C2P9DDMFormFieldValueValidator
        implements DDMFormFieldValueValidator {

    @Override
    public void validate(DDMFormField ddmFormField, Value value)
            throws DDMFormFieldValueValidationException {

        for (Locale availableLocale : value.getAvailableLocales()) {
            _validateDateValue(
                    ddmFormField, availableLocale,
                    value.getString(availableLocale));
        }
    }

    @Reference
    protected JSONFactory jsonFactory;

    private void _validateDateValue(
            DDMFormField ddmFormField, Locale locale, String valueString)
            throws DDMFormFieldValueValidationException {

        if (Validator.isNotNull(valueString)) {
            try {
                DateUtil.formatDate("yyyy-MM-dd", valueString, locale);
            }
            catch (ParseException parseException) {
                if (_log.isDebugEnabled()) {
                    _log.debug(parseException);
                }

                throw new DDMFormFieldValueValidationException(
                        String.format(
                                "Invalid date input for date field \"%s\"",
                                ddmFormField.getName()));
            }
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(
            C2P9DDMFormFieldValueValidator.class);

}
