(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('ImageSaveFile', ImageSaveFile);

    ImageSaveFile.$inject = ['$resource'];

    function ImageSaveFile ($resource) {
        var resourceUrl =  'api/images/store';

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
            'save': { method:'POST',
                      headers: {"Content-Type": undefined},
                        transformResponse: function (data) {
                            console.log('save call'); 
                            console.log(data);
                        if(data)
                        {
                             data = angular.fromJson(data);
                         }
                        return data;
                },
                isArray:false
             },
            'update': { method:'PUT' }
        });
    }
})();
