package com.geo4net.main.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {
	 
	private static final long serialVersionUID = 1L;
	private String exceptionDetail;
	    private Object fieldValue;

	    public RecordNotFoundException( String exceptionDetail, String fieldValue) {
	        super(exceptionDetail+" - "+fieldValue);
	        this.exceptionDetail = exceptionDetail;
	        this.fieldValue = fieldValue;
	    }

	    public String getExceptionDetail() {
	        return exceptionDetail;
	    }

	    public Object getFieldValue() {
	        return fieldValue;
	    }
	}


