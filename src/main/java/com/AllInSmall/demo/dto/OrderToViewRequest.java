package com.AllInSmall.demo.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.AllInSmall.demo.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderToViewRequest implements Comparable<OrderToViewRequest> {
	
	int Id;
	String customerInfo;
	String orderNote;
	String paymentMethod;
	double totalValue;
	OrderStatus status;
	private LocalDateTime orderDateTime;
	@Override
	public int compareTo(OrderToViewRequest o) {
	    // Define the custom order of statuses using the enum
	    List<OrderStatus> statusOrder = Arrays.asList(OrderStatus.PLACED, OrderStatus.READY, OrderStatus.CANCELLED, OrderStatus.COMPLETED);

	    // Get the index of the current order's status and the other order's status
	    int thisStatusIndex = statusOrder.indexOf(this.getStatus());
	    int otherStatusIndex = statusOrder.indexOf(o.getStatus());

	    // Compare by status index to ensure "Placed" and "Ready" are on top
	    int statusComparison = Integer.compare(thisStatusIndex, otherStatusIndex);
	    if (statusComparison != 0) {
	        return statusComparison;
	    }

	    // If statuses are the same, compare by orderDateTime
	    int dateTimeComparison = this.getOrderDateTime().compareTo(o.getOrderDateTime());
	    if (dateTimeComparison != 0) {
	        return dateTimeComparison;
	    }

	    // If both status and orderDateTime are the same, compare by id
	    return Integer.compare(this.getId(), o.getId());
	}

}
