package com.mirum.form.submission.internal.action.portlet;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import com.mirum.form.submission.internal.action.util.MirumHTTPInvoke;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	property = {
		"javax.portlet.name=" + DDMPortletKeys.DYNAMIC_DATA_MAPPING_FORM,
		"mvc.command.name=/dynamic_data_mapping_form/get_otp_code",
		"javax.portlet.security-role-ref=guest,administrator"
	},
	service = MVCResourceCommand.class
)
public class MirumFormResourceMVCCommand implements MVCResourceCommand {

	@Override
	public boolean serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws PortletException {

		try {
			String phoneNumber = ParamUtil.getString(
				resourceRequest, "phoneNumber");

			MirumHTTPInvoke.getOTPCode(phoneNumber);
		}
		catch (IOException e) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MirumFormResourceMVCCommand.class);

}