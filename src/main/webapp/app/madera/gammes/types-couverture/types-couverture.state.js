(function() {
    'use strict';

    angular
        .module('maderaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];
 
    function stateConfig($stateProvider) {
        $stateProvider
             .state('madera-admin.types-couverture', {
                parent: 'madera-admin',
                url: 'types-couverture?page&sort&search',
                data: {
                authorities: ['ROLE_USER']
                },
                views: {
                    'main@madera-admin': {
                        templateUrl: 'app/madera/gammes/types-couverture/types-couverture.html',
                        controller: 'TypesCouvertureController',
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
              .state('madera-admin.types-couverture.delete', {
            parent: 'madera-admin.types-couverture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/madera/gammes/types-couverture/types-couverture-delete.html',
                    controller: 'TypesCouvertureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cover_type', function(Cover_type) {
                            return Cover_type.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('madera-admin.types-couverture', null, { reload: 'madera-admin.types-couverture' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
