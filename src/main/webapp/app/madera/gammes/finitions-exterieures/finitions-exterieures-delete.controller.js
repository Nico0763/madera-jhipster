(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FinitionsExterieuresDeleteController',FinitionsExterieuresDeleteController);

    FinitionsExterieuresDeleteController.$inject = ['$uibModalInstance', 'entity', 'Finition_ext', 'ImageRemoveFile'];

    function FinitionsExterieuresDeleteController($uibModalInstance, entity, Finition_ext,ImageRemoveFile) {
        var vm = this;

        vm.finition_ext = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
 
        function confirmDelete (id) {
            var deleteId = vm.finition_ext.url;

            if(deleteId != "" && deleteId != null && deleteId != undefined)
            {   
                ImageRemoveFile.delete({url:deleteId});
            }

            Finition_ext.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
