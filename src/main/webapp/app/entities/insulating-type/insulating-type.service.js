(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Insulating_type', Insulating_type);

    Insulating_type.$inject = ['$resource'];

    function Insulating_type ($resource) {
        var resourceUrl =  'api/insulating-types/:id';

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
