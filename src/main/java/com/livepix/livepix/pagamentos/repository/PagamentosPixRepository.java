package com.livepix.livepix.pagamentos.repository;

import com.livepix.livepix.pagamentos.model.PagamentosPix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentosPixRepository extends JpaRepository<PagamentosPix, UUID> {
    Optional<PagamentosPix> findByMercadoPagoId(Long mercadoPagoId);
}