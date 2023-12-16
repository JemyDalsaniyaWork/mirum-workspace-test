package com.dev24.dynamic.data.mapping.form.field.type.form.field;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;

@DDMForm
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "%Basic",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"label", "userData", "required", "tip"
							}
						)
					}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "%Advanced",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"dataType", "name", "type", 
								"showLabel", "repeatable", "fieldReference"
							}
						)
					}
				)
			}
		)
	}
)
public interface Dev24CustomFormFieldDDMFormFieldTypeSettings extends DefaultDDMFormFieldTypeSettings {
	@DDMFormField(
		label="%user-data-selection",
		optionLabels= {
				"%full-name", "%email"
		},
		optionValues= {
				"getFullName", "getEmailAddress"
		},
		predefinedValue = "full-name", 
		required = true,
		type="select"
	)
	public String userData();

	/*@DDMFormField(
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
	public boolean requireConfirmation();*/
}
