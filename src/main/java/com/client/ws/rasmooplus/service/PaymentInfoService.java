package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.PaymentProcessDto;

public interface PaymentInfoService {

  Boolean process(PaymentProcessDto dto);
}
