package net.jeeshop.services.common;

import java.util.Map;

public class telTradeRequestVO {
	private String subtransNo = "";
	private String transDate;
	private String transType;
	private pageRecord pageRecord;
	private Map dataTranArea;
	private String transTime;
	private String transNo;
    
	public String getSubtransNo() {
		return subtransNo;
	}

	public void setSubtransNo(String subtransNo) {
		this.subtransNo = subtransNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public pageRecord getPageRecord() {
		return pageRecord;
	}

	public void setPageRecord(pageRecord pageRecord) {
		this.pageRecord = pageRecord;
	}

	public Map getDataTranArea() {
		return dataTranArea;
	}

	public void setDataTranArea(Map dataTranArea) {
		this.dataTranArea = dataTranArea;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

}
