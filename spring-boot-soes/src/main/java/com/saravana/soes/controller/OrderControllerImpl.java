package com.saravana.soes.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.saravana.soes.model.Order;
import com.saravana.soes.service.OrderService;

@Controller
public class OrderControllerImpl implements OrderController {

	@Inject
	private OrderService service;

	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() {
		final Map<String, Object> model = new HashMap<>();
		model.put("orders", this.service.listOrders());
		return new ModelAndView("index", model);
	}

	@Override
	@RequestMapping(name = "/", method = RequestMethod.POST)
	public String placeOrder(@ModelAttribute("order") @Valid Order order, RedirectAttributes redirectAttributes,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", bindingResult);
			redirectAttributes.addFlashAttribute("order", order);
			return "redirect:/";
		}
		this.service.placeOrder(order);
		redirectAttributes.addFlashAttribute("message", "Order with order id " + order.getStockId() + " placed.");
		return "redirect:/";
	}

	@Override
	@RequestMapping("/listOrders")
	public String listOrders() {
		return "redirect:/";
	}

	@Override
	@RequestMapping("/readInput")
	public String parseCSVInput(RedirectAttributes redirectAttributes) {
		this.service.parseCSVInput();
		redirectAttributes.addFlashAttribute("message", "CSV input parsed and placed orders");
		return "redirect:/";
	}

	@Override
	@RequestMapping("/generateOutput")
	public String generateOutput(RedirectAttributes redirectAttributes) {
		this.service.generateCSVOutput();
		redirectAttributes.addFlashAttribute("message", "CSV output generated");
		return "redirect:/";
	}

	@ModelAttribute("order")
	public Order createOrder() {
		return new Order();
	}

	@Override
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource downloadFile() {
		this.service.generateCSVOutput();
		return this.service.downloadFile();
	}

	@Override
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("myFile") MultipartFile myFile, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "File upload successful.");
		this.service.processFile(myFile);
		return "redirect:/";
	}

	@Override
	@ExceptionHandler(Exception.class)
	public ModelAndView globalError(Exception e) {
		final Map<String, Object> model = new HashMap<>();
		model.put("message", "Exception occured, " + e.getMessage());
		return new ModelAndView("error", model);
	}
}