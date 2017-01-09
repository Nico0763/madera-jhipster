(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('searchCoupe', searchCoupe);

    searchCoupe.$inject = ['$resource', 'DateUtils'];

    function searchCoupe ($resource, DateUtils) {
        var resourceUrl =  'api/principal_cross_sections/search/:critere';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
