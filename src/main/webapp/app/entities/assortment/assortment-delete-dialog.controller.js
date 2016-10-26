(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('AssortmentDeleteController',AssortmentDeleteController);

    AssortmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Assortment'];

    function AssortmentDeleteController($uibModalInstance, entity, Assortment) {
        var vm = this;

        vm.assortment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Assortment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
