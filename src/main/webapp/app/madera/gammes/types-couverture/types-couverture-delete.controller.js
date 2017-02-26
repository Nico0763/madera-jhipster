(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('TypesCouvertureDeleteController',TypesCouvertureDeleteController);

    TypesCouvertureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cover_type', 'ImageRemoveFile'];

    function TypesCouvertureDeleteController($uibModalInstance, entity, Cover_type, ImageRemoveFile) {
        var vm = this;

        vm.cover_type = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {


            var deleteId = vm.cover_type.url;

            if(deleteId != "" && deleteId != null && deleteId != undefined)
            {   
                ImageRemoveFile.delete({url:deleteId});
            }

            Cover_type.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
