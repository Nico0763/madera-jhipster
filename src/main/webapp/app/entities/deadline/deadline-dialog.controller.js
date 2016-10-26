(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('DeadlineDialogController', DeadlineDialogController);

    DeadlineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deadline', 'Quotation'];

    function DeadlineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Deadline, Quotation) {
        var vm = this;

        vm.deadline = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.quotations = Quotation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.deadline.id !== null) {
                Deadline.update(vm.deadline, onSaveSuccess, onSaveError);
            } else {
                Deadline.save(vm.deadline, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('maderaApp:deadlineUpdate', result);
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
