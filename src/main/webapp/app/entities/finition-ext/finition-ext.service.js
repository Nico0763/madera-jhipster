(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Finition_ext', Finition_ext);

    Finition_ext.$inject = ['$resource'];

    function Finition_ext ($resource) {
        var resourceUrl =  'api/finition-exts/:id';

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
