(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.types-isolant', {
                parent: 'madera-admin',
                url: 'types-isolant?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/gammes/types-isolant/types-isolant.html',
                        controller: 'TypesIsolantController',
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
              .state('madera-admin.types-isolant.delete', {
            parent: 'madera-admin.types-isolant',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/types-isolant/types-isolant-delete.html',
                    controller: 'TypesIsolantDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Insulating_type', function(Insulating_type) {
                            return Insulating_type.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.types-isolant', null, { reload: 'madera-admin.types-isolant' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
