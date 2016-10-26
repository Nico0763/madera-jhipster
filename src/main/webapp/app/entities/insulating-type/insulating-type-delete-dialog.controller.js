(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Insulating_typeDeleteController',Insulating_typeDeleteController);

    Insulating_typeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Insulating_type'];

    function Insulating_typeDeleteController($uibModalInstance, entity, Insulating_type) {
        var vm = this;

        vm.insulating_type = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Insulating_type.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
