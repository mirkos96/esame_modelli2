package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 12/7/15.
 */
@XmlRootElement
public class ExpenseFilter {

    private List<String> categories;
    private Long startDate = -1L;
    private Long endDate = -1L;
    private double lowerRange = -1.0;
    private double upperRange = -1.0;

    public ExpenseFilter() {

    }

    public ExpenseFilter(List<String> categories, Long startDate, Long endDate, double lowerRange,
                         double upperRange) {
        this.categories = categories;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public double getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(double lowerRange) {
        this.lowerRange = lowerRange;
    }

    public double getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(double upperRange) {
        this.upperRange = upperRange;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("categories", categories)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("lowerRange", lowerRange)
                .append("upperRange", upperRange)
                .toString();
    }
}
