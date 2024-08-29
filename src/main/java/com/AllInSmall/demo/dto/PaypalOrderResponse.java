package com.AllInSmall.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class PaypalOrderResponse {
	private String id;
	private String status;
	private List<PurchaseUnit> purchaseUnits;
	private List<Link> links;
	
	@Data
	public static class PurchaseUnit{
		private Amount amount;
	}
	
	@Data
	public static class Amount{
		private String currencyCode;
		private String value;
	}
	
	@Data
	public static class Link{
		private String href;
		private String rel;
		private String method;
	}
	
	public String getApprovalUrl() {
		for(Link link : links) {
			if("approve".equals(link.getRel())) {
				return link.getHref();
			}
		}
		return null;
	}
	

}
