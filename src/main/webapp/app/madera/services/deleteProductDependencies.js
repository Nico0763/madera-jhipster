(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('DeleteProductDependencies', DeleteProductDependencies);

    DeleteProductDependencies.$inject = ['$resource'];

    function DeleteProductDependencies ($resource) {
        var resourceUrl =  'api/products/dependencies/:id';

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
