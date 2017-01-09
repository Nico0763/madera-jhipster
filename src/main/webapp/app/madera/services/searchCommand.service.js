(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchCommand', searchCommand);

    searchCommand.$inject = ['$resource', 'DateUtils'];

    function searchCommand ($resource, DateUtils) {
        var resourceUrl =  'api/commands/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
