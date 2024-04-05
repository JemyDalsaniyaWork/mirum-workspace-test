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
import {
    createResourceURL
} from 'frontend-js-web';

export function getOTPCode(phoneNumber) {

    var phoneNumberInput = document.getElementById("phoneNumber");

    var portletId = phoneNumberInput.name.split('$')[0];

    portletId = portletId.replace("_ddm", "");
    portletId = portletId.replace("_com_", "com_");

    var basePortletURL = `${Liferay.ThemeDisplay.getPortalURL()}/${Liferay.currentURL}`;
    var url = createResourceURL(
        basePortletURL, {
            'p_p_resource_id': '/dynamic_data_mapping_form/get_otp_code',
            'phoneNumber': phoneNumber,
            'p_auth': `${Liferay.authToken}`,
            'p_p_id': portletId
        }
    );

    return fetch(url.toString(), {
            method: 'POST'
        })
        .then(response => {
            return response.text();
        })
        .then(data => {});
}