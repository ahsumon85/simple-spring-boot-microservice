/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.product.rest.common.messages;

public class BaseResponse {
	
	private String message;

	private int code;

	public BaseResponse(String message, int code) {
		this.message = message;
		this.code = code;
	}

	BaseResponse() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
