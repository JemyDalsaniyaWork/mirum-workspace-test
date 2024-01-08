package com.liferay.commerce.payment.method.amazon;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.constants.CommercePaymentConstants;
import com.liferay.commerce.payment.method.CommercePaymentMethod;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.*;


/**
 * @author Roselaine Marques
 */
@Component(
        immediate = true,
        property = "commerce.payment.engine.method.key="+ AmazonCommercePaymentMethod.KEY,
        service = CommercePaymentMethod.class
)
public class AmazonCommercePaymentMethod implements CommercePaymentMethod {

    public static final String KEY = "amazon";

    @Override
    public String getKey() {
        return KEY;
    }
    @Override
    public String getDescription(Locale locale) {

        ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
                "content.Language", locale, getClass());

        return LanguageUtil.get(
                resourceBundle, "this-is-payment-method-with-card-amazon");
    }

    @Override
    public String getName(Locale locale) {

            ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
                    "content.Language", locale, getClass());

            return LanguageUtil.get(resourceBundle, "amazon-payment-with-card");
    }

    @Override
    public int getPaymentType() {
        return CommercePaymentConstants.COMMERCE_PAYMENT_METHOD_TYPE_ONLINE_REDIRECT;
    }

    @Override
    public String getServletPath() {
        return "";
    }

    @Override
    public boolean isCancelEnabled() {
        return true;
    }

    @Override
    public boolean isCompleteEnabled() {
        return true;
    }

    @Override
    public boolean isProcessPaymentEnabled() {
        return true;
    }

    @Override
    public CommercePaymentResult cancelPayment(CommercePaymentRequest commercePaymentRequest) throws Exception {

        AmazonCommercePaymentMethodRequest amazonCommercePaymentMethodRequest = (AmazonCommercePaymentMethodRequest)commercePaymentRequest;

        List<String> messages = getMessagesFromResponse(amazonCommercePaymentMethodRequest);

        return new CommercePaymentResult(
                amazonCommercePaymentMethodRequest.getTransactionId(),
                amazonCommercePaymentMethodRequest.getCommerceOrderId(),
                CommerceOrderPaymentConstants.STATUS_CANCELLED, false, null, null,
                messages, true);
    }

    @Override
    public CommercePaymentResult completePayment(CommercePaymentRequest commercePaymentRequest) throws Exception {

        AmazonCommercePaymentMethodRequest amazonCommercePaymentMethodRequest = (AmazonCommercePaymentMethodRequest)commercePaymentRequest;
        List<String> messages = getMessagesFromResponse(amazonCommercePaymentMethodRequest);

        return new CommercePaymentResult(
                amazonCommercePaymentMethodRequest.getTransactionId(),
                amazonCommercePaymentMethodRequest.getCommerceOrderId(),
                CommerceOrderConstants.PAYMENT_STATUS_PAID, false, null, null,
                messages, true);
    }

    @Override
    public CommercePaymentResult processPayment(CommercePaymentRequest commercePaymentRequest) throws Exception {

        return null;
        
    }

    private List<String> getMessagesFromResponse(AmazonCommercePaymentMethodRequest amazonCommercePaymentMethodRequest){

       return null;
    }

    @Reference
    private CommerceOrderService _commerceOrderService;

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Reference
    private Http _http;

    @Reference
    private Portal _portal;
}
