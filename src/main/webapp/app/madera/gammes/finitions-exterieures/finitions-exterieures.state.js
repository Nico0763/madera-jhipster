(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.finitions-exterieures', {
                parent: 'madera-admin',
                url: 'finitions-exterieures?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/gammes/finitions-exterieures/finitions-exterieures.html',
                        controller: 'FinitionsExterieuresController',
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
              .state('madera-admin.finitions-exterieures.delete', {
            parent: 'madera-admin.finitions-exterieures',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/finitions-exterieures/finitions-exterieures-delete.html',
                    controller: 'FinitionsExterieuresDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Finition_ext', function(Finition_ext) {
                            return Finition_ext.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.finitions-exterieures', null, { reload: 'madera-admin.finitions-exterieures' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
