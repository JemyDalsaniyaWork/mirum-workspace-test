package com.liferay.commerce.payment.method.amazon;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.request.CommercePaymentRequestProvider;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Roselaine Marques
 */
@Component(immediate = true,
        property = "commerce.payment.engine.method.key=" + AmazonCommercePaymentMethod.KEY,
        service = CommercePaymentRequestProvider.class
)
public class AmazonCommercePaymentRequestProvider implements CommercePaymentRequestProvider {

    @Override
    public CommercePaymentRequest getCommercePaymentRequest(String cancelUrl, long commerceOrderId, HttpServletRequest httpServletRequest, Locale locale, String returnUrl, String transactionId) throws PortalException {

        CommerceOrder commerceOrder =
                _commerceOrderLocalService.getCommerceOrder(commerceOrderId);

        return new com.liferay.commerce.payment.method.amazon.AmazonCommercePaymentMethodRequest(
                commerceOrder.getTotal(), cancelUrl, commerceOrderId, locale, httpServletRequest, returnUrl, transactionId);

    }

    @Reference
    private CommerceOrderLocalService _commerceOrderLocalService;
}
