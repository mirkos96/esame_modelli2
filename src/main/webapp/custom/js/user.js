(function($, window, document) {

    "use strict"

    var User = Backbone.Model.extend({

        urlRoot: '/iPersonal/dashboard/users'
    });

    var UserView = Backbone.View.extend({

        el: $('span.username'),
        template: $('#username-template').html(),

        initialize: function() {
            this.getUserData();
        },

        getUserData: function() {

            var self = this;
            userModel.fetch().complete(function(response) {

                if (response.status == 200) {
                    self.render();

                    if (userModel.get('tags')) {
                        backboneGlobalObj.trigger('tag:set', {
                            'tags': userModel.get('tags')
                        });
                    }

                    if (userModel.get('expenseCategories')) {
                        backboneGlobalObj.trigger('expense_category:set', {
                            'expenseCategories': userModel.get('expenseCategories')
                        });
                    }
                }
            });
        },

        render: function() {
            var tmpl = _.template(this.template);
            this.$el.html(tmpl(userModel.toJSON()));
            return this;
        }
    });

    window.userModel = new User();
    window.userView = new UserView();

})(jQuery, window, document);