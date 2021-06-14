package com.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.model.Tutorial;
import com.api.rest.repository.TutorialRepository;

@CrossOrigin(origins = "http://localhost:8081")//altera a porta padrão
@RestController //define o projeto como uma api rest
@RequestMapping("/api") 
public class ExampleController {
	
	@Autowired
	TutorialRepository tutorialRepository;
	
	@RequestMapping("/tutorials") //traz todos os tutoriais
	public ResponseEntity<List<Tutorial>> getAllTutorials (@RequestParam(required = false) String title){
		
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();
			
			if(title == null) {
				tutorialRepository.findAll().forEach(tutorials::add);
			}else {
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
			}
			
			if(tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
			
			
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/tutorials/{id}")//traz pelo id
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") String id){
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/tutorials/published")//traz os que foram publicados
	public ResponseEntity<List<Tutorial>> findByPublished(){
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
			
			if(tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<>(tutorials,HttpStatus.OK);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
	}
	
	@PostMapping("/tutorials")//criar um novo tutorial
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial){
		try {
			Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished()));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/tutorials/{id}")//alterar um tutorial
	public ResponseEntity<Tutorial> updateTutorial (@PathVariable("id") String id, @RequestBody Tutorial tutorial){
		
		Optional<Tutorial> tutorialData  = tutorialRepository.findById(id);
		if(tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@DeleteMapping("/tutorials/{id}")//deleta pelo id
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") String id){
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials(){
		try{
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}
