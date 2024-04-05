package date.picker.override.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeSettings;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author root318
 */
@Component(
	property = {
		"ddm.form.field.type.description=date-picker-override-description",
		"ddm.form.field.type.display.order:Integer=13",
		"ddm.form.field.type.group=customized",
		"ddm.form.field.type.icon=text",
		"ddm.form.field.type.label=date-picker-override-label",
		"ddm.form.field.type.name=datePickerOverride"
	},
	service = DDMFormFieldType.class
)
public class DatePickerOverrideDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public Class<? extends DDMFormFieldTypeSettings>
	getDDMFormFieldTypeSettings() {

		return DatePickerFormFieldTypeSettings.class;
	}
	@Override
	public String getModuleName() {
		return _npmResolver.resolveModuleName(
			"dynamic-data-date-picker-override-form-field/date-picker-override.es");
	}

	@Override
	public String getName() {
		return "datePickerOverride";
	}

	@Override
	public boolean isCustomDDMFormFieldType() {
		return true;
	}

	@Reference
	private NPMResolver _npmResolver;

}