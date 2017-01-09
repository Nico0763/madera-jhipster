(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('TypesCouvertureDeleteController',TypesCouvertureDeleteController);

    TypesCouvertureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cover_type'];

    function TypesCouvertureDeleteController($uibModalInstance, entity, Cover_type) {
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
