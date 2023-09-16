package com.example.demo.Controller;

import com.example.demo.Models.Person;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public List<Person> getAllPatients() {
        return personService.getAllPatients();
    }

    @PostMapping("/add")
    public void addPatient(@RequestBody Person person) {
        personService.addPatient(person);
    }


}
