(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchProvider', searchProvider);

    searchProvider.$inject = ['$resource', 'DateUtils'];

    function searchProvider ($resource, DateUtils) {
        var resourceUrl =  'api/providers/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
