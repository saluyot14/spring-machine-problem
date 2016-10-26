package com.qbryx.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qbryx.domain.InventoryProduct;
import com.qbryx.exception.DuplicateProductException;
import com.qbryx.service.ManagerService;
import com.qbryx.service.ProductService;
import com.qbryx.util.InventoryProductBuilder;
import com.qbryx.util.Validator;

@Controller
@RequestMapping("/manager")
public class ManagementController {

	@Resource(name = "managerService")
	private ManagerService managerService;

	@Resource(name = "productService")
	private ProductService productService;

	@RequestMapping()
	public String management(Model model) {

		model.addAttribute("activeTab", 1);
		return "management";
	}

	@RequestMapping("/search")
	public String search(@RequestParam(value = "upc") String upc, Model model) {

		boolean productNotFound = false; // remove
		InventoryProduct product = managerService.getProduct(upc);

		if (product == null) {
			productNotFound = true;
		}

		model.addAttribute("product", product);
		model.addAttribute("productNotFound", productNotFound);
		model.addAttribute("activeTab", 1);
		return "management";
	}

	/*@RequestMapping("/createProduct")
	public String createProduct(Model model, @RequestParam(value = "name") String name,
			@RequestParam(value = "upc") String upc, @RequestParam(value = "category") String categoryName,
			@RequestParam(value = "description") String description, @RequestParam(value = "price") BigDecimal price,
			@RequestParam(value = "stock") int stock) {

		model.addAttribute("activeTab", 3);

		if (Validator.invalidUpcFormat(upc)) {

			model.addAttribute("invalidFormat", true);
			return "management";
		}

		Category category = productService.getCategory(categoryName);

		Product product = new Product(upc, category, name, description, price);
		InventoryProduct inventoryProduct = new InventoryProduct(product, stock);

		try {

			managerService.add(inventoryProduct);
			model.addAttribute("productCreated", true);
		} catch (DuplicateProductException e) {

			model.addAttribute("upc", upc);
			model.addAttribute("duplicateProduct", true);
		}

		return "management";
	}*/
	
	@RequestMapping("/createProduct")
	public String createProduct(@ModelAttribute("inventoryProductBuilder") @Valid InventoryProductBuilder inventoryProductBuilder,
			BindingResult bindingResult, Model model) {
		
		model.addAttribute("activeTab", 3);
		
		if(bindingResult.hasErrors()){
			
			model.addAttribute("errorMessage", "Please check your upc. UPC must contain numbers only and 12 digits long");
			return "management";
		}
		
		InventoryProduct inventoryProduct = inventoryProductBuilder.getInventoryProduct(productService);

		try {

			managerService.add(inventoryProduct);
			model.addAttribute("productCreated", true);
		} catch (DuplicateProductException e) {

			model.addAttribute("upc", inventoryProduct.getProduct().getUpc());
			model.addAttribute("duplicateProduct", true);
		}

		return "management";
	}
	
	@RequestMapping("/productByCategory")
	public String prodByCat(Model model, @RequestParam(value = "category", required = false) String category) {

		model.addAttribute("activeTab", 2);

		if (category != null) {
			model.addAttribute("products", productService.getProductsByCategory(category));
		}

		return "management";
	}

	@RequestMapping("/viewProduct")
	public String viewProduct(Model model, @RequestParam(value = "upc") String upc) {

		InventoryProduct product = managerService.getProduct(upc);

		model.addAttribute("product", product);
		return "update_product";
	}

	/*@RequestMapping("/updateProduct")
	public String updateProduct(Model model, @RequestParam(value = "name") String name,
			@RequestParam(value = "upc") String upc, @RequestParam(value = "category") String categoryName,
			@RequestParam(value = "description") String description, @RequestParam(value = "price") BigDecimal price,
			@RequestParam(value = "stock") int stock) {

		Category category = productService.getCategory(categoryName);

		Product product = new Product(upc, category, name, description, price);
		InventoryProduct inventoryProduct = new InventoryProduct(product, stock);

		managerService.update(inventoryProduct);

		model.addAttribute("activeTab", 2);
		model.addAttribute("productUpdated", true);
		return "management";
	}*/
	
	@RequestMapping("/updateProduct")
	public String updateProduct(@ModelAttribute("inventoryProductBuilder") @Valid InventoryProductBuilder inventoryProductBuilder,
			Model model) {
		
		InventoryProduct inventoryProduct = inventoryProductBuilder.getInventoryProduct(productService);
				
		managerService.update(inventoryProduct);

		model.addAttribute("activeTab", 2);
		model.addAttribute("productUpdated", true);
		return "management";
	}
}
