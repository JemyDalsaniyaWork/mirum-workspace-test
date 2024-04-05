<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>
<%@ page import="mirum.commerce.checkout.web.fragment.util.AmazonPaymentUtil" %>

<style>
	a:hover{
		text-decoration: none;
	}
</style>

<%
OrderConfirmationCheckoutStepDisplayContext orderConfirmationCheckoutStepDisplayContext = (OrderConfirmationCheckoutStepDisplayContext)request.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);

CommerceOrderPayment commerceOrderPayment = orderConfirmationCheckoutStepDisplayContext.getCommerceOrderPayment();

String commerceOrderPaymentContent = null;

if (commerceOrderPayment != null) {
	commerceOrderPaymentContent = commerceOrderPayment.getContent();
}

int paymentStatus = CommerceOrderPaymentConstants.STATUS_PENDING;

if (commerceOrderPayment != null) {
	paymentStatus = commerceOrderPayment.getStatus();
}

int AWSPaymentResult = (int)request.getAttribute("paymentStatus");

CommerceOrder commerceOrder = (CommerceOrder)request.getAttribute(CommerceCheckoutWebKeys.COMMERCE_ORDER);

AmazonPaymentUtil paymentUtil = new AmazonPaymentUtil(currentURL, themeDisplay, commerceOrder.getUuid(), request);
%>

<div class="commerce-checkout-confirmation" style="text-align: center;">
	<c:choose>
		<c:when test="<%= AWSPaymentResult == CommerceOrderPaymentConstants.STATUS_COMPLETED %>">
			<div class="lfr-ddm__default-page-container success">
				<h2 class="lfr-ddm__default-page-title">
					<div class="success-message">
						<liferay-ui:message key="success-your-order-has-been-processed" />
					</div>
				</h2>

				<p class="lfr-ddm__default-page-description">
					<liferay-ui:message key="your-request-is-under-consideration-you-will-be-notified-if-it-is-rejected-or-accepted" />
				</p>

				<div class="" style="text-align: center;">
					<aui:button-row>
						<a href="<%= orderConfirmationCheckoutStepDisplayContext.getOrderDetailURL() %>">
							<aui:button cssClass="btn btn-main btn-primary btn-regular btn-rounded" primary="<%= true %>" style="display: inline;" type="submit" value="go-to-order-details" />
						<a />

						<a href="<%= paymentUtil.goToHomePage() %>">
							<aui:button cssClass="btn btn-main btn-regular btn-rounded btn-secondary" primary="<%= false %>" style="display: inline;" type="" value="back-to-homepage" />
						</a>
					</aui:button-row>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="lfr-ddm__default-page-container failed">
				<h2 class="lfr-ddm__default-page-title">
					<div class="success-message">
						<liferay-ui:message key="payment-was-not-successful" />
					</div>
				</h2>

				<p class="lfr-ddm__default-page-description">
					<liferay-ui:message key="there-is-an-error-in-the-payment-information-please-check" />
				</p>

				<div class="" style="text-align: center;">
						<aui:button-row>
							<a href="<%= paymentUtil.getRetryPaymentURL() %>">
								<aui:button cssClass="btn btn-main btn-primary btn-regular btn-rounded" primary="<%= true %>" style="display: inline;" type="submit" value="retry-payment" />
							</a>

							<a href="<%= paymentUtil.goToHomePage() %>">
								<aui:button cssClass="btn btn-main btn-regular btn-rounded btn-secondary" primary="<%= false %>" style="display: inline;" type="" value="back-to-homepage" />
							</a>
						</aui:button-row>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<script>
    function sendEmail() {
        console.log("inside script");
        console.log("language", themeDisplay.getLanguageId());
        var language = themeDisplay.getLanguageId();
        // fetch('http://localhost:8080/o/greetings/sendEmail?p_auth=nFnpu0SQ', {
        fetch('/o/mirum/sendEmail', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "commerceOrderId": "<%=commerceOrder.getCommerceOrderId()%>",
                "languageId": language
            })
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
    sendEmail();
</script>
