(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('DeadlineDetailController', DeadlineDetailController);

    DeadlineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Deadline', 'Quotation'];

    function DeadlineDetailController($scope, $rootScope, $stateParams, previousState, entity, Deadline, Quotation) {
        var vm = this;

        vm.deadline = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:deadlineUpdate', function(event, result) {
            vm.deadline = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
