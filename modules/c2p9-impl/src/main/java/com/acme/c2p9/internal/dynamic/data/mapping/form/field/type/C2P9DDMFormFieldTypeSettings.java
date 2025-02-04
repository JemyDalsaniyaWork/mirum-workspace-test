package com.acme.c2p9.internal.dynamic.data.mapping.form.field.type;

/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.annotations.DDMFormRule;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldValidation;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;

/**
 * @author Bruno Basto
 */
@DDMForm(
        rules = {
                @DDMFormRule(
                        actions = "setValue('required', isRequiredObjectField(getValue('objectFieldName')))",
                        condition = "hasObjectField(getValue('objectFieldName'))"
                ),
                @DDMFormRule(
                        actions = {
                                "setEnabled('required', not(hasObjectField(getValue('objectFieldName'))))",
                                "setVisible('dataType', false)",
                                "setVisible('requiredErrorMessage', getValue('required'))"
                        },
                        condition = "TRUE"
                )
        }
)
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
                                                                "name", "fieldReference", "predefinedValue",
                                                                "objectFieldName", "visibilityExpression",
                                                                "fieldNamespace", "indexType",
                                                                "labelAtStructureLevel", "localizable",
                                                                "nativeField", "readOnly", "dataType", "type",
                                                                "showLabel", "repeatable", "validation"
                                                        }
                                                )
                                        }
                                )
                        }
                )
        }
)
public interface C2P9DDMFormFieldTypeSettings
        extends DefaultDDMFormFieldTypeSettings {

    @DDMFormField(predefinedValue = "date", required = true)
    @Override
    public String dataType();

    @DDMFormField(
            dataType = "string", label = "%predefined-value",
            properties = {
                    "tooltip=%enter-a-default-value-that-is-submitted-if-no-other-value-is-entered",
                    "visualProperty=true"
            },
            type = "date"
    )
    @Override
    public LocalizedValue predefinedValue();

    @DDMFormField(dataType = "date", label = "%validation", type = "validation")

    @Override
    public DDMFormFieldValidation validation();

}
