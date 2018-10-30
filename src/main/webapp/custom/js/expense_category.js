(function($, window, document) {

    "use strict"

    var ExpenseCategory = Backbone.Model.extend({
        defaults: {
            expenseCategories: [],
            expenseCategoriesSet: {}
        },

        addExpenseCategories: function(userExpenseCategories) {

            for (var i = 0; i < userExpenseCategories.length; i++) {

                if (!this.attributes.expenseCategoriesSet[userExpenseCategories[i]]) {
                    this.attributes.expenseCategories.push(userExpenseCategories[i])
                    this.attributes.expenseCategoriesSet[userExpenseCategories[i]] = true;
                }
            }
        },

        getExpenseCategories: function() {
            return this.attributes.expenseCategories;
        }

    });

    window.expenseCategoryModel = new ExpenseCategory();

})(jQuery, window, document);