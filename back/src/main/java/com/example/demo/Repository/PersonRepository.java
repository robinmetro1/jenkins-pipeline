package com.example.demo.Repository;

import com.example.demo.Models.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person,Long>{

}
