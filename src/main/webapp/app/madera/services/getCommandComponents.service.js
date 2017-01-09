(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('getCommandComponents', getCommandComponents);

    getCommandComponents.$inject = ['$resource', 'DateUtils'];

    function getCommandComponents ($resource, DateUtils) {
        var resourceUrl =  'api/command_components/command/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
