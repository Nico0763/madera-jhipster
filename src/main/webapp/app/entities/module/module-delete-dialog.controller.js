(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ModuleDeleteController',ModuleDeleteController);

    ModuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Module'];

    function ModuleDeleteController($uibModalInstance, entity, Module) {
        var vm = this;

        vm.module = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Module.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
