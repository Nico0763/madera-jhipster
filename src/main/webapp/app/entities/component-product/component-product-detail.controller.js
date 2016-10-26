(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_productDetailController', Component_productDetailController);

    Component_productDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Component_product', 'Product', 'Component'];

    function Component_productDetailController($scope, $rootScope, $stateParams, previousState, entity, Component_product, Product, Component) {
        var vm = this;

        vm.component_product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:component_productUpdate', function(event, result) {
            vm.component_product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
