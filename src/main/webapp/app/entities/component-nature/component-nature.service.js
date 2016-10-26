(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Component_nature', Component_nature);

    Component_nature.$inject = ['$resource'];

    function Component_nature ($resource) {
        var resourceUrl =  'api/component-natures/:id';

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
