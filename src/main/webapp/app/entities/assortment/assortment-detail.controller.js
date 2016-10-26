(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('AssortmentDetailController', AssortmentDetailController);

    AssortmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Assortment', 'Finition_ext', 'Insulating_type', 'Frame', 'Cover_type', 'Module', 'Pattern', 'Quotation'];

    function AssortmentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Assortment, Finition_ext, Insulating_type, Frame, Cover_type, Module, Pattern, Quotation) {
        var vm = this;

        vm.assortment = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:assortmentUpdate', function(event, result) {
            vm.assortment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
