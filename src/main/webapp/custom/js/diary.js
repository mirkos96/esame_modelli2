(function($, window, document) {

    "use strict"

    var DIARY_URL_ROOT = '/iPersonal/dashboard/diaries';

    var Page = Base.extend({
        urlRoot: DIARY_URL_ROOT,

        initialize: function() {
            this.mandatory = {
                    'title': true,
                    'content': true,
                    'date': true
                },
                this.maxLength = {
                    'title': 50,
                    'content': 3000
                },
                this.formAttributes = ['title', 'content', 'date'];
        }

    });

    var Pages = Backbone.Collection.extend({
        model: Page
    });

    var DiaryView = BaseView.extend({

        entityType: 'DIARY',
        el: $('#diary-wrapper'),
        upsertTemplate: $('#diary-upsert-template').html(),
        displayTemplate: $('#diary-display-template').html(),

        events: {
            'click #diary-submit': 'upsertDiary',
            'click #diary-cancel': 'resetValues',
            'click #diary-tag-img': 'displayTagSelection'
        },

        getModel: function() {
            return new Page({
                title: '',
                content: '',
                dateStr: '',
                tags: []
            });
        },

        buildModel: function(entity) {
            entity['dateStr'] = entity['year'] + '-' + entity['month'] + '-' + entity['date'];
            return new Page(entity);
        },

        prepareVariables: function() {

            this.saveForm = $('#diary-form');
            this.tagImage = $('#diary-tag-img');
            this.searchTag = $('#diary-tag');
            this.diaryRTE = $('#diary');
        },

        initializeUpdateForm: function() {
            this.prepareVariables();
            Init.initDiary();
        },

        resetValues: function(e) {

            if (e) {
                e.preventDefault();
            }
            BaseView.prototype.resetValues.apply(this, arguments);
            this.diaryRTE.empty();
        },

        upsertDiary: function(e) {

            var self = this;
            e.preventDefault();

            var entityId = self.saveForm.find('.entityId').html();

            if (entityId) {
                self.model = new Page({
                    id: entityId,
                    pageId: entityId
                });
            } else {
                self.model = new Page();
            }

            self.model.set({
                title: self.saveForm.find('[name=title]').val(),
                content: self.saveForm.find('div#diary').cleanHtml(),
                year: self.saveForm.find('[name=date]').val().split('-')[0],
                month: self.saveForm.find('[name=date]').val().split('-')[1],
                date: self.saveForm.find('[name=date]').val().split('-')[2],
                dateStr: self.saveForm.find('[name=date]').val()
            });

            var errors = self.model.validate(self.model.attributes);
            if (errors) {
                self.renderErrors(errors);
                return;
            }

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
                            self.postCreation(response.responseText, "DIARY", self.model.get('title'), 1, tags);
                            self.model.set({
                                id: response.responseText,
                                'createdOn': Math.floor(Date.now()),
                                'modifiedAt': Math.floor(Date.now()),
                                'tags': tags,
                                'summary': self.model.get('content').replace(/<(?:.|\n)*?>/gm, '').trim()
                            });
                        } else {
                            self.postCreation(entityId, "DIARY", self.model.get('title'), 0, tags);
                            self.model.set({
                                'id': entityId,
                                'modifiedAt': Math.floor(Date.now()),
                                'tags': tags,
                                'summary': self.model.get('content').replace(/<(?:.|\n)*?>/gm, '').trim()
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

        fetchPages: function(e) {

            var self = this;

            if (e) {
                e.preventDefault();
            }

            if (this.collection.length == parseInt(entityCountModel.attributes.diaries)) {
                var entityList = this.buildEntityList();
                backboneGlobalObj.trigger('entity:displaylist', entityList);
                return;
            }

            this.model = new Page();
            this.model.fetch({
                data: {
                    offset: this.collection.length,
                    limit: 20
                }
            }).complete(function(response) {
                if (response.status == 200) {
                    var pages = JSON.parse(response.responseText)['page'];
                    if (pages instanceof Array) {
                        for (var index in pages) {
                            var page = new Page(pages[index]);
                            page.set({
                                id: pages[index]['pageId']
                            });

                            if (page.attributes.tags && !(page.attributes.tags instanceof Array)) {
                                var tags = [];
                                tags.push(page.attributes.tags);
                                page.set({
                                    'tags': tags
                                });
                            }
                            page.set({
                                'dateStr': page.get('year') + "-" + page.get('month') + "-" + page.get('date')
                            });
                            self.collection.push(page);
                        }
                    } else if (pages) {
                        var page = new Page(pages);
                        page.set({
                            id: pages['pageId']
                        });
                        if (page.attributes.tags && !(page.attributes.tags instanceof Array)) {
                            var tags = [];
                            tags.push(page.attributes.tags);
                            page.set({
                                'tags': tags
                            });
                        }
                        page.set({
                            'dateStr': page.get('year') + "-" + page.get('month') + "-" + page.get('date')
                        });
                        self.collection.push(page);
                    }
                    var entityList = self.buildEntityList();
                    backboneGlobalObj.trigger('entity:displaylist', entityList);
                }
            });
        },

        buildEntityList: function() {

            var entityList = [];

            for (var i = 0; i < this.collection.length; i++) {
                var summary = this.collection.models[i].attributes.summary;
                var entity = {
                    'entityId': this.collection.models[i].attributes.id,
                    'entityTitle': this.collection.models[i].attributes.title,
                    'entitySummary': summary ? summary.substring(0, 100) : summary,
                    'entityType': 'diary',
                    'dateStr': this.collection.models[i].attributes.dateStr,
                    'year': this.collection.models[i].attributes.year,
                    'month': this.collection.models[i].attributes.month,
                    'date': this.collection.models[i].attributes.date,
                    'modifiedAt': this.collection.models[i].attributes.modifiedAt,
                };
                entityList.push(entity);
            }
            return entityList;
        },

        getDeletableModel: function(id) {

            return new Page({
                id: id
            });
        }
    });

    window.diaryView = new DiaryView({
        collection: new Pages()
    });

})(jQuery, window, document);