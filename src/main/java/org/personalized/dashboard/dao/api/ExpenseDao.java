package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.ExpenseFilter;

import java.util.List;

/**
 * Created by sudan on 17/5/15.
 */
public interface ExpenseDao {

    /**
     * Create a new expense for the user
     *
     * @param expense
     * @return
     */
    String create(Expense expense, String userId);

    /**
     * Get the expense for the expenseId and userId
     *
     * @param expenseId
     * @return
     */
    Expense get(String expenseId, String userId);

    /**
     * Update the expense for the userId
     *
     * @param expense
     * @param userId
     * @return
     */
    Long update(Expense expense, String userId);

    /**
     * Delete the expense for the expenseId and userId
     *
     * @param expenseId
     * @param userId
     * @return
     */
    Long delete(String expenseId, String userId);

    /**
     * Count the expenses for the user and expenseFilter
     *
     * @param userId
     * @return
     */
    Long count(ExpenseFilter expenseFilter, String userId);

    /**
     * Get the expenses for limit , offset and expenseFilter for the user
     *
     * @param expenseFilter
     * @param limit
     * @param offset
     * @param userId
     * @return
     */
    List<Expense> get(ExpenseFilter expenseFilter, int limit, int offset, String userId);
}
