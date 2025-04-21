package com.client.ws.rasmooplus.repository.jpa;

import com.client.ws.rasmooplus.model.jpa.UserPaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaymentInfoRepository extends JpaRepository<UserPaymentInfo, Long> {
}
