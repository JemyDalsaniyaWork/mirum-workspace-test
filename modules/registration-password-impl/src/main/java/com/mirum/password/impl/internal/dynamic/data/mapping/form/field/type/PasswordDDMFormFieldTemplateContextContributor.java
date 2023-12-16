/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.mirum.password.impl.internal.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldOptionsFactory;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.liferay.portal.kernel.util.Validator;
import com.mirum.password.impl.internal.dynamic.data.mapping.form.field.type.util.DDMFormFieldTypeUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "ddm.form.field.type.name=registration-password",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		PasswordDDMFormFieldTemplateContextContributor.class
	}
)
public class PasswordDDMFormFieldTemplateContextContributor
		implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		Locale locale = ddmFormFieldRenderingContext.getLocale();

		if (ddmFormFieldRenderingContext.isReturnFullContext()) {
			parameters = HashMapBuilder.<String, Object>put(
					"confirmationErrorMessage",
					DDMFormFieldTypeUtil.getPropertyValue(
							ddmFormField, locale, "confirmationErrorMessage")
			).put(
					"confirmationLabel",
					DDMFormFieldTypeUtil.getPropertyValue(
							ddmFormField, locale, "confirmationLabel")
			).put(
					"direction", ddmFormField.getProperty("direction")
			).put(
					"hideField",
					GetterUtil.getBoolean(ddmFormField.getProperty("hideField"))
			).put(
					"maxLength",
					() -> {
						Object maxLength = ddmFormField.getProperty("maxLength");

						if (Validator.isNotNull(maxLength)) {
							return GetterUtil.getInteger(maxLength);
						}

						return null;
					}
			).put(
					"placeholder",
					DDMFormFieldTypeUtil.getPropertyValue(
							ddmFormField, locale, "placeholder")
			).put(
					"requireConfirmation",
					GetterUtil.getBoolean(
							ddmFormField.getProperty("requireConfirmation"))
			).put(
					"showCounter",
					() -> {
						Object showCounter = ddmFormField.getProperty(
								"showCounter");

						if (showCounter != null) {
							return GetterUtil.getBoolean(showCounter);
						}

						return null;
					}
			).put(
					"tooltip",
					DDMFormFieldTypeUtil.getPropertyValue(
							ddmFormField, locale, "tooltip")
			).build();
		}

		return HashMapBuilder.<String, Object>put(
				"placeholder",
				_getPlaceholder(ddmFormField, ddmFormFieldRenderingContext)
		).put(
				"tooltip", _getTooltip(ddmFormField, ddmFormFieldRenderingContext)
		).put(
				"predefinedValue",
				DDMFormFieldTypeUtil.getPropertyValue(
						ddmFormField, ddmFormFieldRenderingContext.getLocale(),
						"predefinedValue")
		).putAll(
				parameters
		).build();
	}

	private String _getPlaceholder(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue placeholder = (LocalizedValue)ddmFormField.getProperty(
				"placeholder");

		return _getValueString(
				placeholder, ddmFormFieldRenderingContext.getLocale());
	}

	private String _getTooltip(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue tooltip = (LocalizedValue)ddmFormField.getProperty(
				"tooltip");

		return _getValueString(
				tooltip, ddmFormFieldRenderingContext.getLocale());
	}

	private String _getValueString(Value value, Locale locale) {
		if (value != null) {
			return value.getString(locale);
		}

		return StringPool.BLANK;
	}

}