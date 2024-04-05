import React, {useState} from 'react';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import ClayDatePicker from '@clayui/date-picker';

const Main = ({label, name, onChange, predefinedValue, readOnly, value, min, max, ...otherProps}) => {
    // const [currentValue, setCurrentValue] = useSyncValue(value ? value : predefinedValue);
    const initialValue = value !== null && value !== undefined ? value : predefinedValue;
    const [currentValue, setCurrentValue] = useState(initialValue);

    // Convert min and max to integers if they are provided as strings
    const defaultMin = min !== '' ? parseInt(min, 10) : 1997; // Default to 1997 if min is empty
    const defaultMax = max !== '' ? parseInt(max, 10) : 2024; // Default to 2024 if max is empty

    const currentDate = new Date().getDate(); // Get the current date (1-31)
    const currentMonth = new Date().getMonth() + 1;

    const defaultVal = defaultMax + "-" + currentMonth + "-" + currentDate;
    return (
        <FieldBase
            label={label}
            name={name}
            predefinedValue={predefinedValue}
            {...otherProps}
        >
            <ClayDatePicker
                name={name}
                onChange={(date) => {
                    setCurrentValue(date);
                    onChange(date);
                }}
                placeholder="__/__/____"
                dateFormat="MM/dd/yyyy"
                // dateFormat={clayFormat}
                years={{
                    end: defaultMax,
                    start: defaultMin
                }}
                value={currentValue}
                defaultMonth={defaultVal}

            />
        </FieldBase>
    );
}

Main.displayName = 'DatePickerOverride';

export default Main;
