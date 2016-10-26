(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Module_componentDeleteController',Module_componentDeleteController);

    Module_componentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Module_component'];

    function Module_componentDeleteController($uibModalInstance, entity, Module_component) {
        var vm = this;

        vm.module_component = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Module_component.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
