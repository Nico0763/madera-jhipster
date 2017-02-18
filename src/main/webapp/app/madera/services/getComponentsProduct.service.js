(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('getComponentsProduct', getComponentsProduct);

    getComponentsProduct.$inject = ['$resource', 'DateUtils'];

    function getComponentsProduct ($resource, DateUtils) {
        var resourceUrl =  'api/component_product/product/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
