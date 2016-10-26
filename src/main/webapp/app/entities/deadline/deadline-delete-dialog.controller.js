(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('DeadlineDeleteController',DeadlineDeleteController);

    DeadlineDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deadline'];

    function DeadlineDeleteController($uibModalInstance, entity, Deadline) {
        var vm = this;

        vm.deadline = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Deadline.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
