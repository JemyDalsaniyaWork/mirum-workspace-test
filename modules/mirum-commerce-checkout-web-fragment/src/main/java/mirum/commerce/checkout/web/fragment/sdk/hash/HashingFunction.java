package mirum.commerce.checkout.web.fragment.sdk.hash;

import java.nio.charset.Charset;

/**
 * Interface which give you ability to override implementation of hashing function
 */
public interface HashingFunction {

	/**
	 * @param value
	 * @param charset
	 * @return hashed string with specific charset
	 */
	String hash(String value, Charset charset);

}