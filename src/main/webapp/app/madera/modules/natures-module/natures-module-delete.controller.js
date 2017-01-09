(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('NaturesModuleDeleteController',NaturesModuleDeleteController);

    NaturesModuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Module_nature'];

    function NaturesModuleDeleteController($uibModalInstance, entity, Module_nature) {
        var vm = this;

        vm.module_nature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Module_nature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
