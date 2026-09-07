package com.bakery.inventory.controller;

import com.bakery.inventory.dto.PersonCreateRequest;
import com.bakery.inventory.dto.PersonResponse;
import com.bakery.inventory.dto.PersonUpdateRequest;
import com.bakery.inventory.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@Valid @RequestBody PersonCreateRequest request) {
        PersonResponse response = personService.createPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonResponse>> getByPersonName(@RequestParam String name) {
        return ResponseEntity.ok(personService.getByPersonName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long id,
                                                        @Valid @RequestBody PersonUpdateRequest request) {
        PersonResponse response = personService.updatePerson(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PersonResponse> deactivatePerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.deactivatePerson(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
