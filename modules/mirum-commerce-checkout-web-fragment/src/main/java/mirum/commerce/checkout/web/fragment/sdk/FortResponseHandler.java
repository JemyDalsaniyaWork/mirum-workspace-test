package mirum.commerce.checkout.web.fragment.sdk;

import mirum.commerce.checkout.web.fragment.sdk.exception.FortException;
import mirum.commerce.checkout.web.fragment.sdk.parameter.FortParameter;

/**
 * Callback interface for fort call api method
 */
public interface FortResponseHandler {

	void handleResponse(FortParameter response);

	void handleFailure(FortParameter originalRequest, FortException ex);

}