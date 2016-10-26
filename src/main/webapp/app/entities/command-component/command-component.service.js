(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Command_component', Command_component);

    Command_component.$inject = ['$resource'];

    function Command_component ($resource) {
        var resourceUrl =  'api/command-components/:id';

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
