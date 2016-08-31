package com.saravana.soes.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.saravana.soes.model.Order;

public interface OrderController {

	String listOrders();

	String parseCSVInput(RedirectAttributes redirectAttributes);

	String generateOutput(RedirectAttributes redirectAttributes);

	ModelAndView home();

	String placeOrder(Order order, RedirectAttributes redirectAttributes, BindingResult bindingResult);

	ModelAndView globalError(Exception e);

	FileSystemResource downloadFile();

	String uploadFile(MultipartFile myFile, RedirectAttributes redirectAttributes);

}
