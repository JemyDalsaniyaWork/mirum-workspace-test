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

package com.liferay.commerce.checkout.web.internal.portlet;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.checkout.web.internal.display.context.CheckoutDisplayContext;
import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.CommerceOrderHttpHelper;
import com.liferay.commerce.order.CommerceOrderValidatorRegistry;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.util.CommerceCheckoutStepServicesTracker;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.WorkflowDefinitionLink;
import com.liferay.portal.kernel.model.WorkflowInstanceLink;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService;

import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Vy Bui
 */
@Component(
		enabled = false, immediate = true,
		property = {
				"com.liferay.portlet.add-default-resource=true",
				"com.liferay.portlet.css-class-wrapper=portlet-commerce-checkout",
				"com.liferay.portlet.display-category=commerce",
				"com.liferay.portlet.layout-cacheable=true",
				"com.liferay.portlet.preferences-owned-by-group=true",
				"com.liferay.portlet.private-request-attributes=false",
				"com.liferay.portlet.private-session-attributes=false",
				"com.liferay.portlet.render-weight=50",
				"com.liferay.portlet.scopeable=true",
				"javax.portlet.display-name=Checkout",
				"javax.portlet.expiration-cache=0",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + CommercePortletKeys.COMMERCE_CHECKOUT,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user",
				"javax.portlet.version=3.0"
		},
		service = {CommerceCheckoutPortlet.class, Portlet.class}
)
public class CommerceCheckoutPortlet extends MVCPortlet {

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws IOException, PortletException {

		try {
			actionRequest.setAttribute(
					CommerceCheckoutWebKeys.COMMERCE_ORDER,
					_getCommerceOrder(actionRequest));
		} catch (Exception exception) {
			throw new PortletException(exception);
		}

		super.processAction(actionRequest, actionResponse);
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException {

		try {
			long authenticatedUserId = _getRemoteUserId(renderRequest);
			int paymentStatus = 0;

			CommerceOrder commerceOrder = null;
			if (authenticatedUserId == 0) {

				if (_log.isDebugEnabled()) {
					_log.debug("authenticatedUser id == 0, setting the response to the next http call");
				}

				Map<String, String> paymentResponses = _extractPaymentResponseByHttpServletRequest(_portal.getHttpServletRequest(renderRequest));

				_log.info("payment response size : "+ paymentResponses.size());
				if (!paymentResponses.isEmpty()) {
					_log.info("getting more then 0 responses");
					commerceOrder = _getCommerceOrderCustom(renderRequest);
					_log.info("custom method execution completed...");
				}
//				_portal.getHttpServletResponse(renderResponse).sendRedirect("/c/portal/login");
				_portal.getHttpServletResponse(renderResponse).sendRedirect("/web/jcc/login-page");
			} else {
				_log.info("inside else because user is authenticated !!");
				commerceOrder = _getCommerceOrder(renderRequest);
			}

			if (commerceOrder != null) {

				HttpServletRequest httpServletRequest =
						_portal.getHttpServletRequest(renderRequest);

				Map<String, String> paymentResponses = _extractPaymentResponseByHttpServletRequest(httpServletRequest);
				if(_log.isDebugEnabled()) {
					_log.debug("paymentResponses : " + paymentResponses);
				}

				BigDecimal orderAmount = BigDecimal.valueOf(commerceOrder.getTotal().intValue()).setScale(2);
				_log.info("orderAmount : " + orderAmount);
				String paymentResponseAmount = null;
				BigDecimal bigDecimalNumber = null;
				if (Validator.isNotNull(paymentResponses.get(AMOUNT))) {
					paymentResponseAmount = paymentResponses.get(AMOUNT);
					bigDecimalNumber = new BigDecimal(paymentResponseAmount);
					bigDecimalNumber = bigDecimalNumber.divide(BigDecimal.valueOf(100)).setScale(2);
					_log.info("paymentResponseAmount : " + bigDecimalNumber);
				}

				ExpandoBridge expandoBridgeAmount = commerceOrder.getExpandoBridge();;

				if(Validator.isNotNull(expandoBridgeAmount) && Validator.isNotNull(paymentResponseAmount) && paymentResponses.get(PAYMENT_STATUS).equals("0")) {
					if (!orderAmount.equals(bigDecimalNumber)) {
						paymentResponses.put(PAYMENT_STATUS, String.valueOf(CommerceOrderPaymentConstants.STATUS_FAILED));
						_log.info("inside fail condition");
						if (expandoBridgeAmount.hasAttribute(EXPANDO_PAYMENT_UNSUCCESSFUL)) {
							expandoBridgeAmount.setAttribute(EXPANDO_PAYMENT_UNSUCCESSFUL, "Amount did not match");
						}
					} else {
						if (expandoBridgeAmount.hasAttribute(EXPANDO_PAYMENT_UNSUCCESSFUL)) {
							expandoBridgeAmount.setAttribute(EXPANDO_PAYMENT_UNSUCCESSFUL, "Success");
						}
					}
					commerceOrder.setExpandoBridgeAttributes(expandoBridgeAmount);
				}

				if (!paymentResponses.isEmpty()) {
					if (paymentResponses.get(PAYMENT_STATUS) != null) {
						paymentStatus = Integer.parseInt(paymentResponses.get(PAYMENT_STATUS));

						if (paymentStatus == CommerceOrderConstants.PAYMENT_STATUS_PAID) {
							commerceOrder.setOrderStatus(CommerceOrderConstants.ORDER_STATUS_COMPLETED);
						}
						commerceOrder.setPaymentStatus(Integer.parseInt(paymentResponses.get(PAYMENT_STATUS)));
						renderRequest.setAttribute(
								"paymentStatus", paymentStatus);
					}

					if (paymentResponses.get(PRINTED_NOTE) != null && Validator.isNumber(paymentResponses.get(PRINTED_NOTE))) {
						commerceOrder.setPrintedNote(paymentResponses.get(PRINTED_NOTE));
					}

					if (paymentResponses.get(TRANSACTION_ID) != null) {
						commerceOrder.setTransactionId(paymentResponses.get(TRANSACTION_ID));
					}
					if (authenticatedUserId == 0) {

						ExpandoBridge expandoBridge = commerceOrder.getExpandoBridge();
						_log.info("Had workflow Updated" + expandoBridge.hasAttribute(EXPANDO_WORKFLOW_UPDATE));
						if (expandoBridge.hasAttribute(EXPANDO_WORKFLOW_UPDATE)) {
							expandoBridge.setAttribute(EXPANDO_WORKFLOW_UPDATE, false);
							commerceOrder.setExpandoBridgeAttributes(expandoBridge);
							_log.info("update workflowUpdated expando as false");
						}

						CommerceOrderLocalServiceUtil.updateCommerceOrder(commerceOrder);
					}else {
						_commerceOrderService.updateCommerceOrder(commerceOrder);
					}



				ExpandoBridge expandoBridge = commerceOrder.getExpandoBridge();
				if (paymentResponses.isEmpty() &&
						authenticatedUserId != 0 &&
						expandoBridge.hasAttribute(EXPANDO_WORKFLOW_UPDATE) &&
						expandoBridge.getAttribute(EXPANDO_WORKFLOW_UPDATE).equals(false)) {
					_log.info("Set Expando as true and updating commerce order");
					expandoBridge.setAttribute(EXPANDO_WORKFLOW_UPDATE, true);
					commerceOrder.setExpandoBridgeAttributes(expandoBridge);
					CommerceOrderLocalServiceUtil.updateCommerceOrder(commerceOrder);
				}

				if(Validator.isNotNull(expandoBridge)) {
					if (Validator.isNotNull(paymentResponses.get(FORT_ID)) && expandoBridge.hasAttribute(EXPANDO_PAYMENT_FORT)) {
						expandoBridge.setAttribute(EXPANDO_PAYMENT_FORT, paymentResponses.get(FORT_ID));
					}
					if (Validator.isNotNull(paymentResponses.get(MERCHANT_REFERENCE))&& expandoBridge.hasAttribute(EXPANDO_PAYMENT_MERCHANT_REFERENCE)) {
						expandoBridge.setAttribute(EXPANDO_PAYMENT_MERCHANT_REFERENCE, paymentResponses.get(MERCHANT_REFERENCE));
					}
					commerceOrder.setExpandoBridgeAttributes(expandoBridge);
				}
			}

				HttpServletResponse httpServletResponse =
						_portal.getHttpServletResponse(renderResponse);

				boolean continueAsGuest = GetterUtil.getBoolean(
						CookieKeys.getCookie(
								_portal.getHttpServletRequest(renderRequest),
								CookieKeys.COMMERCE_CONTINUE_AS_GUEST));

				if ((commerceOrder.getCommerceAccountId() ==
						CommerceAccountConstants.ACCOUNT_ID_GUEST) &&
						!continueAsGuest) {

					httpServletResponse.sendRedirect(
							_getCheckoutURL(renderRequest));
				} else if ((commerceOrder.isOpen() &&
						!_isOrderApproved(commerceOrder)) ||
						!_commerceOrderValidatorRegistry.isValid(
								LocaleUtil.getSiteDefault(), commerceOrder)) {

					httpServletResponse.sendRedirect(
							_getOrderDetailsURL(renderRequest));
				} else if (!commerceOrder.isOpen() &&
						(continueAsGuest || commerceOrder.isGuestOrder())) {

					CookieKeys.deleteCookies(
							httpServletRequest, httpServletResponse,
							CookieKeys.getDomain(httpServletRequest),
							CommerceOrder.class.getName() + StringPool.POUND +
									commerceOrder.getGroupId());

					CookieKeys.deleteCookies(
							httpServletRequest, httpServletResponse,
							CookieKeys.getDomain(httpServletRequest),
							CookieKeys.COMMERCE_CONTINUE_AS_GUEST);
				}
				if(_log.isDebugEnabled()) {
					_log.debug("all good : ");
				}
				renderRequest.setAttribute(
						CommerceCheckoutWebKeys.COMMERCE_ORDER, commerceOrder);
			}

			CheckoutDisplayContext checkoutDisplayContext =
					new CheckoutDisplayContext(
							_commerceCheckoutStepServicesTracker,
							_portal.getLiferayPortletRequest(renderRequest),
							_portal.getLiferayPortletResponse(renderResponse), _portal);

			renderRequest.setAttribute(
					"paymentStatus", paymentStatus);
			renderRequest.setAttribute(
					WebKeys.PORTLET_DISPLAY_CONTEXT, checkoutDisplayContext);

			if(_log.isDebugEnabled()) {
				_log.debug("all done with the parameters : ");
			}

			super.render(renderRequest, renderResponse);
		} catch (Exception exception) {
			_log.error("there is an error here: " + exception.getMessage());
			throw new PortletException(exception);
		}
	}

	private Map<String, String> _extractPaymentResponseByHttpServletRequest(HttpServletRequest httpServletRequest) {
		DynamicServletRequest dynamicServletRequest = null;
		Map<String, String[]> dynamicParameterMap = null;
		Map<String, String> paymentResponses = new HashMap<>();

		while (httpServletRequest instanceof HttpServletRequestWrapper) {
			if (httpServletRequest instanceof DynamicServletRequest) {
				dynamicServletRequest =
						(DynamicServletRequest)httpServletRequest;

				HttpServletRequestWrapper httpServletRequestWrapper1 =
						dynamicServletRequest;

				HttpServletRequest httpServletRequest2 =
						(HttpServletRequest)httpServletRequestWrapper1.getRequest();

				dynamicParameterMap = httpServletRequest2.getParameterMap();
				break;
			}

			HttpServletRequestWrapper httpServletRequestWrapper =
					(HttpServletRequestWrapper)httpServletRequest;

			httpServletRequest =
					(HttpServletRequest)httpServletRequestWrapper.getRequest();
		}

		if (dynamicParameterMap != null) {

			if(_log.isDebugEnabled()) {
				_log.debug("Dynamic pamameter map: " + dynamicParameterMap);
			}

			String[] responseCode = null;
			String[] responseMessage = null;
			String[] transactionId = null;
			String[] ddmFormRecordInstanceIds = null;
			String[] paymentStatus = null;
			String[] printedNote = null;
			String[] amount = null;
			String[] merchant_reference = null;
			String[] fort_id = null;


			for (Map.Entry<String,String[]> entry : dynamicParameterMap.entrySet()) {

				if(entry.getKey().equals("amount")){
					amount = entry.getValue();
				}

				if(entry.getKey().equals("merchant_reference")){
					merchant_reference = entry.getValue();
				}

				if(entry.getKey().equals("fort_id")){
					fort_id = entry.getValue();
				}

				if(entry.getKey().equals("response_code")) {
					responseCode = entry.getValue();
				}

				if(entry.getKey().equals("response_message")) {
					responseMessage = entry.getValue();
				}

				if(entry.getKey().equals("fort_id")) {
					transactionId = entry.getValue();
				}

				if(entry.getKey().equals("merchant_extra")) {
					ddmFormRecordInstanceIds = entry.getValue();
				}
				if(entry.getKey().equals("_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_paymentStatus")) {
					paymentStatus = entry.getValue();
				}

				if(entry.getKey().equals("_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_printedNote")) {
					printedNote = entry.getValue();
				}
			}

			if (Validator.isNotNull(responseCode) && Validator.isNotNull(responseMessage)) {

				// success
				if (responseMessage.length > 0 && responseMessage[0].equalsIgnoreCase("success")) {
					paymentResponses.put(PAYMENT_STATUS, String.valueOf(CommerceOrderConstants.PAYMENT_STATUS_PAID));
					if (transactionId != null) {
						paymentResponses.put("transactionId", transactionId[0]);
					}

					if(_log.isDebugEnabled()) {
						_log.debug("success: " + transactionId);
					}

				} else if (responseMessage.length > 0 && !responseMessage[0].equalsIgnoreCase("success")) {

					paymentResponses.put(PAYMENT_STATUS, String.valueOf(CommerceOrderPaymentConstants.STATUS_FAILED));

					if(_log.isDebugEnabled()) {
						_log.debug("failed: " + dynamicParameterMap);
					}
				}
			}

			if (amount != null) {
				_log.info("check amount is null or not!");
				paymentResponses.put(AMOUNT, amount[0]);
			}
			if (merchant_reference != null){
				paymentResponses.put(MERCHANT_REFERENCE,merchant_reference[0]);
			}
			if (fort_id != null){
				paymentResponses.put(FORT_ID,fort_id[0]);
			}
			if (paymentStatus != null) {
				paymentResponses.put(PAYMENT_STATUS, paymentStatus[0]);
			}
			if (printedNote!= null) {
				paymentResponses.put(PRINTED_NOTE, printedNote[0]);
			}

			// extract applicant Id
			if( ddmFormRecordInstanceIds != null) {
				String ddmFormRecordInstanceId = ddmFormRecordInstanceIds[0];
				paymentResponses.put(PRINTED_NOTE, ddmFormRecordInstanceId);
			}
		}
		// Update Status
		return paymentResponses;
	}

	private long _getRemoteUserId(RenderRequest renderRequest) {
		HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(renderRequest);

		return GetterUtil.getLong(httpServletRequest.getRemoteUser());
	}

	private String _getCheckoutURL(PortletRequest portletRequest)
			throws PortalException {

		PortletURL portletURL =
				_commerceOrderHttpHelper.getCommerceCheckoutPortletURL(
						_portal.getHttpServletRequest(portletRequest));

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		return portletURL.toString();
	}

	private String _getCheckoutURLWithOrderUuid(PortletRequest portletRequest, Map<String, String> paymentResponses)
			throws PortalException {

		String commerceOrderUuid = ParamUtil.getString(
				portletRequest, "commerceOrderUuid");

		PortletURL portletURL =
				_commerceOrderHttpHelper.getCommerceCheckoutPortletURL(
						_portal.getHttpServletRequest(portletRequest));

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		int paymentStatus = Integer.parseInt(paymentResponses.get(PAYMENT_STATUS));
		// fail
		portletURL.setParameter("checkoutStepName", "order-confirmation");

		portletURL.setParameter("commerceOrderUuid", commerceOrderUuid);
		portletURL.setParameter(PAYMENT_STATUS, paymentResponses.get(PAYMENT_STATUS));
		portletURL.setParameter(ORDER_STATUS, paymentResponses.get(ORDER_STATUS));
		portletURL.setParameter(PRINTED_NOTE, paymentResponses.get(PRINTED_NOTE));

		return portletURL.toString();
	}

	/* custom method start */
	private CommerceOrder _getCommerceOrderCustom(PortletRequest portletRequest) throws PortalException {

		String commerceOrderUuid = ParamUtil.getString(
				portletRequest, "commerceOrderUuid");
		_log.info("CommerceOrder Id " + commerceOrderUuid);

		long myGroupId = _commerceChannelLocalService.getCommerceChannelGroupIdBySiteGroupId(_portal.getScopeGroupId(portletRequest));

		CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrderByUuidAndGroupId(commerceOrderUuid, myGroupId);
		if(Validator.isNotNull(commerceOrder)) {
			_log.info("Gte commerceorder = " + commerceOrder);
			return  commerceOrder;
		} else {
			_log.info("Null with commerce order");
			return null;
		}

	}

	/*custom method end*/

	private CommerceOrder _getCommerceOrder(PortletRequest portletRequest)
			throws PortalException {

		String commerceOrderUuid = ParamUtil.getString(
				portletRequest, "commerceOrderUuid");

		if (Validator.isNotNull(commerceOrderUuid)) {
			long groupId =
					_commerceChannelLocalService.
							getCommerceChannelGroupIdBySiteGroupId(
									_portal.getScopeGroupId(portletRequest));

			return _commerceOrderService.getCommerceOrderByUuidAndGroupId(
					commerceOrderUuid, groupId);
		}

		return _commerceOrderHttpHelper.getCurrentCommerceOrder(
				_portal.getHttpServletRequest(portletRequest));
	}

	private String _getOrderDetailsURL(PortletRequest portletRequest)
			throws PortalException {

		PortletURL portletURL =
				_commerceOrderHttpHelper.getCommerceCartPortletURL(
						_portal.getHttpServletRequest(portletRequest),
						_getCommerceOrder(portletRequest));

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		return portletURL.toString();
	}

	private boolean _isOrderApproved(CommerceOrder commerceOrder)
			throws PortalException {

		WorkflowInstanceLink workflowInstanceLink =
				_workflowInstanceLinkLocalService.fetchWorkflowInstanceLink(
						commerceOrder.getCompanyId(), commerceOrder.getGroupId(),
						CommerceOrder.class.getName(),
						commerceOrder.getCommerceOrderId());

		if ((workflowInstanceLink != null) &&
				(commerceOrder.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return false;
		}

		WorkflowDefinitionLink workflowDefinitionLink =
				_workflowDefinitionLinkLocalService.fetchWorkflowDefinitionLink(
						commerceOrder.getCompanyId(), commerceOrder.getGroupId(),
						CommerceOrder.class.getName(), 0,
						CommerceOrderConstants.TYPE_PK_APPROVAL, true);


		if ((workflowDefinitionLink != null) &&
				(commerceOrder.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
			CommerceCheckoutPortlet.class);
	private static final String PAYMENT_STATUS = "paymentStatus";
	private static final String ORDER_STATUS = "orderStatus";
	private static final String PRINTED_NOTE = "printedNote";
	private static final String TRANSACTION_ID = "transactionId";
	private static final String AMOUNT = "amount";
	private static final String MERCHANT_REFERENCE = "merchant_reference";
	private static final String FORT_ID = "fort_id";
	private static final String EXPANDO_WORKFLOW_UPDATE = "workflowUpdated";
	private static final String EXPANDO_PAYMENT_FORT = "payment_fort_id";
	private static final String EXPANDO_PAYMENT_MERCHANT_REFERENCE = "payment_merchant_reference";
	private static final String EXPANDO_PAYMENT_UNSUCCESSFUL = "paymentUnsuccessful";

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceCheckoutStepServicesTracker
			_commerceCheckoutStepServicesTracker;

	@Reference
	private CommerceOrderHttpHelper _commerceOrderHttpHelper;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceOrderValidatorRegistry _commerceOrderValidatorRegistry;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private WorkflowDefinitionLinkLocalService
			_workflowDefinitionLinkLocalService;

	@Reference
	private WorkflowInstanceLinkLocalService _workflowInstanceLinkLocalService;

	@Reference
	private CommercePaymentEngine _commercePaymentEngine;
}
