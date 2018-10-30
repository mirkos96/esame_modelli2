  (function($, window, document) {

      "use strict"

      var EXPENSE_URL_ROOT = '/iPersonal/dashboard/expenses';
      var EXPENSE_SEARCH_URL_ROOT = '/iPersonal/dashboard/expenses/search';

      var Expense = Base.extend({
          urlRoot: EXPENSE_URL_ROOT,

          initialize: function() {
              this.mandatory = {
                      'title': true,
                      'amount': true,
                      'description': true,
                      'date': true
                  },
                  this.maxLength = {
                      'title': 50,
                      'description': 1000
                  },
                  this.type = {
                      'amount': 'double'
                  },
                  this.formAttributes = ['title', 'description', 'amount', 'date', 'categories']
          }

      });

      var ExpenseSearch = Backbone.Model.extend({
          urlRoot: EXPENSE_SEARCH_URL_ROOT
      });

      var Expenses = Backbone.Collection.extend({
          model: Expense
      })

      var ExpenseView = BaseView.extend({

          el: $('#expense-wrapper'),
          entityType: 'EXPENSE',
          upsertTemplate: $('#expense-upsert-template').html(),
          displayTemplate: $('#expense-display-template').html(),

          events: {
              'click #exp-submit': 'upsertExpense',
              'click #exp-cancel': 'resetValues',
              'click #exp-tag-img': 'displayTagSelection',
          },

          getModel: function(id) {

              return new Expense({
                  title: '',
                  description: '',
                  amount: '',
                  date: '',
                  tags: [],
                  categories: []
              });

          },

          buildModel: function(entity) {
              return new Expense(entity);
          },

          prepareVariables: function() {

              this.saveForm = $('#expense-form');
              this.tagImage = $('#exp-tag-img');
              this.searchTag = $('#expense-tag');
              this.categoryDropDown = $('#expense-form').find('[name=categories]');
              this.populateCategories();
          },

          initializeUpdateForm: function() {
              this.prepareVariables();
              Init.initExpense();
          },

          resetValues: function(e) {

              if (e) {
                  e.preventDefault();
              }
              BaseView.prototype.resetValues.apply(this, arguments);
              this.categoryDropDown.val('').trigger('chosen:updated');
          },

          populateCategories: function(expenseCategories) {

              var categories = expenseCategoryModel.getExpenseCategories();
              if (categories) {
                  for (var i = 0; i < categories.length; i++) {
                      this.categoryDropDown.append($('<option></option>').attr('value', categories[i]).text(categories[i]));
                  }
                  this.categoryDropDown.trigger('chosen:updated');
              }
          },

          getDeletableModel: function(id) {

              return new Expense({
                  id: id
              });
          },

          upsertExpense: function(e) {

              var self = this;
              e.preventDefault();

              var entityId = self.saveForm.find('.entityId').html();

              if (entityId) {
                  self.model = new Expense({
                      id: entityId,
                      expenseId: entityId
                  });
              } else {
                  self.model = new Expense();
              }

              self.model.set({
                  title: self.saveForm.find('[name=title]').val(),
                  description: self.saveForm.find('[name=description]').val(),
                  amount: self.saveForm.find('[name=amount]').val(),
                  date: (new Date(self.saveForm.find('[name=date]').val()).getTime() / 1000).toFixed(0),
                  categories: self.saveForm.find('[name=categories]').val()
              });

              self.model.on('invalid', function(model, error) {
                  self.renderErrors(error);
              });

              var result = self.model.save({
                  success: function(response) {},
                  error: function(error) {}
              });

              if (result) {
                  result.complete(function(response) {
                      if (response.status != 201 && response.status != 200) {
                          var errors = self.buildErrorObject(response, self);
                          self.renderErrors(errors);
                      } else {

                          var tags = self.searchTag.val();

                          if (!entityId) {
                              self.postCreation(response.responseText, "EXPENSE", self.model.get('title'), 1, tags, self.model.get('categories'))
                              self.model.set({
                                  id: response.responseText,
                                  'createdOn': Math.floor(Date.now()),
                                  'modifiedAt': Math.floor(Date.now()),
                                  'tags': tags
                              });
                          } else {
                              self.postCreation(entityId, "EXPENSE", self.model.get('title'), 0, tags, self.model.get('categories'));
                              self.model.set({
                                  id: entityId,
                                  'modifiedAt': Math.floor(Date.now()),
                                  'tags': tags
                              });
                              self.collection.remove(self.collection.at(self.findIndex(entityId)));
                          }
                          self.collection.unshift(self.model);
                          var entityList = self.buildEntityList();
                          backboneGlobalObj.trigger('entity:displaylist', entityList);
                      }
                  });
              }
          },

          fetchExpenses: function(e) {

              var self = this;

              if (e) {
                  e.preventDefault();
              }

              if (this.collection.length == parseInt(entityCountModel.attributes.expenses)) {
                  var entityList = this.buildEntityList();
                  backboneGlobalObj.trigger('entity:displaylist', entityList);
                  return;
              }

              this.model = new ExpenseSearch();
              this.model.set({
                  url: '/iPersonal/dashboard/expenses/search'
              })
              this.model.save({
                  data: {
                      offset: this.collection.length,
                      limit: 20
                  }
              }).complete(function(response) {
                  if (response.status == 200) {
                      var expenses = JSON.parse(response.responseText)['expense'];
                      if (expenses instanceof Array) {
                          for (var index in expenses) {
                              var expense = new Expense(expenses[index]);
                              expense.set({
                                  id: expenses[index]['expenseId']
                              });

                              if (expense.attributes.tags && !(expense.attributes.tags instanceof Array)) {
                                  var tags = [];
                                  tags.push(expense.attributes.tags);
                                  expense.set({
                                      'tags': tags
                                  });
                              }

                              if (expense.attributes.categories && !(expense.attributes.categories instanceof Array)) {
                                  var categories = [];
                                  categories.push(expense.attributes.categories);
                                  expense.set({
                                      'categories': categories
                                  });
                              }

                              self.collection.push(expense);
                          }
                      } else if (expenses) {
                          var expense = new Expense(expenses);
                          expense.set({
                              id: expenses['expenseId']
                          });

                          if (expense.attributes.tags && !(expense.attributes.tags instanceof Array)) {
                              var tags = [];
                              tags.push(expense.attributes.tags);
                              expense.set({
                                  'tags': tags
                              });
                          }

                          if (expense.attributes.categories && !(expense.attributes.categories instanceof Array)) {
                              var categories = [];
                              categories.push(expense.attributes.categories);
                              expense.set({
                                  'categories': categories
                              });
                          }
                          self.collection.push(expense);
                      }
                      var entityList = self.buildEntityList();
                      backboneGlobalObj.trigger('entity:displaylist', entityList);
                  }
              });
          },

          buildEntityList: function() {

              var entityList = [];

              for (var i = 0; i < this.collection.length; i++) {
                  var description = this.collection.models[i].attributes.description;
                  var entity = {
                      'entityId': this.collection.models[i].attributes.id,
                      'entityTitle': this.collection.models[i].attributes.title,
                      'amount': this.collection.models[i].attributes.amount,
                      'entitySummary': description ? description.substring(0, 100) : description,
                      'entityType': 'expense',
                      'modifiedAt': this.collection.models[i].attributes.modifiedAt,
                  };
                  entityList.push(entity);
              }
              return entityList;
          },

          buildFilterEntityList: function() {

              var entityList = [];

              for (var i = 0; i < this.expenseFilterCollection.length; i++) {
                  var description = this.expenseFilterCollection.models[i].attributes.description;
                  var entity = {
                      'entityId': this.expenseFilterCollection.models[i].attributes.id,
                      'entityTitle': this.expenseFilterCollection.models[i].attributes.title,
                      'amount': this.expenseFilterCollection.models[i].attributes.amount,
                      'entitySummary': description ? description.substring(0, 100) : description,
                      'entityType': 'expense',
                      'modifiedAt': this.expenseFilterCollection.models[i].attributes.modifiedAt,
                  };
                  entityList.push(entity);
              }
              return entityList;
          },

          searchExpenses: function(expenseFilter) {

              var self = this;
              var expenseFilter = new ExpenseSearch(expenseFilter);
              expenseFilter.save({
                  data: {
                      offset: 0,
                      limit: 20
                  }
              }).complete(function(response) {
                  self.expenseFilterCollection = new Expenses();
                  if (response.status == 200 && JSON.parse(response.responseText) != null) {
                      var expenses = JSON.parse(response.responseText)['expense'];
                      if (expenses instanceof Array) {
                          for (var index in expenses) {
                              var expense = new Expense(expenses[index]);
                              expense.set({
                                  id: expenses[index]['expenseId']
                              });

                              if (expense.attributes.tags && !(expense.attributes.tags instanceof Array)) {
                                  var tags = [];
                                  tags.push(expense.attributes.tags);
                                  expense.set({
                                      'tags': tags
                                  });
                              }

                              if (expense.attributes.categories && !(expense.attributes.categories instanceof Array)) {
                                  var categories = [];
                                  categories.push(expense.attributes.categories);
                                  expense.set({
                                      'categories': categories
                                  });
                              }

                              self.expenseFilterCollection.push(expense);
                          }
                      } else if (expenses) {
                          var expense = new Expense(expenses);
                          expense.set({
                              id: expenses['expenseId']
                          });

                          if (expense.attributes.tags && !(expense.attributes.tags instanceof Array)) {
                              var tags = [];
                              tags.push(expense.attributes.tags);
                              expense.set({
                                  'tags': tags
                              });
                          }

                          if (expense.attributes.categories && !(expense.attributes.categories instanceof Array)) {
                              var categories = [];
                              categories.push(expense.attributes.categories);
                              expense.set({
                                  'categories': categories
                              });
                          }
                          self.expenseFilterCollection.push(expense);
                      }
                  }
                  if (response.status == 200) {
                      var entityList = self.buildFilterEntityList();
                      backboneGlobalObj.trigger('entity:displaylist', entityList);
                  }
                  $('.modal-header').find('button.close').click();
              });

          }
      });

      window.expenseView = new ExpenseView({
          collection: new Expenses()
      });

  })(jQuery, window, document);