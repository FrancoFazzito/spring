package com.bolsadeideas.springboot.datajpa.app.controllers;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.datajpa.app.models.entity.Cliente;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Factura;
import com.bolsadeideas.springboot.datajpa.app.models.entity.Producto;
import com.bolsadeideas.springboot.datajpa.app.models.service.IclienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	private final org.slf4j.Logger LOG = LoggerFactory.getLogger(getClass());
	@Autowired
	private IclienteService clienteService;
	
	@GetMapping("form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId,Model model,RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(clienteId);
		
		if(cliente == null){
			flash.addFlashAttribute("error","el cliente no existe en la BD");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.addAttribute("factura",factura);
		model.addAttribute("titulo","crear factura");
		model.addAttribute("productos",clienteService.findAllProducts());
		return "factura/form";
	}
	
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		
		LOG.debug("entrando consulta");
		var lista = clienteService.findByNombre(term);
		for (Producto producto : lista) {
			LOG.debug(producto.getNombre());
		}
		return lista;
	}
}