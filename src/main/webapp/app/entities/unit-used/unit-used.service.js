(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Unit_used', Unit_used);

    Unit_used.$inject = ['$resource'];

    function Unit_used ($resource) {
        var resourceUrl =  'api/unit-useds/:id';

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
