package com.sr.blog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorInfo {
	@SuppressWarnings("unused")
	private String errorCode;
	@SuppressWarnings("unused")
	private String errorMessage;

	ErrorInfo() {
	}

	ErrorInfo(String errorMessage) {
		super();
		this.errorCode = "-1";
		this.errorMessage = errorMessage;
	}

	ErrorInfo(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public static ErrorInfo toErrorInfo(Throwable t) {
		ErrorInfo errorInfo = new ErrorInfo(t.getMessage());

		return errorInfo;
	}
}