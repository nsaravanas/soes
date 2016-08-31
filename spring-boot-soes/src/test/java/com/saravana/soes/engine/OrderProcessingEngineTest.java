package com.saravana.soes.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.saravana.soes.StockOrderExecutionSystem;
import com.saravana.soes.model.Order;
import com.saravana.soes.model.Side;
import com.saravana.soes.model.Status;
import com.saravana.soes.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StockOrderExecutionSystem.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderProcessingEngineTest {

	@Inject
	private OrderService service;

	@Test
	public void testOrder() {

		Order o = new Order(1, Side.BUY, "ABC Inc", 100L);
		this.service.placeOrder(o);

		assertNotNull(this.service.getOrder(o.getStockId()));
		assertEquals(1, this.service.listOrders().size());

		Order pOrder = this.service.getOrder(o.getStockId());
		assertEquals(Status.OPEN, pOrder.getStatus());

	}

	@Test
	public final void testProcessingEngine() {

		Order o = new Order(1, Side.BUY, "ABC Inc", 100L);
		this.service.placeOrder(o);

		Order o1 = new Order(2, Side.SELL, "ABC Inc", 50L);
		this.service.placeOrder(o1);

		assertEquals(Status.OPEN, this.service.getOrder(o.getStockId()).getStatus());
		assertEquals(Status.CLOSED, this.service.getOrder(o1.getStockId()).getStatus());

		Order o2 = new Order(3, Side.SELL, "ABC Inc", 50L);
		this.service.placeOrder(o2);

		assertEquals(Status.CLOSED, this.service.getOrder(o1.getStockId()).getStatus());
		assertEquals(Status.CLOSED, this.service.getOrder(o2.getStockId()).getStatus());

	}

}
