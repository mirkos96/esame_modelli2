package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.ExpenseFilter;

import java.util.List;

/**
 * Created by sudan on 17/5/15.
 */
public interface ExpenseService {

    /**
     * Create a new expense for the user
     *
     * @param expense
     * @return
     */
    String createExpense(Expense expense);

    /**
     * Get the expense given the id
     *
     * @param expenseId
     * @return
     */
    Expense getExpense(String expenseId);

    /**
     * Update the expense
     *
     * @param expense
     * @return
     */
    Long updateExpense(Expense expense);

    /**
     * Delete the expense for the expenseId
     *
     * @param expenseId
     */
    void deleteExpense(String expenseId);

    /**
     * Count the expenses for the user with expenseFilter
     *
     * @return
     */
    Long countExpense(ExpenseFilter expenseFilter);

    /**
     * Fetch the expenses with given limit and offset and ExpenseFilter
     *
     * @param expenseFilter
     * @param limit
     * @param offset
     * @return
     */
    List<Expense> fetchExpenses(ExpenseFilter expenseFilter, int limit, int offset);
}
