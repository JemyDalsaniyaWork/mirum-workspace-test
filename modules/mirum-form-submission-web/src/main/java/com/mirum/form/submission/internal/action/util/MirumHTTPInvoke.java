package com.mirum.form.submission.internal.action.util;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MirumHTTPInvoke {

	public static HttpInvoker.HttpResponse getOTPCode(String phoneNumber)
		throws IOException {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);

		httpInvoker.header("accept", "application/json");
		httpInvoker.header("content-type", "application/x-www-form-urlencoded");
		httpInvoker.header("x-authenticate-app-id", APP_ID);
		httpInvoker.header("Authorization", "Bearer " + AUTH_TOKEN);

		httpInvoker.path(
			"https://authenticate.cloud.api.unifonic.com/services/api/v2/verifications/start");
		httpInvoker.body(
			"locale=ar&to=+966" + phoneNumber + "&channel=sms&length=4",
			"application/json");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse;
	}

	public static boolean validateMembership(String membership)
		throws DocumentException, IOException {

		HttpInvoker.HttpResponse httpResponse = verifyMembership(membership);

		String content = httpResponse.getContent();

		Document document = SAXReaderUtil.read(content);

		String verifiedMembership = document.getRootElement(
		).element(
			"Mirum_MBRInfo"
		).element(
			"MemberId"
		).getStringValue();

		boolean validMembership = false;

		if (verifiedMembership.equals(membership)) {
			validMembership = true;
			_logger.fine("HTTP response content: " + content);
			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());
		}
		else {
		}

		return validMembership;
	}

	public static boolean validateOTPCode(String phoneNumber, String OTPCode)
		throws Exception {

		HttpInvoker.HttpResponse httpResponse = verifyPhoneNameWithOTP(
			phoneNumber, OTPCode);

		String content = httpResponse.getContent();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(content);

		boolean validOTP = false;

		if (jsonObject.getInt("error_code") == 101) {
			_logger.fine("HTTP response content: " + content);
			_logger.fine("HTTP response message: " + httpResponse.getMessage());
			_logger.fine(
				"HTTP response status code: " + httpResponse.getStatusCode());

			validOTP = true;
		}
		else {
			_logger.log(
				Level.WARNING,
				"Unable to process HTTP response content: " + content);

			_logger.log(
				Level.WARNING,
				"HTTP response message: " + httpResponse.getMessage());
			_logger.log(
				Level.WARNING,
				"HTTP response status code: " + httpResponse.getStatusCode());
		}

		return validOTP;
	}

	private static HttpInvoker.HttpResponse verifyMembership(String membership)
		throws IOException {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.httpMethod(HttpInvoker.HttpMethod.GET);

		httpInvoker.path(
			"https://jcap.jcci.org.sa/JCCIService/JCCI_MCI_WS.asmx/Mirum_GetmbrInfobymbr?" +
				"Mbrno=" + membership + "&username=Mirum&password=Mi123rum");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse;
	}

	private static HttpInvoker.HttpResponse verifyPhoneNameWithOTP(
			String phoneNumber, String otpCode)
		throws Exception {

		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.header("accept", "application/json");
		httpInvoker.header("content-type", "application/x-www-form-urlencoded");
		httpInvoker.header("x-authenticate-app-id", APP_ID);

		httpInvoker.body(
			"locale=ar&to=+966" + phoneNumber + "&channel=sms&code=" + otpCode,
			"application/json");

		httpInvoker.header("Authorization", "Bearer " + AUTH_TOKEN);
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path(
			"https://authenticate.cloud.api.unifonic.com/services/api/v2/verifications/check");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse;
	}

	private static final String APP_ID = "AP2b33623bc7914483aa7dfaf581adfb82";

	private static final String AUTH_TOKEN =
		"eyJhbGciOiJIUzI1NiJ9.eyJzZXJ2aWNlX2lkIjoiQVAyYjMzNjIzYmM3OTE0NDgzYWE3ZGZhZjU4MWFkZmI4MiJ9.z0Pep5U70LkBLuZwguWyjINRf0r7uMgSI3DOOZT5P7Q";

	private static final Logger _logger = Logger.getLogger(
		MirumHTTPInvoke.class.getName());

}