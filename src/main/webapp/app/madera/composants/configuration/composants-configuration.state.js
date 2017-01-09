(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.composants-configuration', {
                parent: 'madera-admin',
                url: 'composants-configuration?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/composants/configuration/composants-configuration.html',
                        controller: '_ComposantsConfigurationController',
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
                            $translatePartialLoader.addPart('component');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.composants-configuration.new', {
            parent: 'madera-admin.composants-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/composants/configuration/composants-dialog.html',
                    controller: '_ComposantsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                 name: null,
                                cctp: null,
                                cctpContentType: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('madera-admin.composants-configuration', null, { reload: 'madera-admin.composants-configuration' });
                }, function() {
                    $state.go('madera-admin.composants-configuration');
                });
            }]
        })
        .state('madera-admin.composants-configuration.edit', {
            parent: 'madera-admin.composants-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/composants/configuration/composants-dialog.html',
                    controller: '_ComposantsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Component', function(Component) {
                            return Component.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.composants-configuration', null, { reload: 'madera-admin.composants-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.composants-configuration.delete', {
            parent: 'madera-admin.composants-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/composants/configuration/composants-delete-dialog.html',
                    controller: '_ComposantsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Component', function(Component) {
                            return Component.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.composants-configuration', null, { reload: 'madera-admin.composants-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
