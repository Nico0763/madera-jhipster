(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('HuisseriesDeleteController',HuisseriesDeleteController);

    HuisseriesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Frame', 'ImageRemoveFile'];

    function HuisseriesDeleteController($uibModalInstance, entity, Frame, ImageRemoveFile) {
        var vm = this;

        vm.frame = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {


            var deleteId = vm.frame.url;

            if(deleteId != "" && deleteId != null && deleteId != undefined)
            {   
                ImageRemoveFile.delete({url:deleteId});
            }

            Frame.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
