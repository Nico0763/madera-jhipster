(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Component_natureDeleteController',Component_natureDeleteController);

    Component_natureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component_nature'];

    function Component_natureDeleteController($uibModalInstance, entity, Component_nature) {
        var vm = this;

        vm.component_nature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Component_nature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
