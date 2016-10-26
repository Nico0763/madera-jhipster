(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_productDeleteController',Component_productDeleteController);

    Component_productDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component_product'];

    function Component_productDeleteController($uibModalInstance, entity, Component_product) {
        var vm = this;

        vm.component_product = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Component_product.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
