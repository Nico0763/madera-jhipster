(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Component_product', Component_product);

    Component_product.$inject = ['$resource'];

    function Component_product ($resource) {
        var resourceUrl =  'api/component-products/:id';

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
