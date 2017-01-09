(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.fournisseurs-configuration', {
                parent: 'madera-admin',
                url: 'fournisseurs-configuration?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/fournisseurs/configuration/fournisseurs-configuration.html',
                        controller: '_FournisseursConfigurationController',
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
                            $translatePartialLoader.addPart('provider');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.fournisseurs-configuration.new', {
            parent: 'madera-admin.fournisseurs-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/configuration/fournisseurs-dialog.html',
                    controller: '_FournisseursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                 name: null,
                                address: null,
                                pc: null,
                                city: null,
                                country: null,
                                phone_number: null,
                                mail: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('madera-admin.fournisseurs-configuration', null, { reload: 'madera-admin.fournisseurs-configuration' });
                }, function() {
                    $state.go('madera-admin.fournisseurs-configuration');
                });
            }]
        })
        .state('madera-admin.fournisseurs-configuration.edit', {
            parent: 'madera-admin.fournisseurs-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/configuration/fournisseurs-dialog.html',
                    controller: '_FournisseursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Provider', function(Provider) {
                            return Provider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.fournisseurs-configuration', null, { reload: 'madera-admin.fournisseurs-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.fournisseurs-configuration.delete', {
            parent: 'madera-admin.fournisseurs-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/configuration/fournisseurs-delete-dialog.html',
                    controller: '_FournisseursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Provider', function(Provider) {
                            return Provider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.fournisseurs-configuration', null, { reload: 'madera-admin.fournisseurs-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
