(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('AddProductDependencies', AddProductDependencies);

    AddProductDependencies.$inject = ['$resource'];

    function AddProductDependencies($resource) {
        var resourceUrl =  'api/products/dependencies';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                },
                isArray:true
            },
            'update': { method:'PUT' }
        });
    }
})();
