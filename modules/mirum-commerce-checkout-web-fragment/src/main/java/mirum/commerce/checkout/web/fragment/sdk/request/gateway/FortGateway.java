package mirum.commerce.checkout.web.fragment.sdk.request.gateway;

/**
 * Interface which give you ability to override implementation of calling fort api using http
 */
public interface FortGateway {

	/**
	 * @param content
	 * @return String which represent response
	 */
	String send(String content);

}