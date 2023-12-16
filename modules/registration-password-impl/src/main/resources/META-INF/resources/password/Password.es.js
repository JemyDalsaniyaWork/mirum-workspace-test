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
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import withConfirmationField from 'dynamic-data-mapping-form-field-type/util/withConfirmationField.es';
import React, { useState } from 'react';

const RegisterPassword = ({
	name,
	onBlur,
	onChange,
	onFocus,
	placeholder,
	predefinedValue,
	readOnly: disabled,
	value: initialValue,
	...otherProps
}) => {
	const [value, setValue] = useState(
		initialValue ? initialValue : predefinedValue
	);

	return (
		<FieldBase {...otherProps} name={name} readOnly={disabled}>
			<input
				className="ddm-field-text form-control"
				disabled={disabled}
				id={name}
				name={name}
				onBlur={onBlur}
				onFocus={onFocus}
				onInput={(event) => {
					onChange(event);
					setValue(event.target.value);
				}}
				placeholder={placeholder}
				type="password"
				value={value}
			/>
		</FieldBase>
	);
};

RegisterPassword.displayName = 'Password for registration';
export default withConfirmationField(RegisterPassword);

