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
import {ClayInput} from '@clayui/form';

import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import React, {useEffect, useRef, useState} from 'react';
import {getOTPCode} from './services/api'

const buttonOtp = {
    "width": "100px",
    "height": "auto",
    "textAlign": "center",
    "whiteSpace": "nowrap",
    "verticalAlign": "middle",
    "fontSize": "1em",
    "fontWeight": "400",
    "textAlign": "center",
    "lineHeight": "40px",
    "color": "#fff",
    "borderColor": "10px",
    "backgroundColor": "#007bff",
    "lineHeight": "1.5",
    "transition": "color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out,",
    "cursor": "pointer",
    "borderStyle": "solid",
    "borderWidth": "0.0625rem",
    "borderRadius": "0.25rem",
    "boxShadow": "none",
    "direction": "rtl"
}

const Main = ({
	label,
	name,
	onChange,
	predefinedValue,
	readOnly,
	shouldUpdateValue = false,
	syncDelay = true,
	value,
	...otherProps
}) => {

    const [phoneNumber, setPhoneNumber] = useSyncValue(
        value ? value : predefinedValue, syncDelay
     );

    const [loading, setLoading] = useState(false);
    const [isPhoneNumberValid, setIsPhoneNumberValid] = useState(false);
    const [validToRequestCode, setValidToRequestCode] = useState(false);
    const [resendOTP, setResendOTP] = useState(false);
    const [showInvalidMessage, setShowInvalidMessage] = useState(false);
    const [isVerified, setVerify] = useState(false);

    var uaePhonePatternRegex = /^(05|5)(5|0|3|6|4|9|1|8|7)([0-9]{7})$/;

    const handleOnBlur = (e) => {
        if (!phoneNumber) {
            setIsPhoneNumberValid(false);
            return;
        }

        if (uaePhonePatternRegex.test(phoneNumber)) {
                        // true
            setIsPhoneNumberValid(true);
            if (!resendOTP) {
                setValidToRequestCode(true);
            }
            setShowInvalidMessage(false);

        }
       else {
            setIsPhoneNumberValid(false);
            setValidToRequestCode(false);
            setShowInvalidMessage(true);
       }
    }

	// Sent OTP
	const sendOTPToPhone = async () => {
        setLoading(true);
        if (phoneNumber === "" || phoneNumber.length < 8) return;

        const getOTPResponse = await getOTPCode(phoneNumber);

        setVerify(true);
        setValidToRequestCode(false);
        setResendOTP(true);
        setLoading(false);
    };

	return (
		<FieldBase
			label={label}
			name={name}
			value={phoneNumber}
			predefinedValue={predefinedValue}
			{...otherProps}
		>
			<div style={{ "marginTop": "10px" }}>
                <div className="input-field">
                    <ClayInput
                        className="ddm-field-slider form-control slider"
                        disabled={readOnly}
                        id="phoneNumber"
                        name={name}
                        onChange={(event) => {
                            setPhoneNumber(event.target.value);
                            onChange(event);
                        }}
                        onBlur={handleOnBlur}
                        type="text"
                        placeholder="5 XXXX XXXX"
                        value={phoneNumber ? phoneNumber : predefinedValue}
                        style={{
                            "width": "100%",
                        }}
                    />
                    <br />
                    <div id="recaptcha-container">
                        <p  style={{
                            ...buttonOtp,
                            ...{display: (validToRequestCode) ? "block": "none"},
                            }}
                            onClick={sendOTPToPhone}>
                            أرسل الرمز
                        </p>
                        <p
                            style={{
                                ...{display: (showInvalidMessage) ? "block": "none"}
                            }}
                        >
                            <span
                                className="form-feedback-indicator"
                                style={{
                                    "color": "#da1414",
                                    "fontWeight": "600",
                                }}>
                                رقم الهاتف غير صالح
                            </span>
                        </p>
                        <div
                            style={{
                                display: (resendOTP) ? "block": "none"
                            }}
                        >
                            تم إرسال الرمز إلى هاتفك المحمول ، يرجى إدخاله للمتابعة
                            <p
                                style={buttonOtp}
                                onClick={sendOTPToPhone}>
                              إعادة إرسال الرمز
                             </p>
                        </div>
                        <p
                            style={{
                                display: (loading) ? "block": "none"
                            }}
                        > جار التحميل ... </p>
                    </div>
                </div>
            </div>
		</FieldBase>
	);
};

Main.displayName = 'PhoneNumber';

export default Main;
