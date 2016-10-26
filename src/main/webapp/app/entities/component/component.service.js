(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Component', Component);

    Component.$inject = ['$resource'];

    function Component ($resource) {
        var resourceUrl =  'api/components/:id';

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
