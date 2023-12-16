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

package com.mirum.dynamic.data.mapping.form.field.type.internal.password;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.annotations.DDMFormRule;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;

/**
 * @author Marcellus Tavares
 */
@DDMForm(
	rules = {
		@DDMFormRule(
			actions = {
				"setVisible('requireConfirmation', TRUE)",
				"setVisible('required', TRUE)", "setVisible('showLabel', TRUE)",
				"setVisible('validation', TRUE)",
				"setVisible('indexType', false)",
				"setVisible('predefinedValue', false)"
			}
		),
		@DDMFormRule(
			actions = {
					"setValue('required', TRUE)", "setValue('showLabel', TRUE)",
					"setValue('required', TRUE)",
					"setVisible('required', TRUE)",
			},
			condition = "hasObjectField(getValue('objectFieldName'))"
		),
		@DDMFormRule(
			actions = {
				"setVisible('confirmationErrorMessage', getValue('requireConfirmation'))",
				"setVisible('confirmationLabel', getValue('requireConfirmation'))",
				"setVisible('direction', getValue('requireConfirmation'))",
				"setVisible('requiredErrorMessage', getValue('required'))"
			}
		)
	}
)
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "basic",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12, value = {
									"label", "tip", "required",
									"requiredErrorMessage"
							}
						)
					}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "properties",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"name", "fieldReference", "predefinedValue",
								"objectFieldName", "placeholder",
								"visibilityExpression", "fieldNamespace",
								"indexType", "localizable", "readOnly",
								"dataType", "type", "showLabel", "repeatable",
								"requireConfirmation", "direction",
								"confirmationLabel", "confirmationErrorMessage"
							}
						)
					}
				)
			}
		)
	}
)
public interface RegisterPasswordDDMFormFieldTypeSettings
	extends DefaultDDMFormFieldTypeSettings {

	@DDMFormField(
			label = "%direction", optionLabels = {"%horizontal", "%vertical"},
			optionValues = {"horizontal", "vertical"},
			predefinedValue = "[\"vertical\"]",
			properties = "showEmptyOption=false", type = "select"
	)
	public String direction();
	@DDMFormField(
		dataType = "string", label = "%error-message",
		properties = "initialValue=%the-information-does-not-match",
		type = "text"
	)
	public LocalizedValue confirmationErrorMessage();

	@DDMFormField(
		dataType = "string", label = "%label",
		properties = "initialValue=%confirm", type = "text"
	)
	public LocalizedValue confirmationLabel();

	@DDMFormField(
		dataType = "string", label = "%placeholder-text",
		properties = {
			"tooltip=%enter-text-that-assists-the-user-but-is-not-submitted-as-a-field-value",
			"visualProperty=true"
		},
		type = "text"
	)
	public LocalizedValue placeholder();

	@DDMFormField(
		label = "%require-confirmation", properties = "showAsSwitcher=true"
	)
	public boolean requireConfirmation();

}