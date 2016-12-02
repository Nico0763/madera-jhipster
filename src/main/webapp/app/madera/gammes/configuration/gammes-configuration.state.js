(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.gammes-configuration', {
                parent: 'madera-admin',
                url: 'gammes-configuration?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/gammes/configuration/gammes-configuration.html',
                        controller: 'GammesConfigurationController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                    }],
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('assortment');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.gammes-configuration.new', {
            parent: 'madera-admin.gammes-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/configuration/gamme-dialog.html',
                    controller: 'GammeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                skeleton_conception_mode: null,
                                skeleton_conception_modeContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('madera-admin.gammes-configuration', null, { reload: 'madera-admin.gammes-configuration' });
                }, function() {
                    $state.go('madera-admin.gammes-configuration');
                });
            }]
        })
        .state('madera-admin.gammes-configuration.edit', {
            parent: 'madera-admin.gammes-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/configuration/gamme-dialog.html',
                    controller: 'GammeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Assortment', function(Assortment) {
                            return Assortment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.gammes-configuration', null, { reload: 'madera-admin.gammes-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.gammes-configuration.delete', {
            parent: 'madera-admin.gammes-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/configuration/gamme-delete-dialog.html',
                    controller: 'GammeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Assortment', function(Assortment) {
                            return Assortment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.gammes-configuration', null, { reload: 'madera-admin.gammes-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
