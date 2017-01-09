(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.coupes-principe', {
                parent: 'madera-admin',
                url: 'coupes-principe?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/modules/coupes-principe/coupes-principe.html',
                        controller: 'CoupesPrincipeController',
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
                            $translatePartialLoader.addPart('principal_cross_section');
                            $translatePartialLoader.addPart('global');
                            return $translate.refresh();
                        }]
                }
            }) 
        .state('madera-admin.coupes-principe.new', {
            parent: 'madera-admin.coupes-principe',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/coupes-principe/coupes-principe-dialog.html',
                    controller: 'CoupesPrincipeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                 name: null,
                               description: null,
                               image:null,
                                imageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('madera-admin.coupes-principe', null, { reload: 'madera-admin.coupes-principe' });
                }, function() {
                    $state.go('madera-admin.coupes-principe');
                });
            }]
        })
        .state('madera-admin.coupes-principe.edit', {
            parent: 'madera-admin.coupes-principe',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/coupes-principe/coupes-principe-dialog.html',
                    controller: 'CoupesPrincipeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Principal_cross_section', function(Principal_cross_section) {
                            return Principal_cross_section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.coupes-principe', null, { reload: 'madera-admin.coupes-principe' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.coupes-principe.delete', {
            parent: 'madera-admin.coupes-principe',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/coupes-principe/coupes-principe-delete-dialog.html',
                    controller: 'CoupesPrincipeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Principal_cross_section', function(Principal_cross_section) {
                            return Principal_cross_section.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.coupes-principe', null, { reload: 'madera-admin.coupes-principe' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
