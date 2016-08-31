package com.saravana.soes.engine;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.saravana.soes.model.Order;
import com.saravana.soes.model.Status;
import com.saravana.soes.repository.OrderRepository;

@Service
public class OrderProcessingEngineImpl implements OrderProcessingEngine {

	@Inject
	private OrderRepository repository;

	@Override
	public void processOrder(Order order) {
		List<Order> orders = this.repository.findOpenOrders(order.getCompany(), order.getSide(), Status.OPEN);
		Long order_quantity = order.getRemainingQuantity();
		for (Order o : orders) {
			if (order_quantity > 0) {
				Long o_quantity = o.getRemainingQuantity();
				if (order_quantity == o_quantity) {
					order_quantity = 0L;
					o_quantity = 0L;
					updateRemainingQuantityStatus(order_quantity, order, o_quantity, o);
				} else if (order_quantity < o_quantity) {
					o_quantity -= order_quantity;
					order_quantity = 0L;
					updateRemainingQuantityStatus(order_quantity, order, o_quantity, o);
				} else {
					order_quantity -= o_quantity;
					o_quantity = 0L;
					updateRemainingQuantityStatus(order_quantity, order, o_quantity, o);
				}
			}
		}
	}

	private void updateRemainingQuantityStatus(Long order_quantity, Order order, Long o_quantity, Order o) {

		order.setRemainingQuantity(order_quantity);
		if (order_quantity == 0)
			order.setStatus(Status.CLOSED);
		order.setUpdatedTime(new Date());

		o.setRemainingQuantity(o_quantity);
		if (o_quantity == 0)
			o.setStatus(Status.CLOSED);
		o.setUpdatedTime(new Date());

		this.repository.save(order);
		this.repository.save(o);
	}

}
