package com.maybank.platform.services.restapi.payload.response;

import lombok.Value;

@Value
public class ApiResponse {
	private Boolean success;
	private String message;
}