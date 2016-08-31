package com.saravana.soes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saravana.soes.model.Order;
import com.saravana.soes.model.Side;
import com.saravana.soes.model.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("from STOCK_ORDER so where so.company = ?1 and so.side != ?2 and so.status = ?3 order by stock_id asc")
	List<Order> findOpenOrders(String company, Side side, Status status);

}
