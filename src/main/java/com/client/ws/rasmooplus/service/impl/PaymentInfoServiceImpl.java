package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.enums.UserTypeEnum;
import com.client.ws.rasmooplus.exception.BusinessException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.integration.WsRaspayIntegration;
import com.client.ws.rasmooplus.mapper.CreditCardMapper;
import com.client.ws.rasmooplus.mapper.UserPaymentInfoMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.OrderMapper;
import com.client.ws.rasmooplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.rasmooplus.model.User;
import com.client.ws.rasmooplus.model.UserCredentials;
import com.client.ws.rasmooplus.model.UserPaymentInfo;
import com.client.ws.rasmooplus.model.UserType;
import com.client.ws.rasmooplus.repository.SubscriptionTypeRepository;
import com.client.ws.rasmooplus.repository.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.UserPaymentInfoRepository;
import com.client.ws.rasmooplus.repository.UserRepository;
import com.client.ws.rasmooplus.repository.UserTypeRepository;
import com.client.ws.rasmooplus.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

  @Value("${webservices.rasplus.default.password}")
  private String defaultPass;

  private final UserRepository userRepository;
  private final UserPaymentInfoRepository userPaymentInfoRepository;
  private final WsRaspayIntegration wsRaspayIntegration;
  private final MailIntegration mailIntegration;
  private final UserDetailsRepository userDetailsRepository;
  private final UserTypeRepository userTypeRepository;
  private final SubscriptionTypeRepository subscriptionTypeRepository;

  PaymentInfoServiceImpl(UserRepository userRepository,
                         UserPaymentInfoRepository userPaymentInfoRepository,
                         WsRaspayIntegration wsRaspayIntegration,
                         MailIntegration mailIntegration,
                         UserDetailsRepository userDetailsRepository,
                         UserTypeRepository userTypeRepository,
                         SubscriptionTypeRepository subscriptionTypeRepository) {
    this.userRepository = userRepository;
    this.userPaymentInfoRepository = userPaymentInfoRepository;
    this.wsRaspayIntegration = wsRaspayIntegration;
    this.mailIntegration = mailIntegration;
    this.userDetailsRepository = userDetailsRepository;
    this.userTypeRepository = userTypeRepository;
    this.subscriptionTypeRepository = subscriptionTypeRepository;
  }

  @Override
  public Boolean process(PaymentProcessDto dto) {

    var userOpt = userRepository.findById(dto.userPaymentInfoDto().userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    if (Objects.nonNull(userOpt.getSubscriptionType())) {
      throw new BusinessException("Pagamento não pode ser processado pois usuário já possui assinatura");
    }

    Boolean successPayment = getSuccessPayment(dto, userOpt);

    return createUserCredentials(dto, successPayment, userOpt);
  }

  private boolean createUserCredentials(PaymentProcessDto dto, Boolean successPayment, User userOpt) {
    if (Boolean.TRUE.equals(successPayment)) {
      UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(dto.userPaymentInfoDto(), userOpt);
      userPaymentInfoRepository.save(userPaymentInfo);

      UserType userType = userTypeRepository.findById(UserTypeEnum.ALUNO.getId())
          .orElseThrow(() -> new NotFoundException("UserType não encontrado"));

      UserCredentials userCredentials = new UserCredentials(null, userOpt.getEmail(), new BCryptPasswordEncoder().encode(defaultPass), userType);
      userDetailsRepository.save(userCredentials);

      var subscriptionType = subscriptionTypeRepository.findByProductKey(dto.productKey())
              .orElseThrow(() -> new NotFoundException("SubscriptionType não encontrada"));

      userOpt.setSubscriptionType(subscriptionType);
      userRepository.save(userOpt);

      mailIntegration.send(userOpt.getEmail(), message(userOpt), "Acesso Liberado");

      return true;
    }
    return false;
  }

  private Boolean getSuccessPayment(PaymentProcessDto dto, User userOpt) {
    CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(userOpt));
    OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.id(), dto));
    PaymentDto paymentDto = PaymentMapper.build(customerDto.id(),
        orderDto.id(),
        CreditCardMapper.build(dto.userPaymentInfoDto(), userOpt.getCpf()));
    return wsRaspayIntegration.processPayment(paymentDto);
  }

  private String message(User user) {
    StringBuilder sb = new StringBuilder();

    sb.append("Olá ").append(user.getName()).append(",\n\n");
    sb.append("Seja bem-vindo(a) à RasmooPlus!\n\n");
    sb.append("Aqui estão seus dados de acesso:\n");
    sb.append("Login: ").append(user.getEmail()).append("\n");
    sb.append("Senha: ").append(defaultPass).append("\n\n");
    sb.append("Acesse a plataforma pelo link abaixo:\n");
    sb.append("https://www.sua-plataforma.com/login\n\n");
    sb.append("Em caso de dúvidas, estamos a disposição.\n");
    sb.append("Atenciosamente,\n");
    sb.append("Rasmoo Plus");

    return sb.toString();
  }
}
