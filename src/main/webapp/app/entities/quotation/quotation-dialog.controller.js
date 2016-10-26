(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('QuotationDialogController', QuotationDialogController);

    QuotationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Quotation', 'Assortment', 'Deadline', 'Product', 'Customer'];

    function QuotationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Quotation, Assortment, Deadline, Product, Customer) {
        var vm = this;

        vm.quotation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.assortments = Assortment.query();
        vm.deadlines = Deadline.query();
        vm.products = Product.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.quotation.id !== null) {
                Quotation.update(vm.quotation, onSaveSuccess, onSaveError);
            } else {
                Quotation.save(vm.quotation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:quotationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
