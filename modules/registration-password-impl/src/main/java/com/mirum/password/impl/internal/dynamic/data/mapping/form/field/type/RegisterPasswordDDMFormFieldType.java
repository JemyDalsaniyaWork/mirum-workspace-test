package com.mirum.password.impl.internal.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;

import com.mirum.dynamic.data.mapping.form.field.type.internal.password.RegisterPasswordDDMFormFieldTypeSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"ddm.form.field.type.description=registration-password",
		"ddm.form.field.type.display.order:Integer=10",
		"ddm.form.field.type.group=customized", "ddm.form.field.type.icon=text",
		"ddm.form.field.type.label=registration-password",
		"ddm.form.field.type.name=registration-password"
	},
	service = DDMFormFieldType.class
)
public class RegisterPasswordDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public Class<? extends RegisterPasswordDDMFormFieldTypeSettings>
		getDDMFormFieldTypeSettings() {

		return RegisterPasswordDDMFormFieldTypeSettings.class;
	}

	@Override
	public String getModuleName() {
		return _npmResolver.resolveModuleName(
			"dynamic-data-mapping-form-field-type-register-password/password/Password.es");
	}

	@Override
	public String getName() {
		return "registration-password";
	}

	@Override
	public boolean isCustomDDMFormFieldType() {
		return true;
	}

	@Reference
	private NPMResolver _npmResolver;

}