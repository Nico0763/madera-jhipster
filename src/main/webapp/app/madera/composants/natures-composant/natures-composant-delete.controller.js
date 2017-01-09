(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('NaturesComposantDeleteController',NaturesComposantDeleteController);

    NaturesComposantDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component_nature'];

    function NaturesComposantDeleteController($uibModalInstance, entity, Component_nature) {
        var vm = this;

        vm.composant_nature = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Component_nature.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
