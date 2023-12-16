import React, { useEffect } from 'react';
import { FieldBase } from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import { useSyncValue } from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import { ClayInput } from '@clayui/form';

/**
 * Dev24customformfield React Component
 */
const Dev24CustomFormField = ({ disabled, name, onInput, value }) => (
	<ClayInput
		className="ddm-field-text"
		disabled={disabled}
		name={name}
		onInput={onInput}
		type="date"
		value={value}
	/>
);

const myLabels = React.useMemo(() => {
	return [{
		start: '2023-12-12',
		textColor: '#e1528f',
		title: '2 SPOTS'
	}];
}, []);

const myInvalid = React.useMemo(() => {
	return [{
		start: '2023-12-12T08:00',
		end: '2023-12-12T13:00'
	}, {
		start: '2023-12-12T15:00',
		end: '2023-12-12T17:00'
	}, {
		start: '2023-12-12T19:00',
		end: '2023-12-12T20:00'
	}];
}, []);

const Main = (props) => {
	const {
		label,
		name,
		onChange,
		readOnly,
		userData,
		userDataValue,
		value,
		...otherProps
	} = props;

	// const [currentValue, setCurrentValue] = useSyncValue(value ? value : userDataValue);
	//
	// useEffect(() => {
	// 	if (Array.isArray(userData)) {
	// 		let method = '';
	// 		switch (userData[0]) {
	// 			case 'getFullName':
	// 				method = 'getUserName';
	// 				break;
	// 			case 'getEmailAddress':
	// 				method = 'getUserEmailAddress';
	// 				break;
	// 		}
	//
	// 		setCurrentValue(Liferay.ThemeDisplay[method]());
	// 	}
	//
	// 	// Initialize datepicker using jQuery
	// }, [userData]);

	return (
		<FieldBase
			label={label}
			name={name}
			predefinedValue={userDataValue}
			{...otherProps}
		>
			<Dev24CustomFormField
				disabled={readOnly}
				name={name}
				onInput={onChange}
				value={currentValue}
				controls={['calendar', 'timegrid']}
				min="2023-12-11T00:00"
				max="2024-06-11T00:00"
				minTime="08:00"
				maxTime="19:59"
				stepMinute={60}
				labels={myLabels}
				invalid={myInvalid}
			/>
			{/*<Datepicker*/}
			{/*	controls={['calendar', 'timegrid']}*/}
			{/*	min="2023-12-11T00:00"*/}
			{/*	max="2024-06-11T00:00"*/}
			{/*	minTime="08:00"*/}
			{/*	maxTime="19:59"*/}
			{/*	stepMinute={60}*/}

			{/*/>*/}

			{/* Make sure to add an element with the specified ID for the jQuery plugin */}
		</FieldBase>
	);
};

export default Main;


