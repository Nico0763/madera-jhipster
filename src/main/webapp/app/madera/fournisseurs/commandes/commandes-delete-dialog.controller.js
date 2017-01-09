(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_CommandesDeleteController',_CommandesDeleteController);

    _CommandesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Command'];

    function _CommandesDeleteController($uibModalInstance, entity, Command) {
        var vm = this;

        vm.command = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Command.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
