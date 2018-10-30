package org.personalized.dashboard.dao;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.ExpenseDao;
import org.personalized.dashboard.dao.api.UserDao;
import org.personalized.dashboard.dao.impl.ExpenseDaoImpl;
import org.personalized.dashboard.dao.impl.UserDaoImpl;
import org.personalized.dashboard.model.CurrencyType;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.ExpenseFilter;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sudan on 16/7/15.
 */
@ActiveProfiles("test")
public class ExpenseDaoTest {

    private ExpenseDao expenseDao;
    private UserDao userDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.expenseDao = new ExpenseDaoImpl(new IdGenerator());
        this.userDao = new UserDaoImpl(new IdGenerator());
    }

    @Test
    public void testExpenseDao() throws ParseException {

        boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties

         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.EXPENSES).drop();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Expense expense1 = new Expense();
            expense1.setTitle("title1");
            expense1.setDescription("desc1");
            expense1.setAmount(200);
            expense1.setDate(dateFormat.parse("2015-05-01").getTime());
            String expenseId1 = expenseDao.create(expense1, "1");

            Expense expense2 = new Expense();
            expense2.setTitle("title2");
            expense2.setDescription("desc2");
            expense2.setAmount(400);
            expense2.setDate(dateFormat.parse("2015-06-01").getTime());
            List<String> categories = Lists.newArrayList();
            categories.add("cat1");
            categories.add("cat2");
            expense2.setCategories(categories);
            String expenseId2 = expenseDao.create(expense2, "1");

            Expense expense3 = new Expense();
            expense3.setTitle("title3");
            expense3.setDescription("desc3");
            expense3.setAmount(500);
            expense3.setDate(dateFormat.parse("2015-07-01").getTime());
            String expenseId3 = expenseDao.create(expense3, "1");

            Expense expense4 = new Expense();
            expense4.setTitle("title4");
            expense4.setDescription("desc4");
            expense4.setAmount(250);
            expense4.setDate(dateFormat.parse("2015-08-01").getTime());
            String expenseId4 = expenseDao.create(expense4, "1");

            Expense expense5 = new Expense();
            expense5.setTitle("title5");
            expense5.setDescription("desc5");
            expense5.setAmount(700);
            expense5.setDate(dateFormat.parse("2015-03-01").getTime());
            String expenseId5 = expenseDao.create(expense5, "1");

            Expense expenseRead1 = expenseDao.get(expenseId1, "1");
            Expense expenseRead2 = expenseDao.get(expenseId2, "1");

            Assert.assertEquals("ExpenseIds match", expenseId1, expenseRead1.getExpenseId());
            Assert.assertEquals("Title match", "title1", expenseRead1.getTitle());
            Assert.assertEquals("Description match", "desc1", expenseRead1.getDescription());
            Assert.assertEquals("Amount match", 200, (int) (expenseRead1.getAmount()));
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(), expenseRead1.getCurrencyType().name());
            Assert.assertEquals("Date match", (long) dateFormat.parse("2015-05-01").getTime(), (long) expenseRead1.getDate());
            Assert.assertNotNull("Createdon is not null", expenseRead1.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", expenseRead1.getModifiedAt());
            Assert.assertEquals("Categories count match", 0, expenseRead1.getCategories().size());

            Assert.assertEquals("ExpenseIds match", expenseId2, expenseRead2.getExpenseId());
            Assert.assertEquals("Title match", "title2", expenseRead2.getTitle());
            Assert.assertEquals("Description match", "desc2", expenseRead2.getDescription());
            Assert.assertEquals("Amount match", 400, (int) (expenseRead2.getAmount()));
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(), expenseRead2.getCurrencyType().name());
            Assert.assertEquals("Date match", (long) dateFormat.parse("2015-06-01").getTime(), (long) expenseRead2.getDate());
            Assert.assertNotNull("Createdon is not null", expenseRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", expenseRead2.getModifiedAt());
            Assert.assertEquals("Categories count match", 2, expenseRead2.getCategories().size());
            Assert.assertEquals("Category 1 match", "cat1", expenseRead2.getCategories().get(0));
            Assert.assertEquals("Category 2 match", "cat2", expenseRead2.getCategories().get(1));

            expense1.setExpenseId(expenseId1);
            expense1.setTitle("titlemod1");
            expense1.setDescription("descmod1");
            expense1.setAmount(250);
            expense1.setDate(dateFormat.parse("2015-05-03").getTime());
            expense1.setCategories(categories);
            expenseDao.update(expense1, "1");

            expenseRead1 = expenseDao.get(expenseId1, "1");

            Assert.assertEquals("ExpenseIds match", expenseId1, expenseRead1.getExpenseId());
            Assert.assertEquals("Title match", "titlemod1", expenseRead1.getTitle());
            Assert.assertEquals("Description match", "descmod1", expenseRead1.getDescription());
            Assert.assertEquals("Amount match", 250, (int) (expenseRead1.getAmount()));
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(), expenseRead1.getCurrencyType().name());
            Assert.assertEquals("Date match", (long) dateFormat.parse("2015-05-03").getTime(), (long) expenseRead1.getDate());
            Assert.assertNotNull("Createdon is not null", expenseRead1.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", expenseRead1.getModifiedAt());
            Assert.assertEquals("Categories count match", 2, expenseRead1.getCategories().size());
            Assert.assertEquals("Category 1 match", "cat1", expenseRead1.getCategories().get(0));
            Assert.assertEquals("Category 2 match", "cat2", expenseRead1.getCategories().get(1));

            ExpenseFilter expenseFilter = new ExpenseFilter();
            expenseFilter.setLowerRange(300);
            Long count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 3L, (long) count);

            expenseFilter = new ExpenseFilter();
            expenseFilter.setLowerRange(300);
            expenseFilter.setUpperRange(500);
            count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 2L, (long) count);

            expenseFilter = new ExpenseFilter();
            expenseFilter.setStartDate(dateFormat.parse("2015-06-01").getTime());
            count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 3L, (long) count);

            expenseFilter = new ExpenseFilter();
            expenseFilter.setStartDate(dateFormat.parse("2015-06-01").getTime());
            expenseFilter.setEndDate(dateFormat.parse("2015-07-30").getTime());
            count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 2L, (long) count);

            expenseFilter = new ExpenseFilter();
            categories = Lists.newArrayList();
            categories.add("cat1");
            categories.add("cat3");
            expenseFilter.setCategories(categories);
            count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 2L, (long) count);

            expenseFilter = new ExpenseFilter();
            expenseFilter.setLowerRange(400);
            expenseFilter.setUpperRange(500);
            expenseFilter.setStartDate(dateFormat.parse("2015-07-01").getTime());
            count = expenseDao.count(expenseFilter, "1");
            Assert.assertEquals("Count match", 1L, (long) count);

            expenseFilter = new ExpenseFilter();
            expenseFilter.setLowerRange(300);
            List<Expense> expenses = expenseDao.get(expenseFilter, 3, 0, "1");

            Assert.assertEquals("Count match", 3, expenses.size());

            Assert.assertEquals("ExpenseId match 1", expenseId5, expenses.get(0).getExpenseId());
            Assert.assertEquals("Title match 1", "title5", expenses.get(0).getTitle());
            Assert.assertEquals("Desc match 1", "desc5", expenses.get(0).getDescription());
            Assert.assertEquals("Amount match 1", 700, (int) expenses.get(0).getAmount());
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(),
                    expenses.get(0).getCurrencyType().name());
            Assert.assertEquals("Date match 1", dateFormat.parse("2015-03-01").getTime(),
                    (long) expenses.get(0).getDate());

            Assert.assertEquals("ExpenseId match 2", expenseId3, expenses.get(1).getExpenseId());
            Assert.assertEquals("Title match 2", "title3", expenses.get(1).getTitle());
            Assert.assertEquals("Desc match 2", "desc3", expenses.get(1).getDescription());
            Assert.assertEquals("Amount match 2", 500, (int) expenses.get(1).getAmount());
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(),
                    expenses.get(1).getCurrencyType().name());
            Assert.assertEquals("Date match 2", dateFormat.parse("2015-07-01").getTime(),
                    (long) expenses.get(1).getDate());

            Assert.assertEquals("ExpenseId match 3", expenseId2, expenses.get(2).getExpenseId());
            Assert.assertEquals("Title match 3", "title2", expenses.get(2).getTitle());
            Assert.assertEquals("Desc match 3", "desc2", expenses.get(2).getDescription());
            Assert.assertEquals("Amount match 3", 400, (int) expenses.get(2).getAmount());
            Assert.assertEquals("CurrencyType match", CurrencyType.INR.name(),
                    expenses.get(2).getCurrencyType().name());
            Assert.assertEquals("Date match 3", dateFormat.parse("2015-06-01").getTime(),
                    (long) expenses.get(2).getDate());
            Assert.assertEquals("Category match 1", "cat1", expenses.get(2).getCategories().get(0));
            Assert.assertEquals("Category match 1", "cat2", expenses.get(2).getCategories().get(1));

        }
    }

}
