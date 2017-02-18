(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('getModulesAssortment', getModulesAssortment);

    getModulesAssortment.$inject = ['$resource', 'DateUtils'];

    function getModulesAssortment ($resource, DateUtils) {
        var resourceUrl =  'api/modules/assortment/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
