(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchModule', searchModule);

    searchModule.$inject = ['$resource', 'DateUtils'];

    function searchModule ($resource, DateUtils) {
        var resourceUrl =  'api/modules/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
