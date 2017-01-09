(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchCommandByState', searchCommandByState);

    searchCommandByState.$inject = ['$resource', 'DateUtils'];

    function searchCommandByState ($resource, DateUtils) {
        var resourceUrl =  'api/commands/state/:state';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
