(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('Cover_typeDetailController', Cover_typeDetailController);

    Cover_typeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Cover_type', 'Assortment'];

    function Cover_typeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Cover_type, Assortment) {
        var vm = this;

        vm.cover_type = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:cover_typeUpdate', function(event, result) {
            vm.cover_type = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
