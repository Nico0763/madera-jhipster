(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Finition_extDeleteController',Finition_extDeleteController);

    Finition_extDeleteController.$inject = ['$uibModalInstance', 'entity', 'Finition_ext'];

    function Finition_extDeleteController($uibModalInstance, entity, Finition_ext) {
        var vm = this;

        vm.finition_ext = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Finition_ext.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
