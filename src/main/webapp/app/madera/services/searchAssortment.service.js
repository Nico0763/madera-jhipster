(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchAssortment', searchAssortment);

    searchAssortment.$inject = ['$resource', 'DateUtils'];

    function searchAssortment ($resource, DateUtils) {
        var resourceUrl =  'api/assortments/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
