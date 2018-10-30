(function($, window, document) {

    window.Init = {

        init: function() {
            this.initBackboneEvents();
            this.initSearch();
        },

        initBookmark: function() {

            $('#bookmark-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();
        },

        initNote: function() {

            $('#note-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();
            $('#note').wysiwyg();
            this.initRTE();
        },

        initPin: function() {

            $('#pin-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();
        },

        initTodo: function() {

            $('#todo-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();
        },

        initDiary: function() {

            $('#diary-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();

            $('#diary').wysiwyg();
            this.initRTE();
            this.initDatePicker();
        },

        initExpense: function() {

            $('#expense-tag').chosen({
                width: '100%',
                no_results_text: 'Add a new tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');

            $('#expense-category').chosen({
                width: '100%',
                no_results_text: 'Add a new category and press enter(min 3 chars)'
            }).trigger('chosen:updated');
            this.initChosenDropdowns();
            this.initDatePicker();
        },

        initDatePicker: function() {
            $(document).ready(function() {
                $('input.datepicker').datepicker({
                    format: 'yyyy-mm-dd'
                });
            });
        },

        initBackboneEvents: function() {
            window.backboneGlobalObj = {};
            _.extend(backboneGlobalObj, Backbone.Events);

            backboneGlobalObj.on('tag:add', function(obj) {
                window.tagModel.addTags(obj.entityId, obj.entityType, obj.entityTitle, obj.tags);
            });

            backboneGlobalObj.on('entity:count', function(obj) {
                window.entityCountView.refreshCount(obj.entityType, obj.relativeValue);
            });

            backboneGlobalObj.on('tag:set', function(obj) {
                window.tagModel.setTags(obj.tags);
            });

            backboneGlobalObj.on('expense_category:set', function(obj) {
                window.expenseCategoryModel.addExpenseCategories(obj.expenseCategories);
            });

            backboneGlobalObj.on('expense_category:populate', function(obj) {
                window.expenseView.populateCategories();
            });

            backboneGlobalObj.on('entity:displaylist', function(obj) {
                window.entityListView.renderList(obj);
            });
        },

        initChosenDropdowns: function() {

            $('.chosen-menu').parent().find('div.chosen-container').find('input').on('keyup', function(event) {
                if (event.keyCode == 13) {
                    var value = $(event.target).val();
                    if (value.length >= 3) {
                        var tagDropDown = $(event.target).closest('.form-group').eq(0).children('select');
                        tagDropDown.append($('<option></option>').attr('value', value).text(value));
                        var chosenValues = tagDropDown.val();
                        if (!chosenValues)
                            chosenValues = []
                        chosenValues.push(value);

                        tagDropDown.val(chosenValues);
                        tagDropDown.trigger('chosen:updated');
                    }
                }
            });
        },

        initRTE: function() {
            var self = this;
            self.bold = false;
            self.italic = false;
            self.underline = false;

            $('.btn-group').find('a').on('click', function() {
                var styles = $(this).data('edit');

                if (styles === 'bold') {
                    self.bold = (self.bold == true) ? false : true;
                }
                if (styles === 'italic') {
                    self.italic = (self.italic == true) ? false : true;
                }
                if (styles === 'underline') {
                    self.underline = (self.underline == true) ? false : true;
                }
                (self.bold == true) ?
                $('.btn-group').find('a.bold').addClass('btn-info'):
                    $('.btn-group').find('a.bold').removeClass('btn-info');

                (self.italic == true) ?
                $('.btn-group').find('a.italic').addClass('btn-info'):
                    $('.btn-group').find('a.italic').removeClass('btn-info');

                (self.underline == true) ?
                $('.btn-group').find('a.underline').addClass('btn-info'):
                    $('.btn-group').find('a.underline').removeClass('btn-info');
            });
        },

        initSearch: function() {

            $('#search-tag').chosen({
                width: '100%',
                no_results_text: 'Add a tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');

            $('#search-title').chosen({
                width: '100%',
                no_results_text: 'Add a tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');

            $('#search-keyword').chosen({
                width: '100%',
                no_results_text: 'Add a tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');

            $('#search-entity').chosen({
                width: '100%',
                no_results_text: 'Add a tag and press enter(min 3 chars)'
            }).trigger('chosen:updated');

            $('#search-category').chosen({
                width: '100%',
                no_results_text: 'Add a category and press enter (min 3 chars)'
            });

            this.initChosenDropdowns();
            this.initDatePicker();
        }
    };

    Init.init();

})(jQuery, window, document);