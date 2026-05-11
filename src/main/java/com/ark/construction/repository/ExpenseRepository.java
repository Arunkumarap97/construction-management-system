package com.ark.construction.repository;

import com.ark.construction.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.active = true")
    Double sumAllExpenses();

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.active = true")
    Double getTotalExpense();

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.project.id = :projectId
        AND e.active = true
    """)
    Double getTotalExpenseByProject(Long projectId);

    List<Expense> findByActiveTrueOrderByExpenseDateDesc();

    List<Expense> findByProject_IdAndActiveTrueOrderByExpenseDateDesc(Long projectId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.project.id = :projectId
        AND e.active = true
        AND e.expenseDate BETWEEN :start AND :end
    """)
    Double sumByProjectAndDate(Long projectId, LocalDate start, LocalDate end);
}