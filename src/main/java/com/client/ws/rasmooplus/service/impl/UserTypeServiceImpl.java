package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.model.UserType;
import com.client.ws.rasmooplus.repository.UserTypeRepository;
import com.client.ws.rasmooplus.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeServiceImpl implements UserTypeService {

  @Autowired
  private UserTypeRepository userTypeRepository;

  @Override
  public List<UserType> findAll() {
    return userTypeRepository.findAll();
  }
}
