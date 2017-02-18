(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('PatternDeleteController',PatternDeleteController);

    PatternDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pattern'];

    function PatternDeleteController($uibModalInstance, entity, Pattern) {
        var vm = this;

        vm.pattern = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pattern.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
