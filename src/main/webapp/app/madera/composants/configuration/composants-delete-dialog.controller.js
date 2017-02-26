(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('_ComposantsDeleteController',_ComposantsDeleteController);

    _ComposantsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Component', 'ImageRemoveFile'];

    function _ComposantsDeleteController($uibModalInstance, entity, Component, ImageRemoveFile) {
        var vm = this;

        vm.component = entity; 
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {

            var deleteId = vm.component.url;

            if(deleteId != "" && deleteId != null && deleteId != undefined)
            {   
                ImageRemoveFile.delete({url:deleteId});
            }


            Component.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
