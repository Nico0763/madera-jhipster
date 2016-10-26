(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Command_componentDeleteController',Command_componentDeleteController);

    Command_componentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Command_component'];

    function Command_componentDeleteController($uibModalInstance, entity, Command_component) {
        var vm = this;

        vm.command_component = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Command_component.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
