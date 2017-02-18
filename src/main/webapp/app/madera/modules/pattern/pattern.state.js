(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.pattern', {
                parent: 'madera-admin',
                url: 'pattern?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/modules/pattern/pattern.html',
                        controller: '_PatternController',
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
            .state('madera-admin.pattern.delete', {
            parent: 'madera-admin.pattern',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/pattern/pattern-delete.html',
                    controller: 'PatternDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pattern', function(Pattern) {
                            return Pattern.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.pattern', null, { reload: 'madera-admin.pattern' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('madera-admin.pattern.products', {
            parent: 'madera-admin.pattern',
            url: '/{id}/products',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/pattern/pattern-products.html',
                    controller: 'PatternProductsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pattern', function(Pattern) {
                            return Pattern.get({id : $stateParams.id}).$promise;
                        }]
                    }
                        
                }).result.then(function() {
                    $state.go('madera-admin.pattern', null, { reload: 'madera-admin.pattern' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
         .state('madera-admin.pattern.products.components', {
            parent: 'madera-admin.pattern.products',
            url: '/{id_product}/components',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/modules/pattern/product-components.html',
                    controller: 'ProductComponentsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id_product}).$promise;
                        }]
                    }
                        
                }).result.then(function() {
                    $state.go('madera-admin.pattern.products', {id:$stateParams.id});
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
