package me.youxiong.person.model;

public class ReturnJsonModel {
	
	private boolean IsSuccess;
	private int StatusCode;
	private String Description;
	private String ReturnValue;
	
	public boolean getIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}
	public int getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getReturnValue() {
		return ReturnValue;
	}
	public void setReturnValue(String returnValue) {
		ReturnValue = returnValue;
	}
	@Override
	public String toString() {
		return "ReturnJsonModel [IsSuccess=" + IsSuccess + ", StatusCode=" + StatusCode + ", Description="
				+ Description + ", ReturnValue=" + ReturnValue + "]";
	}
	
}
