(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchComponent', searchComponent);

    searchComponent.$inject = ['$resource', 'DateUtils'];

    function searchComponent ($resource, DateUtils) {
        var resourceUrl =  'api/components/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
