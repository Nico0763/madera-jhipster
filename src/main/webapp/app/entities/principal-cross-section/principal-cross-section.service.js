(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Principal_cross_section', Principal_cross_section);

    Principal_cross_section.$inject = ['$resource'];

    function Principal_cross_section ($resource) {
        var resourceUrl =  'api/principal-cross-sections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
