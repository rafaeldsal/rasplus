package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscription-type")
public class SubscriptionTypeController {

  @Autowired
  private SubscriptionTypeService subscriptionTypeService;

  @GetMapping
  public ResponseEntity<List<SubscriptionType>> readAll() {
    return ResponseEntity.status(HttpStatus.OK).body(subscriptionTypeService.readAll());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionType> findById(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(subscriptionTypeService.findById(id));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionType> create(@Valid @RequestBody SubscriptionTypeDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionTypeService.create(dto));
  }

  @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubscriptionType> update(@PathVariable("id") Long id, @Valid @RequestBody SubscriptionTypeDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(subscriptionTypeService.update(id, dto));
  }

  @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    subscriptionTypeService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
