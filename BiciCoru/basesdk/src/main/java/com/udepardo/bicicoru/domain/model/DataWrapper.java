//package com.udepardo.bicicoru.domain.model;
//
//import org.json.JSONException;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
//import javax.security.auth.login.LoginException;
//
///**
// * Created by ude on 01/08/2017.
// */
//
//public class DataWrapper <T> {
//	public Long getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(Long timestamp) {
//		this.timestamp = timestamp;
//	}
//
//	public enum Status {
//		SUCCESS,
//		NETWORK_ERROR,
//		WRONG_CREDENTIALS,
//		SERVER_ERROR,
//		ERROR_PARSING,
//		UNKNOWN,
//	}
//
//	private Long timestamp;
//
//	private T data;
//	private Status status; //or A message String, Or whatever
//
//	public T getData() {
//		return data;
//	}
//
//	public void setData(T data) {
//		this.data = data;
//	}
//
//	public Status getStatus() {
//		return status;
//	}
//
//	public void setStatus(Status status) {
//		this.status = status;
//	}
//
//	public void setError(Exception error) {
//		if (error instanceof UnknownHostException) {
//			status = Status.NETWORK_ERROR;
//		} else if (error instanceof JSONException){
//			status = Status.ERROR_PARSING;
//		} else if (error instanceof IOException){
//			status = Status.SERVER_ERROR;
//		} else if (error instanceof LoginException){
//			status = Status.WRONG_CREDENTIALS;
//		} else {
//			status = Status.UNKNOWN;
//		}
//	}
//}
