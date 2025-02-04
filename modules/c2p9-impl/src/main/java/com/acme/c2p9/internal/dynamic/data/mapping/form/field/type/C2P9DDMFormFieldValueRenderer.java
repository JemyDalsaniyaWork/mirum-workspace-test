package com.acme.c2p9.internal.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueRenderer;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.form.validation.util.DateParameterUtil;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.Validator;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;

import java.util.Locale;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;

/**
 * @author Bruno Basto
 */
@Component(
        property = {
                "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.DATE,
                "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.DATE_TIME
        },
        service = DDMFormFieldValueRenderer.class
)
public class C2P9DDMFormFieldValueRenderer
        implements DDMFormFieldValueRenderer {

    @Override
    public String render(DDMFormFieldValue ddmFormFieldValue, Locale locale) {
        Value value = ddmFormFieldValue.getValue();

        return _render(locale, value.getString(locale));
    }

    private String _render(Locale defaultLocale, String valueString) {
        if (Validator.isNull(valueString)) {
            return StringPool.BLANK;
        }

        Locale locale = LocaleThreadLocal.getThemeDisplayLocale();

        if (locale == null) {
            locale = defaultLocale;
        }

        boolean dateTime = Pattern.matches(
                "^\\d{4}-\\d{2}-\\d{2} \\d{1,2}:\\d{2}$", valueString);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                C2P9DDMFormFieldUtil.getPattern(dateTime, locale), locale);

        if (dateTime) {
            LocalDateTime localDateTime = DateParameterUtil.getLocalDateTime(
                    valueString);

            return localDateTime.format(
                    dateTimeFormatter.withDecimalStyle(DecimalStyle.of(locale)));
        }

        LocalDate localDate = DateParameterUtil.getLocalDate(valueString);

        return localDate.format(
                dateTimeFormatter.withDecimalStyle(DecimalStyle.of(locale)));
    }

}
