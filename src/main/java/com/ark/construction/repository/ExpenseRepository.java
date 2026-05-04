package com.ark.construction.repository;

import com.ark.construction.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    Double sumAllExpenses();

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e")
    Double getTotalExpense();

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e WHERE e.project.id = :projectId")
    Double getTotalExpenseByProject(Long projectId);

    List<Expense> findAllByOrderByExpenseDateDesc();

    List<Expense> findByProject_IdOrderByExpenseDateDesc(Long projectId);

}