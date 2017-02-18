(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('getComponentsModule', getComponentsModule);

    getComponentsModule.$inject = ['$resource', 'DateUtils'];

    function getComponentsModule ($resource, DateUtils) {
        var resourceUrl =  'api/module_components/module/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
