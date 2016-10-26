(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('Module_nature', Module_nature);

    Module_nature.$inject = ['$resource'];

    function Module_nature ($resource) {
        var resourceUrl =  'api/module-natures/:id';

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
