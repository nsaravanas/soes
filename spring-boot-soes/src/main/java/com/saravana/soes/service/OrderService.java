package com.saravana.soes.service;

import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import com.saravana.soes.model.Order;

public interface OrderService {

	boolean placeOrder(Order order);

	List<Order> listOrders();

	boolean placeOrders(List<Order> orders);

	boolean parseCSVInput();

	boolean generateCSVOutput();

	Order getOrder(Integer id);

	boolean processFile(MultipartFile myFile);

	FileSystemResource downloadFile();

}
