(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('CoupesPrincipeDeleteController',CoupesPrincipeDeleteController);

    CoupesPrincipeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Principal_cross_section'];

    function CoupesPrincipeDeleteController($uibModalInstance, entity, Principal_cross_section) {
        var vm = this;

        vm.principal_cross_section = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Principal_cross_section.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
