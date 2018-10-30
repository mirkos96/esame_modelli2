package org.personalized.dashboard.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.validator.FieldName;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 4/5/15.
 */
@XmlRootElement
public class Expense {

    private String expenseId;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.EXPENSE_TITLE)
    private String title;

    @NotEmpty
    @Size(max = Constants.CONTENT_MAX_LENGTH)
    @FieldName(name = FieldKeys.EXPENSE_DESCRIPTION)
    private String description;

    @FieldName(name = FieldKeys.EXPENSE_AMOUNT)
    private double amount = 0.0;


    private CurrencyType currencyType = CurrencyType.INR;

    @FieldName(name = FieldKeys.EXPENSE_DATE)
    private Long date = -1L;

    private List<String> categories = Lists.newArrayList();

    private List<String> tags = Lists.newArrayList();

    private Long createdOn;
    private Long modifiedAt;

    public Expense() {

    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("expenseId", expenseId)
                .append("title", title)
                .append("description", description)
                .append("amount", amount)
                .append("currencyType", currencyType)
                .append("date", date)
                .append("categories", categories)
                .append("tags", tags)
                .append("createdOn", createdOn)
                .append("modifiedAt", modifiedAt)
                .toString();
    }
}
