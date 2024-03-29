package date.picker.override.form.field;

import com.liferay.dynamic.data.mapping.annotations.*;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;

@DDMForm
@DDMFormLayout(
        paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
        value = {
                @DDMFormLayoutPage(
                        title = "%basic",
                        value = {
                                @DDMFormLayoutRow(
                                        {
                                                @DDMFormLayoutColumn(
                                                        size = 12,
                                                        value = {
                                                                "label", "tip", "required",
                                                                "requiredErrorMessage"
                                                        }
                                                )
                                        }
                                )
                        }
                ),
                @DDMFormLayoutPage(
                        title = "%advanced",
                        value = {
                                @DDMFormLayoutRow(
                                        {
                                                @DDMFormLayoutColumn(
                                                        size = 12,
                                                        value = {
//                                                                "dataType", "min", "max", "name", "showLabel",
//                                                                "repeatable", "type", "validation",
//                                                                "visibilityExpression","fieldReference", "predefinedValue"
                                                                "name", "fieldReference", "predefinedValue",
                                                                "objectFieldName", "visibilityExpression",
                                                                "fieldNamespace", "indexType",
                                                                "labelAtStructureLevel", "localizable",
                                                                "nativeField", "readOnly", "dataType", "type",
                                                                "showLabel", "repeatable", "validation","min", "max"                                                        }
                                                )
                                        }
                                )
                        }
                )
        }
)
public interface DatePickerFormFieldTypeSettings extends DefaultDDMFormFieldTypeSettings {
    @DDMFormField(
            label = "%max-value",
            properties = "placeholder=%enter-the-top-limit-of-the-range",
            type = "numeric",
            optionValues= {
                    "2024"
            },
            predefinedValue = "2024"
    )
    public String max();

    @DDMFormField(
            label = "%min-value",
            properties = "placeholder=%enter-the-bottom-limit-of-the-range",
            type = "numeric",
            optionValues= {
            "2021"
    },
            predefinedValue = "2021"
    )
    public String min();



}
