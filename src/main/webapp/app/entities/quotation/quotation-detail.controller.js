(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('QuotationDetailController', QuotationDetailController);

    QuotationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Quotation', 'Assortment', 'Deadline', 'Product', 'Customer'];

    function QuotationDetailController($scope, $rootScope, $stateParams, previousState, entity, Quotation, Assortment, Deadline, Product, Customer) {
        var vm = this;

        vm.quotation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('maderaApp:quotationUpdate', function(event, result) {
            vm.quotation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
