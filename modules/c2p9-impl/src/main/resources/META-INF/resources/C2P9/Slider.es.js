import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import React from 'react';
import ClayDatePicker from "@clayui/date-picker";

const Main = ({
				  label,
				  name,
				  onChange,
				  predefinedValue,
				  readOnly,
				  value,
				  ...otherProps
			  }) => {
	const [currentValue, setCurrentValue] = useSyncValue(
		value ? value : predefinedValue
	);
	return (
		<FieldBase
			label={label}
			name={name}
			predefinedValue={predefinedValue}
			{...otherProps}
		>
			<ClayDatePicker
				name={name}
				onChange={(event) => {
					setCurrentValue(event);
					onChange(event);
				}}
				placeholder="__/__/____"
				dateFormat="MM/dd/yyyy"
				years={{
					end: 2024,
					start: 1990
				}}
				value={currentValue}
				readOnly={readOnly}

			/>
		</FieldBase>
	);
};

Main.displayName = 'DatePicker';

export default Main;
