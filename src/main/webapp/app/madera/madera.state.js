(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
            .state('madera-admin', {
                
                parent: 'app',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/madera/navbar/navbar-madera.html',
                        controller: 'NavbarMaderaController',
                        controllerAs: 'vm'
                    },
                    'main@madera-admin': {
                        templateUrl: 'app/madera/main.html',
                        controller: '',
                        controllerAs: 'vm'
                    },
                    'login@madera-admin': {
                        templateUrl:'app/login/login.html',
                        controller:'LoginMaderaController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                } 
            });
    }
})();
