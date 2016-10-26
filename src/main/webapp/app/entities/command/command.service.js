(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Command', Command);

    Command.$inject = ['$resource'];

    function Command ($resource) {
        var resourceUrl =  'api/commands/:id';

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
