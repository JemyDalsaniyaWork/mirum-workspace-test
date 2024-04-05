package mirum.commerce.checkout.web.fragment.sdk.types;

/**
 * Represent the available accessible environments for FORT merchants
 * -   SandBox
 * -   Production
 *
 * Please note that account details are different for each environment.
 */
public enum FortEnvironment {

	SAND_BOX(
		"https://sbpaymentservices.payfort.com/FortAPI/paymentApi",
		"https://sbcheckout.payfort.com/FortAPI/paymentPage"),
	PROD(
		"https://paymentservices.payfort.com/FortAPI/paymentApi",
		"https://checkout.payfort.com/FortAPI/paymentPage");

	public String apiURL() {
		return apiURL;
	}

	public String pageURL() {
		return pageURL;
	}

	FortEnvironment(String apiURL, String pageURL) {
		this.apiURL = apiURL;
		this.pageURL = pageURL;
	}

	private final String apiURL;
	private final String pageURL;

}