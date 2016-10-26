(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Cover_type', Cover_type);

    Cover_type.$inject = ['$resource'];

    function Cover_type ($resource) {
        var resourceUrl =  'api/cover-types/:id';

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
