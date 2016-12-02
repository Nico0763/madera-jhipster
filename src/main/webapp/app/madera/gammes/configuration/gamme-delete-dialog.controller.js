(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('GammeDeleteController',GammeDeleteController);

    GammeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Assortment'];

    function GammeDeleteController($uibModalInstance, entity, Assortment) {
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
