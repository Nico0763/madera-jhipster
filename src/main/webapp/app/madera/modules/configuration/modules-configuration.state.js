(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.modules-configuration', {
                parent: 'madera-admin',
                url: 'modules-configuration?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/modules/configuration/modules-configuration.html',
                        controller: '_ModulesConfigurationController',
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
                            $translatePartialLoader.addPart('module');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.modules-configuration.new', {
            parent: 'madera-admin.modules-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/configuration/module-dialog.html',
                    controller: '_ModuleDialogController',
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
                    $state.go('madera-admin.modules-configuration', null, { reload: 'madera-admin.modules-configuration' });
                }, function() {
                    $state.go('madera-admin.modules-configuration');
                });
            }]
        })
        .state('madera-admin.modules-configuration.edit', {
            parent: 'madera-admin.modules-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/configuration/module-dialog.html',
                    controller: '_ModuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Module', function(Module) {
                            return Module.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.modules-configuration', null, { reload: 'madera-admin.modules-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.modules-configuration.delete', {
            parent: 'madera-admin.modules-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/configuration/module-delete-dialog.html',
                    controller: '_ModuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Module', function(Module) {
                            return Module.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.modules-configuration', null, { reload: 'madera-admin.modules-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
