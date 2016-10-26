(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('QuotationDeleteController',QuotationDeleteController);

    QuotationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Quotation'];

    function QuotationDeleteController($uibModalInstance, entity, Quotation) {
        var vm = this;

        vm.quotation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Quotation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
