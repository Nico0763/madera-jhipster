(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FrameDeleteController',FrameDeleteController);

    FrameDeleteController.$inject = ['$uibModalInstance', 'entity', 'Frame'];

    function FrameDeleteController($uibModalInstance, entity, Frame) {
        var vm = this;

        vm.frame = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Frame.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
