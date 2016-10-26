(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Frame', Frame);

    Frame.$inject = ['$resource'];

    function Frame ($resource) {
        var resourceUrl =  'api/frames/:id';

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
