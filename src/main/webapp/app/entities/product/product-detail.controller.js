(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Product', 'Pattern', 'Module', 'Quotation', 'Component_product'];

    function ProductDetailController($scope, $rootScope, $stateParams, previousState, entity, Product, Pattern, Module, Quotation, Component_product) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
