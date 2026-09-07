package com.bakery.inventory.service.impl;

import com.bakery.inventory.dto.PersonCreateRequest;
import com.bakery.inventory.dto.PersonResponse;
import com.bakery.inventory.dto.PersonUpdateRequest;
import com.bakery.inventory.entity.Department;
import com.bakery.inventory.entity.Person;
import com.bakery.inventory.exception.DuplicateResourceException;
import com.bakery.inventory.exception.ResourceInUseException;
import com.bakery.inventory.exception.ResourceNotFoundException;
import com.bakery.inventory.mapper.PersonMapper;
import com.bakery.inventory.repository.DepartmentRepository;
import com.bakery.inventory.repository.IssueRepository;
import com.bakery.inventory.repository.PersonRepository;
import com.bakery.inventory.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;
    private final IssueRepository issueRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository,
                              DepartmentRepository departmentRepository,
                              IssueRepository issueRepository,
                              PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonResponse createPerson(PersonCreateRequest request) {
        if (personRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Person with name '" + request.getName() + "' already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id " + request.getDepartmentId()));

        Person person = new Person();
        person.setName(request.getName());
        person.setDepartment(department);
        person.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);

        Person saved = personRepository.save(person);
        return personMapper.toResponse(saved);
    }

    @Override
    public PersonResponse updatePerson(Long personId, PersonUpdateRequest request) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id " + personId));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(person.getName())) {
            if (personRepository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateResourceException(
                        "Person with name '" + request.getName() + "' already exists");
            }
            person.setName(request.getName());
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department not found with id " + request.getDepartmentId()));
            person.setDepartment(department);
        }

        if (request.getActive() != null) {
            person.setActive(request.getActive());
        }

        Person updated = personRepository.save(person);
        return personMapper.toResponse(updated);
    }

    @Override
    public void deletePerson(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id " + personId));

        if (issueRepository.existsByPersonId(person.getId())) {
            throw new ResourceInUseException(
                    "Person with id " + personId + " cannot be deleted because it is still referenced by issues");
        }

        personRepository.delete(person);
    }

    @Override
    public PersonResponse deactivatePerson(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id " + personId));

        person.setActive(Boolean.FALSE);
        Person updated = personRepository.save(person);
        return personMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id " + personId));
        return personMapper.toResponse(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonResponse> getByPersonName(String name) {
        return personRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(personMapper::toResponse)
                .collect(Collectors.toList());
    }
}
