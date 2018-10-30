(function($, window, document) {

    "use strict"

    var SearchEntity = Base.extend({

        urlRoot: '/iPersonal/dashboard/search'
    });

    var ExpenseFilterEntity = Base.extend({

    });

    var SearchResult = Base.extend({

    });

    var SearchResults = Backbone.Collection.extend({
        model: SearchResult
    });

    var SearchView = BaseView.extend({

        entityType: 'GENERAL',
        el: $('#search-wrapper'),
        generalSearchForm: $('#general-search-form'),
        expenseSearchForm: $('#expense-search-form'),

        events: {
            'click #search': 'search',
            'click a': 'toggleSearch'
        },

        search: function(e) {

            var target = this.$el.find('.nav-tabs').find('li.active').data('type');
            if (target == 'general') {
                this.searchEntities(e);
            } else {
                this.searchExpenses(e);
            }
        },

        searchEntities: function(e) {

            var self = this;
            e.preventDefault();

            this.model = new SearchEntity();
            var entities = this.generalSearchForm.find('#search-entity').val();
            var tags = this.generalSearchForm.find('#search-tag').val();
            var titles = this.generalSearchForm.find('#search-title').val();
            var keywords = this.generalSearchForm.find('#search-keyword').val();

            if (entities)
                this.model.set({
                    entityTypes: entities
                });
            if (tags)
                this.model.set({
                    tags: tags
                });
            if (titles)
                this.model.set({
                    titles: titles
                });
            if (keywords)
                this.model.set({
                    keywords: keywords
                });

            var result = this.model.save({
                success: function(response) {},
                error: function(error) {}
            });

            if (result) {
                result.complete(function(response) {
                    if (response.status == 200) {
                        self.collection = new SearchResults();
                        var searchResults = JSON.parse(response.responseText);

                        if (searchResults) {
                            searchResults = searchResults['searchDocument'];
                            if (searchResults instanceof Array) {

                                for (var index in searchResults) {

                                    var searchResult = new SearchResult();
                                    searchResult.set({
                                        entityType: searchResults[index]['entityType'],
                                        entityId: searchResults[index]['documentId'],
                                        modifiedAt: searchResults[index]['createdAt'],
                                        entityTitle: searchResults[index]['title'],
                                        tags: []
                                    });

                                    if (searchResult.get('entityType') == 'BOOKMARK' || searchResult.get('entityType') == 'PIN') {
                                        searchResult.set({
                                            url: searchResults[index]['summary'].split('###')[0],
                                            entitySummary: searchResults[index]['summary'].split('###')[1]
                                        });
                                    } else if (searchResult.get('entityType') == 'EXPENSE') {
                                        searchResult.set({
                                            amount: searchResults[index]['summary'].split('###')[0],
                                            entitySummary: searchResults[index]['summary'].split('###')[1]
                                        });
                                    } else {
                                        searchResult.set({
                                            entitySummary: searchResults[index]['summary']
                                        });
                                    }
                                    self.collection.add(searchResult);
                                }

                            } else {
                                var searchResult = new SearchResult({
                                    entityType: searchResults['entityType'],
                                    entityId: searchResults['documentId'],
                                    modifiedAt: searchResults['createdAt'],
                                    entityTitle: searchResults['title'],
                                    tags: []
                                });

                                if (searchResult.get('entityType') == 'BOOKMARK' || searchResult.get('entityType') == 'PIN') {
                                    searchResult.set({
                                        url: searchResults['summary'].split('###')[0],
                                        entitySummary: searchResults['summary'].split('###')[1]
                                    });
                                } else if (searchResult.get('entityType') == 'EXPENSE') {
                                    searchResult.set({
                                        amount: searchResults['summary'].split('###')[0],
                                        entitySummary: searchResults['summary'].split('###')[1]
                                    });
                                } else {
                                    searchResult.set({
                                        entitySummary: searchResults['summary']
                                    });
                                }
                                self.collection.add(searchResult);
                            }
                        }
                        var entityList = self.buildEntityList();
                        self.resetModal();
                        $('.modal-header').find('button.close').click();
                        backboneGlobalObj.trigger('entity:displaylist', entityList);
                    }
                });
            }
        },

        searchExpenses: function(e) {

            var payload = {};
            var categories = this.expenseSearchForm.find('#search-category').val();

            if (categories) {
                payload['categories'] = categories;
            }

            var startAmount = this.expenseSearchForm.find('[name=start-amount]').val();
            if (startAmount && !isNaN(startAmount)) {
                payload['lowerRange'] = startAmount;
            }

            var endAmount = this.expenseSearchForm.find('[name=end-amount]').val();
            if (endAmount && !isNaN(endAmount)) {
                payload['upperRange'] = endAmount;
            }

            var startDate = this.expenseSearchForm.find('[name=start-date]').val();
            if (startDate) {
                var startDateLong = (new Date(startDate).getTime() / 1000).toFixed(0);
                if (!isNaN(startDateLong)) {
                    payload['startDate'] = startDateLong;
                }
            }

            var endDate = this.expenseSearchForm.find('[name=end-date]').val();
            if (endDate) {
                var endDateLong = (new Date(endDate).getTime() / 1000).toFixed(0);
                if (!isNaN(endDateLong)) {
                    payload['endDate'] = endDateLong;
                }
            }
            expenseView.searchExpenses(payload);
        },

        buildEntityList: function() {

            var entityList = [];

            for (var i = 0; i < this.collection.length; i++) {
                var entity = {};

                entity['entityId'] = this.collection.models[i].attributes.entityId;
                entity['entityType'] = this.collection.models[i].attributes.entityType.toLowerCase();
                entity['entityTitle'] = this.collection.models[i].attributes.entityTitle;
                entity['entitySummary'] = this.collection.models[i].attributes.entitySummary;
                entity['modifiedAt'] = this.collection.models[i].attributes.modifiedAt;

                if (this.collection.models[i].attributes.entityType == 'BOOKMARK' ||
                    this.collection.models[i].attributes.entityType == 'PIN') {
                    entity['url'] = this.collection.models[i].attributes.url;
                }

                if (this.collection.models[i].attributes.entityType == 'EXPENSE') {
                    entity['amount'] = this.collection.models[i].attributes.amount;
                }

                entityList.push(entity);
            }
            return entityList;
        },

        resetModal: function() {
            this.generalSearchForm.find('#search-entity').val('').trigger('chosen:updated');
            this.generalSearchForm.find('#search-tag').val('').trigger('chosen:updated');
            this.generalSearchForm.find('#search-title').val('').trigger('chosen:updated');
            this.generalSearchForm.find('#search-keyword').val('').trigger('chosen:updated');
        },

        toggleSearch: function(e) {

            var div = $(e.target).attr('href');
            $(div).removeClass('invisible').siblings('.tab-pane').addClass('invisible');
        }
    });

    window.searchView = new SearchView();

})(jQuery, window, document);