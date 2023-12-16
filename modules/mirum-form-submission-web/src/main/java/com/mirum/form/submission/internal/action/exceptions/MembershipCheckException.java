package com.mirum.form.submission.internal.action.exceptions;

import com.liferay.portal.kernel.exception.PortalException;

public class MembershipCheckException extends PortalException {

	public MembershipCheckException() {
	}

	public MembershipCheckException(String msg) {
		super(msg);
	}

	public MembershipCheckException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public MembershipCheckException(Throwable throwable) {
		super(throwable);
	}

}