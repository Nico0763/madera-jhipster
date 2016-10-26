(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Unit_usedDeleteController',Unit_usedDeleteController);

    Unit_usedDeleteController.$inject = ['$uibModalInstance', 'entity', 'Unit_used'];

    function Unit_usedDeleteController($uibModalInstance, entity, Unit_used) {
        var vm = this;

        vm.unit_used = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Unit_used.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
