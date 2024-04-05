package mirum.commerce.checkout.web.fragment.util;

import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PropsUtil;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import mirum.commerce.checkout.web.fragment.sdk.FortAccount;
import mirum.commerce.checkout.web.fragment.sdk.FortKeys;
import mirum.commerce.checkout.web.fragment.sdk.FortRequestConfiguration;
import mirum.commerce.checkout.web.fragment.sdk.FortSDK;
import mirum.commerce.checkout.web.fragment.sdk.parameter.FortParameter;
import mirum.commerce.checkout.web.fragment.sdk.types.FortEnvironment;

public class AmazonPaymentUtil {

	public static String getAccessCode() {

		// TODO

		return PropsUtil.get("amazon.payment.access.code");
	}

	public static String getMerchantIdentifier() {
		return PropsUtil.get("amazon.payment.merchant.identifier");
	}

	public static String getSHARequestPhrase() {

		// TODO

		return PropsUtil.get("amazon.sha.request.phrase");
	}

	public static String getSHAResponsePhrase() {

		// TODO

		return PropsUtil.get("amazon.sha.response.phrase");
	}

	public static String getPaymentUrl() {
		return PropsUtil.get("amazon.payment.url");
	}

	public AmazonPaymentUtil(
		String currentURL, ThemeDisplay themeDisplay, String orderCommerceUuid,
		HttpServletRequest httpServletRequest) {

		_accountConfiguration = new FortAccount(
			getAccessCode(), getMerchantIdentifier(), getSHARequestPhrase(),
			getSHAResponsePhrase());
		_configuration = new FortRequestConfiguration();

		// TODO

		_currentURL = currentURL;
		_themeDisplay = themeDisplay;
		_orderCommerceUuid = orderCommerceUuid;
		_paymentUrl = getPaymentUrl();

		_fort = new FortSDK(
			_accountConfiguration, FortEnvironment.SAND_BOX, _configuration);
		_mechantReference = _fort.generateRandomMerchantReference();
		_groupId = _themeDisplay.getScopeGroupId();
		_commerceOrder = (CommerceOrder)httpServletRequest.getAttribute(
			CommerceCheckoutWebKeys.COMMERCE_ORDER);
	}

	public FortParameter buildParameters()
		throws MalformedURLException, PortalException {

		_parameters = new FortParameter();
		_parameters.add(FortKeys.ACCESS_CODE, getAccessCode());
		_parameters.add(FortKeys.AMOUNT, getAmount().intValue());
		_parameters.add(FortKeys.COMMAND, "PURCHASE");
		_parameters.add(FortKeys.CURRENCY, "SAR");
		_parameters.add(FortKeys.CUSTOMER_EMAIL, getCustomerEmail());
		_parameters.add(FortKeys.CUSTOMER_IP, getCustomerIP());
		_parameters.add(FortKeys.ECI, "ECOMMERCE");
		_parameters.add(FortKeys.LANGUAGE, "en");
		_parameters.add(FortKeys.MERCHANT_EXTRA, getFormId());
		_parameters.add(FortKeys.MERCHANT_IDENTIFIER, getMerchantIdentifier());
		_parameters.add(FortKeys.MERCHANT_REFERENCE, getMerchantReference());
		_parameters.add(FortKeys.RETURN_URL, getReturnURL());
		_parameters.add(FortKeys.ORDER_DESCRIPTION, getOrderId());

		return _parameters;
	}

	public Number getAmount() throws PortalException {
		if (_commerceOrder != null) {
			return _commerceOrder.getTotal(
			).intValue() * MULTIPLIER_FOR_AMAZON;
		}

		throw new PortalException("Could not find order ");
	}

	public String getCustomerEmail() {
		if (_themeDisplay.getUser() != null) {
			return _themeDisplay.getUser(
			).getEmailAddress();
		}

		return StringPool.BLANK;
	}

	public String getCustomerIP() {

		// TODO

		return "117.2.164.76";
	}

	public String getFormId() {
		String[] currentURLAsArray = _themeDisplay.getURLCurrent(
		).split(
			"\\?"
		);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"get formId with the following array: " +
					Arrays.toString(currentURLAsArray));
		}

		String[] applicationParamaters = {"", "12345"};

		int lastIndex = currentURLAsArray.length - 1;

		if (!currentURLAsArray[lastIndex].isEmpty()) {
			applicationParamaters = currentURLAsArray[lastIndex].split("=");
		}

		int lastIndexParameter = applicationParamaters.length - 1;

		if (_log.isDebugEnabled()) {
			_log.debug(
				"return the last parameter of currentURL: " +
					applicationParamaters[lastIndexParameter]);
		}

		return applicationParamaters[lastIndexParameter];
	}

	public String getMerchantReference() {
		return _mechantReference;
	}

	public String getRetryPaymentURL() throws MalformedURLException {
		String retryPaymentURL = StringBundler.concat(
			_themeDisplay.getCDNBaseURL(),
			"/web/jcc/checkout/-/checkout/order-summary/", _orderCommerceUuid,
			"?applicantId=" + getFormId());

		if (_log.isDebugEnabled()) {
			_log.debug("return URL: " + _returnURL);
		}

		return retryPaymentURL.toString();
	}

	public String getReturnURL() throws MalformedURLException {
		String baseURLAsString = _getBaseURLAsString();

		URL url = new URL(_currentURL);

		URL baseURL = new URL(
			url.getProtocol(), url.getHost(), url.getPort(), baseURLAsString);

		_log.debug("base url: " + baseURLAsString);

		_returnURL = StringBundler.concat(
			baseURL.toString(),
			"/-/checkout/order-confirmation/" + _orderCommerceUuid +
//				"?p_p_id=com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet" +
					"?_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderUuid=" +
						_orderCommerceUuid);

		if (_log.isDebugEnabled()) {
			_log.debug("return URL: " + _returnURL);
		}

		return _returnURL;
	}

	public String getSignature() throws MalformedURLException, PortalException {
		String signature = StringPool.BLANK;

		if (_fort != null) {
			signature = _fort.calculateRequestSignature(buildParameters());
		}

		return signature;
	}

	public String getOrderId()  {
		return "OrderId: " + String.valueOf(_commerceOrder.getCommerceOrderId());
	}

	public String goToHomePage() {
		return _themeDisplay.getCDNBaseURL() + "/web/jcc";
	}

	private String _getBaseURLAsString() {
		String[] currentURLAsArray = _themeDisplay.getURLCurrent(
		).split(
			"-"
		);
		_log.debug("current: " + _themeDisplay.getURLCurrent());
		String baseURLAsString = "";

		if (currentURLAsArray[0].endsWith("/")) {
			baseURLAsString = currentURLAsArray[0].substring(
				0, currentURLAsArray[0].length() - 1);
		}

		_log.debug("base as String: " + baseURLAsString);

		if (!baseURLAsString.isEmpty()) {
			return baseURLAsString;
		}

		return _themeDisplay.getURLCurrent();
	}

	private static final double MULTIPLIER_FOR_AMAZON = 100;

	private static final Log _log = LogFactoryUtil.getLog(
		AmazonPaymentUtil.class);

	private FortAccount _accountConfiguration;
	private CommerceOrder _commerceOrder;
	private FortRequestConfiguration _configuration;
	private String _currentURL;
	private FortSDK _fort;
	private long _groupId;
	private final String _mechantReference;
	private String _orderCommerceUuid;
	private FortParameter _parameters;
	private String _returnURL;
	private ThemeDisplay _themeDisplay;
	private String _paymentUrl;
	private String orderId;

}
