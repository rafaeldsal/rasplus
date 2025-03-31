package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.model.SubscriptionType;
import com.client.ws.rasmooplus.repository.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionTypeServiceImpl implements SubscriptionTypeService {

  private final SubscriptionTypeRepository subscriptionTypeRepository;

  SubscriptionTypeServiceImpl(SubscriptionTypeRepository subscriptionTypeRepository) {
    this.subscriptionTypeRepository = subscriptionTypeRepository;
  }

  @Override
  public List<SubscriptionType> readAll() {
    return subscriptionTypeRepository.findAll();
  }

  @Override
  public SubscriptionType findById(Long id) {
    Optional<SubscriptionType> optionalSubscriptionType = subscriptionTypeRepository.findById(id);

    if (optionalSubscriptionType.isEmpty()) {
      throw new NotFoundException("SubscriptionType não encontrado.");
    }

    return optionalSubscriptionType.get();
  }

  @Override
  public SubscriptionType create(SubscriptionTypeDto subscriptionTypeDto) {
    return subscriptionTypeRepository.save(SubscriptionType.builder()
        .id(subscriptionTypeDto.id())
        .name(subscriptionTypeDto.name())
        .accessMonths(subscriptionTypeDto.accessMonths())
        .price(subscriptionTypeDto.price())
        .productKey(subscriptionTypeDto.productKey())
        .build());
  }

  @Override
  public SubscriptionType update(Long id, SubscriptionType subscriptionType) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }
}
