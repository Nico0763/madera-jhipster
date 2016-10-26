(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Assortment', Assortment);

    Assortment.$inject = ['$resource'];

    function Assortment ($resource) {
        var resourceUrl =  'api/assortments/:id';

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
