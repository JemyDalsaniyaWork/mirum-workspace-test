package date.picker.override.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.util.HashMapBuilder;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

@Component(
        property = {
                "ddm.form.field.type.name=datePickerOverride"
        },
        service = {
                DDMFormFieldTemplateContextContributor.class,
                DatePickerTemplateContextContributor.class
        }

)
public class DatePickerTemplateContextContributor implements DDMFormFieldTemplateContextContributor {
    @Override
    public Map<String, Object> getParameters(DDMFormField ddmFormField, DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
        return HashMapBuilder.<String, Object>put("max",ddmFormField.getProperty("max"))
                .put("min",ddmFormField.getProperty("min")).build();
    }
}
