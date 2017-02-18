(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('getProductsPattern', getProductsPattern);

    getProductsPattern.$inject = ['$resource', 'DateUtils'];

    function getProductsPattern ($resource, DateUtils) {
        var resourceUrl =  'api/products/pattern/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
