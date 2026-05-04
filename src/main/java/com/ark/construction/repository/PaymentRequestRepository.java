package com.ark.construction.repository;

import com.ark.construction.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {
}
