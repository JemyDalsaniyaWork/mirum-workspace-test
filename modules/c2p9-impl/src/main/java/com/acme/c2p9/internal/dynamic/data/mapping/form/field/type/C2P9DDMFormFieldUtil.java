package com.acme.c2p9.internal.dynamic.data.mapping.form.field.type;

import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;

import java.util.Locale;


/**
 * @author Carolina Barbosa
 */
public class C2P9DDMFormFieldUtil {

    public static String getPattern(boolean dateTime, Locale locale) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(
                dateTime, locale);

        String pattern = simpleDateFormat.toPattern();

        if (StringUtils.countMatches(pattern, "d") == 1) {
            pattern = StringUtil.replace(pattern, 'd', "dd");
        }

        if (StringUtils.countMatches(pattern, "h") == 1) {
            pattern = StringUtil.replace(pattern, 'h', "hh");
        }

        if (StringUtils.countMatches(pattern, "H") == 1) {
            pattern = StringUtil.replace(pattern, 'H', "HH");
        }

        if (StringUtils.countMatches(pattern, "M") == 1) {
            pattern = StringUtil.replace(pattern, 'M', "MM");
        }

        if (StringUtils.countMatches(pattern, "y") == 2) {
            pattern = StringUtil.replace(pattern, 'y', "yy");
        }

        return pattern;
    }

    public static SimpleDateFormat getSimpleDateFormat(
            boolean dateTime, Locale locale) {

        if (dateTime) {
            return (SimpleDateFormat)DateFormatFactoryUtil.getDateTime(locale);
        }

        return (SimpleDateFormat)DateFormatFactoryUtil.getDate(locale);
    }

}
