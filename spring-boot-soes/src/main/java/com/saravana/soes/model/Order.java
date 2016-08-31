package com.saravana.soes.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity(name = "STOCK_ORDER")
@Table(name = "STOCK_ORDER")
public class Order {

	@Id
	@NotNull
	@Column(name = "STOCK_ID")
	private Integer stockId;

	@NotNull
	@Column(name = "SIDE")
	@Enumerated(EnumType.STRING)
	private Side side;

	@NotNull
	@Column(name = "COMPANY")
	private String company;

	@NotNull
	@DecimalMin(inclusive = false, value = "0")
	@Column(name = "QUANTITY")
	private Long quantity;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status = Status.OPEN;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVED_TIME")
	private Date receivedTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_TIME")
	private Date updatedTime;

	@Column(name = "REMAINING_QUANTITY")
	private Long remainingQuantity;

	@Version
	private Long version = 0L;

	public Order() {
		super();
	}

	public Order(Integer stockId, Side side, String company, Long quantity) {
		super();
		this.stockId = stockId;
		this.side = side;
		this.company = company;
		this.quantity = quantity;
		this.remainingQuantity = quantity;
		this.receivedTime = new Date();

	}

	public Long getVersion() {
		return version;
	}

	public Long getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
		setRemainingQuantity(quantity);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "Order [stockId=" + stockId + ", side=" + side + ", company=" + company + ", quantity=" + quantity
				+ ", status=" + status + "]";
	}
}
