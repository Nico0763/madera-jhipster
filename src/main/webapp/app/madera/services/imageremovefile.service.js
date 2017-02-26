(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('ImageRemoveFile', ImageRemoveFile);

    ImageRemoveFile.$inject = ['$resource'];

    function ImageRemoveFile ($resource) {
        var resourceUrl =  'api/images/remove/:url';

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
