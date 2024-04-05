package com.acme.c2p9.internal.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.vulcan.util.TransformUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * @author Marcellus Tavares
 */
@Component(
        property = {
                "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.DATE,
                "ddm.form.field.type.name=" + DDMFormFieldTypeConstants.DATE_TIME
        },
        service = DDMFormFieldTemplateContextContributor.class
)
public class C2P9DDMFormFieldTemplateContextContributor
        implements DDMFormFieldTemplateContextContributor {



    @Override
    public Map<String, Object> getParameters(
            DDMFormField ddmFormField,
            DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {


        return HashMapBuilder.<String, Object>put(
                "firstDayOfWeek",
                _getFirstDayOfWeek(ddmFormFieldRenderingContext.getLocale())
        ).put(
                "months",
                Arrays.asList(
                        CalendarUtil.getMonths(
                                ddmFormFieldRenderingContext.getLocale()))
        ).put(
                "predefinedValue",
                getPropertyValue(
                        ddmFormField, ddmFormFieldRenderingContext.getLocale(),
                        "predefinedValue")
        ).put(
                "tooltip",
                getPropertyValue(
                        ddmFormField, ddmFormFieldRenderingContext.getLocale(),
                        "tooltip")
        ).put(
                "weekdaysShort",
                TransformUtil.transformToList(
                        CalendarUtil.DAYS_ABBREVIATION,
                        day -> _language.get(
                                ddmFormFieldRenderingContext.getLocale(), day))
        ).put(
                "years", _getYears()
        ).build();
    }

    private int _getFirstDayOfWeek(Locale locale) {
        WeekFields weekFields = WeekFields.of(locale);

        DayOfWeek dayOfWeek = weekFields.getFirstDayOfWeek();

        return dayOfWeek.getValue() % 7;
    }

    private List<Integer> _getYears() {
        List<Integer> years = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, -4);

        for (int i = 0; i < 5; i++) {
            years.add(calendar.get(Calendar.YEAR));

            calendar.add(Calendar.YEAR, 1);
        }

        return years;
    }
    public static String getPropertyValue(
            DDMFormField ddmFormField, Locale locale, String propertyName) {

        LocalizedValue value = (LocalizedValue)ddmFormField.getProperty(
                propertyName);

        if (value == null) {
            return StringPool.BLANK;
        }

        return GetterUtil.getString(value.getString(locale));
    }
    @Reference
    private Language _language;

}
