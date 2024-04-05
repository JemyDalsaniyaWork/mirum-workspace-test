package mirum.commerce.checkout.web.fragment.sdk;

import mirum.commerce.checkout.web.fragment.sdk.request.converter.RequestConverter;
import mirum.commerce.checkout.web.fragment.sdk.request.converter.json.JsonRequestConverter;
import mirum.commerce.checkout.web.fragment.sdk.request.gateway.FortGateway;
import mirum.commerce.checkout.web.fragment.sdk.request.gateway.http.DefaultFortGateway;
import mirum.commerce.checkout.web.fragment.sdk.types.FortEnvironment;
import mirum.commerce.checkout.web.fragment.sdk.types.FortLanguage;

/**
 * <p>
 * You also must provide on which environment you need to call (Sand Box or Prod)
 * <p>
 * you can also used to configure default language for all calls with fort
 * You can used these class to override any implementation used in sdk like :
 * -   Request Converter
 * -   Form Generator
 * -   Fort Gateway
 * -   Default language
 */
public class FortRequestConfiguration {

	/**
	 * Used to override fort gateway Default is http using apache http Client
	 * Your Implementation must be thread safe
	 */
	public FortRequestConfiguration setGateway(FortGateway gateway) {
		this.gateway = gateway;

		return this;
	}

	/**
	 * Used to set the value of default language in all request
	 * You can override these on individual call
	 */
	public FortRequestConfiguration setLanguage(String language) {
		this.language = language;

		return this;
	}

	/**
	 * Used to override request converter Default is Json using Gson api
	 * Your Implementation must be thread safe
	 */
	public FortRequestConfiguration setRequestConverter(
		RequestConverter requestConverter) {

		this.requestConverter = requestConverter;

		return this;
	}

	private FortGateway gateway;
	private String language = FortLanguage.English.getCode();
	private RequestConverter requestConverter;

	FortGateway getGateway(FortEnvironment fortEnvironment) {
		if (gateway == null)
			gateway = new DefaultFortGateway(
				fortEnvironment.apiURL(), "application/json");

		return gateway;
	}

	RequestConverter getRequestConverter() {
		if (requestConverter == null)
			requestConverter = new JsonRequestConverter();

		return requestConverter;
	}

	String getLanguage() {
		return this.language;
	}

}