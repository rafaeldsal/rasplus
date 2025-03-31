package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/{id}")
  public ResponseEntity<SubscriptionType> findById(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(subscriptionTypeService.findById(id));
  }

  @PostMapping
  public ResponseEntity<SubscriptionType> create(@RequestBody SubscriptionTypeDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionTypeService.create(dto));
  }
}
