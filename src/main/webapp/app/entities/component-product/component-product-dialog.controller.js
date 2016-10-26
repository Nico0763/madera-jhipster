(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_productDialogController', Component_productDialogController);

    Component_productDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Component_product', 'Product', 'Component'];

    function Component_productDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Component_product, Product, Component) {
        var vm = this;

        vm.component_product = entity;
        vm.clear = clear;
        vm.save = save;
        vm.products = Product.query();
        vm.components = Component.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.component_product.id !== null) {
                Component_product.update(vm.component_product, onSaveSuccess, onSaveError);
            } else {
                Component_product.save(vm.component_product, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:component_productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
