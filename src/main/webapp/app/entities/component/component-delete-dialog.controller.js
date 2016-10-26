(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ComponentDeleteController',ComponentDeleteController);

    ComponentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component'];

    function ComponentDeleteController($uibModalInstance, entity, Component) {
        var vm = this;

        vm.component = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Component.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
