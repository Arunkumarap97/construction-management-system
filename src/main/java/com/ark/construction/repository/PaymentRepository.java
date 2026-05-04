package com.ark.construction.repository;

import com.ark.construction.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
    Double sumAllPayments();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.project.id = :projectId")
    Double totalPaidByProject(@Param("projectId") Long projectId);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.project.id = :projectId
    """)
    Double getTotalPaidByProject(Long projectId);

    @Query("""
    SELECT COALESCE(SUM(p.amount),0)
    FROM Payment p
    WHERE p.project.id = :projectId
    AND p.paymentDate BETWEEN :start AND :end
""")
    Double sumByProjectAndDate(Long projectId, LocalDate start, LocalDate end);
}