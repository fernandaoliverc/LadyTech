package com.elastech.LadyTech.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.elastech.LadyTech.models.Called;
import com.elastech.LadyTech.models.Technical;
import com.elastech.LadyTech.repositories.CalledRepository;
import com.elastech.LadyTech.repositories.TechnicalRepository;

@Controller
@RequestMapping("/called")
public class CalledController {

	@Autowired
	private TechnicalRepository technicalRepository;

	@Autowired
	private CalledRepository calledRepository;

	@GetMapping("/create-called")
	private String getCalled(Model model){
		model.addAttribute("called", new Called());
		return "usuario-novo-chamado";
	}
	@PostMapping("/create-called")
	public String createCalled(@ModelAttribute("called") Called called, Model model) {
		Long idTechnical = called.getTechnical().getIdTechnical();

		Optional<Technical> technical = technicalRepository.findById(idTechnical);

		if (technical.isEmpty()) {
			model.addAttribute("error", "Chamado não encontrado.");
			return "redirect:/called/create-called";
		} else {
			String technicalName = technical.get().getName();

			called.setTechnicalName(technicalName);
			calledRepository.save(called);
			return "redirect:/called/create-called";

		}
	}
	
	@GetMapping("/consult-called")
	private String getAllCalled(Model model) {
		List<Called> called = calledRepository.findAll();
		model.addAttribute("chamados", called);
		return "administrador-tela-inicial";
	}
	@GetMapping("/byId/{idCalled}")
	private Called getCalledById(@PathVariable long idCalled) {
		return calledRepository.findById(idCalled).orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
	}

	
	@PatchMapping("update/status/{idCalled}")
	private Called updateStatus(@PathVariable long idCalled, @RequestBody Called calledUpdate) {
		Called called = calledRepository.findById(idCalled)
				.orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
		// set em cada um dos atributos autalizados com novo valor do tecnicoupdate
		called.setStatus(calledUpdate.getStatus());
		return calledRepository.save(called);
	}

	@PatchMapping("update/priority/{idCalled}")
	private Called updatePriority(@PathVariable long idCalled, @RequestBody Called calledUpdate) {
		Called called = calledRepository.findById(idCalled)
				.orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
		// set em cada um dos atributos autalizados com novo valor do tecnicoupdate
		called.setPriority(calledUpdate.getPriority());
		return calledRepository.save(called);
	}

}
