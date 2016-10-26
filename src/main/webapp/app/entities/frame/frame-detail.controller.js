(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('FrameDetailController', FrameDetailController);

    FrameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Frame', 'Assortment'];

    function FrameDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Frame, Assortment) {
        var vm = this;

        vm.frame = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('maderaApp:frameUpdate', function(event, result) {
            vm.frame = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
