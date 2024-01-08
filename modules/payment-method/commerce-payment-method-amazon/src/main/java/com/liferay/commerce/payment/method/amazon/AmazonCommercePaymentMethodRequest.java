package com.liferay.commerce.payment.method.amazon;

import com.liferay.commerce.payment.request.CommercePaymentRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author Roselaine Marques
 */
public class AmazonCommercePaymentMethodRequest extends CommercePaymentRequest {

    public AmazonCommercePaymentMethodRequest(BigDecimal amount, String cancelUrl, long commerceOrderId, Locale locale, HttpServletRequest httpServletRequest, String returnUrl, String transactionId) {
        super(amount, cancelUrl, commerceOrderId, locale, returnUrl, transactionId);

        _httpServletRequest = httpServletRequest;

    }

    public HttpServletRequest getHttpServletRequest(){
        return _httpServletRequest;
    }

    private final HttpServletRequest _httpServletRequest;
}
