package com.livepix.livepix.pagamentos.repositorio;

import com.livepix.livepix.pagamentos.modelo.PagamentosPix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentosPixRepository extends JpaRepository<PagamentosPix, UUID> {
    Optional<PagamentosPix> findByMercadoPagoId(Long mercadoPagoId);
}
