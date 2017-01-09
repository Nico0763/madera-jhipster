(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_FournisseursDeleteController',_FournisseursDeleteController);

    _FournisseursDeleteController.$inject = ['$uibModalInstance', 'entity', 'Provider'];

    function _FournisseursDeleteController($uibModalInstance, entity, Provider) {
        var vm = this;

        vm.provider = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Provider.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
