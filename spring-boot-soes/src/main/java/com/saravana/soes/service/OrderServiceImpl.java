package com.saravana.soes.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.saravana.soes.controller.OrderControllerImpl;
import com.saravana.soes.engine.OrderProcessingEngine;
import com.saravana.soes.io.util.CSVUtil;
import com.saravana.soes.model.Order;
import com.saravana.soes.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);

	@Inject
	private OrderRepository repository;

	@Inject
	private OrderProcessingEngine engine;

	@Value("${input.filename}")
	private String inputFilename;

	@Value("${output.filename}")
	private String outputFilename;

	@Override
	@Transactional
	public boolean placeOrder(Order order) {
		boolean isSuccess = false;
		try {
			this.repository.save(order);
			isSuccess = true;
		} catch (DataAccessException dae) {
			LOG.error("Exception occured while persisting with order id " + order.getStockId() + " Exception "
					+ dae.getMessage());
			isSuccess = false;
		}
		if (isSuccess) {
			this.engine.processOrder(order);
		}
		return isSuccess;
	}

	@Override
	@Transactional
	public List<Order> listOrders() {
		return this.repository.findAll();
	}

	@Override
	@Transactional
	public boolean placeOrders(List<Order> orders) {
		for (Order order : orders) {
			placeOrder(order);
		}
		return true;
	}

	@Override
	@Transactional
	public Order getOrder(Integer id) {
		return this.repository.findOne(id);
	}

	public boolean parseCSVInput(File inputFile) {
		return false;

	}

	@Override
	public boolean parseCSVInput() {
		try {
			this.placeOrders(CSVUtil.readFile(this.inputFilename));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean generateCSVOutput() {
		try {
			CSVUtil.writeFile(listOrders(), this.outputFilename);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean processFile(MultipartFile myFile) {
		try {
			this.placeOrders(CSVUtil.readFile(myFile.getInputStream()));
			return true;
		} catch (IOException e) {
			LOG.error("Error while reading File " + e);
		}
		return false;
	}

	@Override
	public FileSystemResource downloadFile() {
		final ClassLoader classLoader = OrderControllerImpl.class.getClassLoader();
		return new FileSystemResource(classLoader.getResource(this.outputFilename).getFile());
	}

}
