(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_ModuleDeleteController',_ModuleDeleteController);

    _ModuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Module'];

    function _ModuleDeleteController($uibModalInstance, entity, Module) {
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
