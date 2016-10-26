(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Module_component', Module_component);

    Module_component.$inject = ['$resource'];

    function Module_component ($resource) {
        var resourceUrl =  'api/module-components/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
