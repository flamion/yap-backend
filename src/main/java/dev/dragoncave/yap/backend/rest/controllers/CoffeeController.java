package dev.dragoncave.yap.backend.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeController {

	@GetMapping("/coffee")
	public ResponseEntity<?> brewCoffee() {
		return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
	}
}
