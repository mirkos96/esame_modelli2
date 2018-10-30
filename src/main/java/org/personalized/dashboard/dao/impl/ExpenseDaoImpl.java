package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.ExpenseDao;
import org.personalized.dashboard.model.CurrencyType;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.ExpenseFilter;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by sudan on 17/5/15.
 */
public class ExpenseDaoImpl implements ExpenseDao {

    private IdGenerator idGenerator;

    @Inject
    public ExpenseDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String create(Expense expense, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);

        String expenseId = idGenerator.generateId(Constants.EXPENSE_PREFIX, Constants.ID_LENGTH, true);
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, expenseId)
                .append(FieldKeys.EXPENSE_TITLE, expense.getTitle())
                .append(FieldKeys.EXPENSE_DESCRIPTION, expense.getDescription())
                .append(FieldKeys.EXPENSE_AMOUNT, expense.getAmount())
                .append(FieldKeys.EXPENSE_CURRENCY_TYPE, expense.getCurrencyType().name())
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.EXPENSE_CATEGORIES, expense.getCategories())
                .append(FieldKeys.EXPENSE_DATE, expense.getDate())
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        collection.insertOne(document);
        if (!CollectionUtils.isEmpty(expense.getCategories()))
            addToUserCategories(userId, expense.getCategories());
        return expenseId;
    }

    @Override
    public Expense get(String expenseId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);

        Document document = collection.find(
                and(
                        eq(FieldKeys.PRIMARY_KEY, expenseId),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )
        ).first();

        if (document != null) {
            Expense expense = new Expense();
            expense.setExpenseId(document.getString(FieldKeys.PRIMARY_KEY));
            expense.setTitle(document.getString(FieldKeys.EXPENSE_TITLE));
            expense.setDescription(document.getString(FieldKeys.EXPENSE_DESCRIPTION));
            expense.setAmount(document.getDouble(FieldKeys.EXPENSE_AMOUNT));
            expense.setCurrencyType(CurrencyType.valueOf(document.getString(FieldKeys.EXPENSE_CURRENCY_TYPE)));
            List<String> categories = (List<String>) document.get(FieldKeys.EXPENSE_CATEGORIES);
            expense.setCategories(categories);
            expense.setDate(document.getLong(FieldKeys.EXPENSE_DATE));
            expense.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
            expense.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                expense.setTags(tags);
            }
            return expense;
        }
        return null;
    }

    @Override
    public Long update(Expense expense, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);

        Document document = new Document()
                .append(FieldKeys.EXPENSE_TITLE, expense.getTitle())
                .append(FieldKeys.EXPENSE_DESCRIPTION, expense.getDescription())
                .append(FieldKeys.EXPENSE_AMOUNT, expense.getAmount())
                .append(FieldKeys.EXPENSE_CURRENCY_TYPE, expense.getCurrencyType().name())
                .append(FieldKeys.EXPENSE_CATEGORIES, expense.getCategories())
                .append(FieldKeys.EXPENSE_DATE, expense.getDate())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, expense.getExpenseId()),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                ),
                new Document(Constants.SET_OPERATION, document)
        );

        if (updateResult.getModifiedCount() > 0) {
            if (!CollectionUtils.isEmpty(expense.getCategories()))
                addToUserCategories(userId, expense.getCategories());
        }
        return updateResult.getModifiedCount();
    }

    @Override
    public Long delete(String expenseId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);

        Document document = new Document()
                .append(FieldKeys.IS_DELETED, true);

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, expenseId),
                        eq(FieldKeys.USER_ID, userId)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long count(ExpenseFilter expenseFilter, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);
        return collection.count(getFilterDocument(expenseFilter, userId));
    }

    @Override
    public List<Expense> get(ExpenseFilter expenseFilter, int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES);

        FindIterable<Document> iterator = collection.find(getFilterDocument(expenseFilter, userId))
                .skip(offset).limit(limit)
                .sort(new Document(FieldKeys.MODIFIED_AT, -1)
                );

        final List<Expense> expenses = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Expense expense = new Expense();
                expense.setExpenseId(document.getString(FieldKeys.PRIMARY_KEY));
                expense.setTitle(document.getString(FieldKeys.EXPENSE_TITLE));
                expense.setDescription(document.getString(FieldKeys.EXPENSE_DESCRIPTION));
                expense.setAmount(document.getDouble(FieldKeys.EXPENSE_AMOUNT));
                expense.setCurrencyType(CurrencyType.valueOf(document.getString(FieldKeys.EXPENSE_CURRENCY_TYPE)));
                List<String> categories = (List<String>) document.get(FieldKeys.EXPENSE_CATEGORIES);
                expense.setCategories(categories);
                expense.setDate(document.getLong(FieldKeys.EXPENSE_DATE));
                expense.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                expense.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

                if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                    List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                    expense.setTags(tags);
                }
                expenses.add(expense);
            }
        });
        return expenses;
    }

    private Document getFilterDocument(ExpenseFilter expenseFilter, String userId) {
        Document document = new Document(FieldKeys.USER_ID, userId);

        if (expenseFilter.getLowerRange() >= 0 && expenseFilter.getUpperRange() >= 0) {
            Document rangeDocument = new Document(Constants.GREATER_THAN_EQUAL, expenseFilter.getLowerRange())
                    .append(Constants.LESS_THAN_EQUAL, expenseFilter.getUpperRange());
            document.append(FieldKeys.EXPENSE_AMOUNT, rangeDocument);

        } else if (expenseFilter.getLowerRange() >= 0) {
            document.append(FieldKeys.EXPENSE_AMOUNT,
                    new Document(Constants.GREATER_THAN_EQUAL, expenseFilter.getLowerRange()));
        } else if (expenseFilter.getUpperRange() >= 0) {
            document.append(FieldKeys.EXPENSE_AMOUNT,
                    new Document(Constants.LESS_THAN_EQUAL, expenseFilter.getUpperRange()));
        }

        if (expenseFilter.getStartDate() >= 0 && expenseFilter.getEndDate() >= 0) {
            Document rangeDocument = new Document(Constants.GREATER_THAN_EQUAL, expenseFilter.getStartDate())
                    .append(Constants.LESS_THAN_EQUAL, expenseFilter.getEndDate());
            document.append(FieldKeys.EXPENSE_DATE, rangeDocument);
        } else if (expenseFilter.getStartDate() >= 0) {
            document.append(FieldKeys.EXPENSE_DATE,
                    new Document(Constants.GREATER_THAN_EQUAL, expenseFilter.getStartDate()));
        } else if (expenseFilter.getEndDate() >= 0) {
            document.append(FieldKeys.EXPENSE_DATE,
                    new Document(Constants.LESS_THAN_EQUAL, expenseFilter.getEndDate()));
        }

        if (!CollectionUtils.isEmpty(expenseFilter.getCategories())) {
            document.append(FieldKeys.EXPENSE_CATEGORIES,
                    new Document(Constants.IN, expenseFilter.getCategories()));
        }

        document.append(FieldKeys.IS_DELETED,
                new Document(Constants.NOT_EQUAL_TO, true));

        return document;
    }


    private void addToUserCategories(String userId, List<String> categories) {

        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.USERS);

        Document nestedDocument = new Document(Constants.EACH, categories);
        Document document = new Document(FieldKeys.EXPENSE_CATEGORIES, nestedDocument);

        collection.updateOne(eq(FieldKeys.PRIMARY_KEY, userId),
                new Document(Constants.ADD_TO_SET_OPERATION, document));

    }
}
