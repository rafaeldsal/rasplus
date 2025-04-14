package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.PaymentProcessDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
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
import com.client.ws.rasmooplus.model.UserPaymentInfo;
import com.client.ws.rasmooplus.repository.UserPaymentInfoRepository;
import com.client.ws.rasmooplus.repository.UserRepository;
import com.client.ws.rasmooplus.service.PaymentInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

  private final UserRepository userRepository;
  private final UserPaymentInfoRepository userPaymentInfoRepository;
  private final WsRaspayIntegration wsRaspayIntegration;
  private final MailIntegration mailIntegration;

  PaymentInfoServiceImpl(UserRepository userRepository,
                         UserPaymentInfoRepository userPaymentInfoRepository,
                         WsRaspayIntegration wsRaspayIntegration,
                         MailIntegration mailIntegration) {
    this.userRepository = userRepository;
    this.userPaymentInfoRepository = userPaymentInfoRepository;
    this.wsRaspayIntegration = wsRaspayIntegration;
    this.mailIntegration = mailIntegration;
  }

  @Override
  public Boolean process(PaymentProcessDto dto) {

    // Verifica usuário por id e verifica se já existe assinatura
    var userOpt = userRepository.findById(dto.userPaymentInfoDto().userId())
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    if (Objects.nonNull(userOpt.getSubscriptionType())) {
      throw new BusinessException("Pagamento não pode ser processado pois usuário já possui assinatura");
    }

    // Criar ou atualiza usuário raspay
    CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(userOpt));
    // cria o pedido de pagamento
    OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.id(), dto));

    // Processa o pagamento
    PaymentDto paymentDto = PaymentMapper.build(customerDto.id(),
        orderDto.id(),
        CreditCardMapper.build(dto.userPaymentInfoDto(), userOpt.getCpf()));

    Boolean successPayment = wsRaspayIntegration.processPayment(paymentDto);

    if (Boolean.TRUE.equals(successPayment)) {
      // Salvar informações de pagamento
      UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(dto.userPaymentInfoDto(), userOpt);
      userPaymentInfoRepository.save(userPaymentInfo);

      // Enviar email de criação de conta
      mailIntegration.send(userOpt.getEmail(), message(userOpt), "Acesso Liberado");

      // Retorna o sucesso ou não do pagamento

      return true;
    }

    return false;
  }

  private String message(User user) {
    StringBuilder sb = new StringBuilder();

    sb.append("Olá ").append(user.getName()).append(",\n\n");
    sb.append("Seja bem-vindo(a) à RasmooPlus!\n\n");
    sb.append("Aqui estão seus dados de acesso:\n");
    sb.append("Login: ").append(user.getEmail()).append("\n");
    sb.append("Senha: ").append("alunorasmoo").append("\n\n");
    sb.append("Acesse a plataforma pelo link abaixo:\n");
    sb.append("https://www.sua-plataforma.com/login\n\n");
    sb.append("Em caso de dúvidas, estamos a disposição.\n");
    sb.append("Atenciosamente,\n");
    sb.append("Rasmoo Plus");

    return sb.toString();

  }
}
