(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Cover_typeDeleteController',Cover_typeDeleteController);

    Cover_typeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cover_type'];

    function Cover_typeDeleteController($uibModalInstance, entity, Cover_type) {
        var vm = this;

        vm.cover_type = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cover_type.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
