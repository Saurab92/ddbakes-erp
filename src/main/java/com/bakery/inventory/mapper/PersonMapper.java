package com.bakery.inventory.mapper;

import com.bakery.inventory.dto.PersonResponse;
import com.bakery.inventory.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonResponse toResponse(Person person) {
        PersonResponse response = new PersonResponse();
        response.setId(person.getId());
        response.setName(person.getName());
        if (person.getDepartment() != null) {
            response.setDepartmentId(person.getDepartment().getId());
            response.setDepartmentName(person.getDepartment().getName());
        }
        response.setActive(person.getActive());
        response.setCreatedAt(person.getCreatedAt());
        response.setUpdatedAt(person.getUpdatedAt());
        return response;
    }
}
