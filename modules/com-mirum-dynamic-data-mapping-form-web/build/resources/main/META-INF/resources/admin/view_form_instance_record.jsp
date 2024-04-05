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

<%@ include file="/admin/init.jsp" %>
<%
String redirect = ParamUtil.getString(request, "redirect");

DDMFormInstanceRecordVersion formInstanceRecordVersion = ddmFormAdminDisplayContext.getDDMFormInstanceRecordVersion();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(LanguageUtil.get(request, "view-form"));
%>

<clay:container-fluid>
	<c:if test="<%= formInstanceRecordVersion != null %>">
		<aui:model-context bean="<%= formInstanceRecordVersion %>" model="<%= DDMFormInstanceRecordVersion.class %>" />

		<div class="panel text-center">
			<c:choose>
                <c:when test ="<%= formInstanceRecordVersion.getStatus() == 100 %>">
                    <span class="taglib-workflow-status">
                        <span class="mr-2 workflow-version">
                            <span class="workflow-label">Version:</span>
                            <span class="workflow-value"><%= formInstanceRecordVersion.getVersion() %></span>
                        </span>
                        <span class="workflow-status">
                            <span class="label status workflow-value" style="background-color: #e7e7e6; border-color: #9499cd; color: #bb892d;">
                                PAYMENT PAID
                            </span>
                        </span>
                    </span>
                </c:when>
                <c:when test = "<%= formInstanceRecordVersion.getStatus() == 101 %>">
                    <span class="taglib-workflow-status">
                        <span class="mr-2 workflow-version">
                            <span class="workflow-label">Version:</span>
                            <span class="workflow-value"><%= formInstanceRecordVersion.getVersion() %></span>
                        </span>
                        <span class="workflow-status">
                            <span class="label status workflow-value" style="background-color: #fdfdfd; border-color: #ffffff; color: #781212;">
                                PAYMENT FAILED
                            </span>
                        </span>
                    </span>
                </c:when>
                <c:otherwise>
                    <aui:workflow-status markupView="lexicon" model="<%= DDMFormInstanceRecord.class %>" showHelpMessage="<%= false %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= formInstanceRecordVersion.getStatus() %>" version="<%= formInstanceRecordVersion.getVersion() %>" />
                </c:otherwise>
            </c:choose>
		</div>
	</c:if>
</clay:container-fluid>

<clay:container-fluid
	cssClass="ddm-form-builder-app form-entry"
>
	<react:component
		module="admin/js/FormView"
		props="<%= ddmFormAdminDisplayContext.getDDMFormContext(renderRequest) %>"
	/>
</clay:container-fluid>
