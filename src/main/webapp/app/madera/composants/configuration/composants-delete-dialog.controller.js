(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_ComposantsDeleteController',_ComposantsDeleteController);

    _ComposantsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component'];

    function _ComposantsDeleteController($uibModalInstance, entity, Component) {
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
