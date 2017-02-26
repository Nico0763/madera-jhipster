(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('PatternDeleteController',PatternDeleteController);

    PatternDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pattern', 'ImageRemoveFile'];

    function PatternDeleteController($uibModalInstance, entity, Pattern, ImageRemoveFile) {
        var vm = this;

        vm.pattern = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            var deleteId = vm.pattern.url;

            if(deleteId != "" && deleteId != null && deleteId != undefined)
            {   
                ImageRemoveFile.delete({url:deleteId});
            }


            Pattern.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
