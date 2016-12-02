(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('DuplicateGamme', DuplicateGamme);

    DuplicateGamme.$inject = ['$resource'];

    function DuplicateGamme ($resource) {
        var resourceUrl =  'api/assortments/duplicate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                },
                isArray:true
            },
            'update': { method:'PUT' }
        });
    }
})();
