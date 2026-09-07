package com.bakery.inventory.service;

import com.bakery.inventory.dto.PersonCreateRequest;
import com.bakery.inventory.dto.PersonResponse;
import com.bakery.inventory.dto.PersonUpdateRequest;

import java.util.List;

public interface PersonService {

    PersonResponse createPerson(PersonCreateRequest request);

    PersonResponse updatePerson(Long personId, PersonUpdateRequest request);

    void deletePerson(Long personId);

    PersonResponse deactivatePerson(Long personId);

    List<PersonResponse> getAllPersons();

    PersonResponse getPersonById(Long personId);

    List<PersonResponse> getByPersonName(String name);
}
