(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.commandes', {
                parent: 'madera-admin',
                url: 'commandes?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/fournisseurs/commandes/commandes.html',
                        controller: '_CommandesController',
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
                            $translatePartialLoader.addPart('command');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.commandes.new', {
            parent: 'madera-admin.commandes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/commandes/commandes-dialog.html',
                    controller: '_CommandesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                 reference: null,
                                state: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('madera-admin.commandes', null, { reload: 'madera-admin.commandes' });
                }, function() {
                    $state.go('madera-admin.commandes');
                });
            }]
        })
        .state('madera-admin.commandes.edit', {
            parent: 'madera-admin.commandes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/commandes/commandes-dialog.html',
                    controller: '_CommandesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Command', function(Command) {
                            return Command.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.commandes', null, { reload: 'madera-admin.commandes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.commandes.delete', {
            parent: 'madera-admin.commandes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/fournisseurs/commandes/commandes-delete-dialog.html',
                    controller: '_CommandesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Command', function(Command) {
                            return Command.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.commandes', null, { reload: 'madera-admin.commandes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
