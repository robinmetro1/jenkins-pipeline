package com.example.demo.Service;

import com.example.demo.Models.Person;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private final List<Person> patients = new ArrayList<>();

    public List<Person> getAllPatients() {
        return patients;
    }

    public void addPatient(Person person) {
        patients.add(person);
    }
}
