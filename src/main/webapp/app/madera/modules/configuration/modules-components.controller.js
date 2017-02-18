(function() {
    'use strict';

    angular
        .module('maderaApp')
        .controller('ModulesComponentsController', ModulesComponentController);

    ModulesComponentController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity',  'Module', 'getComponentsModule','Module_component', 'Component'];

    function ModulesComponentController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Module, getComponentsModule, Module_component, Component) {
        var vm = this;

        vm.module = entity;

        vm.clear = clear;
       vm.save = save;

        vm.components = Component.query();
        vm.cps = null;

        vm.editData = {quantity:null};
        vm.addData = {component:null, quantity:null};

        loadComponents();
        function loadComponents()
        {
            vm.cps = getComponentsModule.query({id:entity.id});
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }






        /*** Edition ***/

        vm.selectElement = function(element, data)
        {
            vm.select = element;
            data.quantity = element.quantity;
        }

        vm.deleteComponent = function(element)
        {
            Module_component.delete({id:element.id}, function(data)
            {

                 vm.cps.splice(vm.cps.indexOf(element, 1));
            });
        }

        function save(data,type)
        {


            if(type == 1 && vm.select != null)
            {
            
                    vm.select.quantity= data.quantity;
            
                Module_component.update(vm.select, function(success)
                    {
                        console.debug(success)
                    }, function(error) {
                        console.debug(error);
                    });
                vm.select = null;
                
            }
            else if(type==2)
            {
                Module_component.save({module:vm.module, component:data.component, quantity:data.quantity}, function(data)
                    {
                        vm.cps.push(data);
                        $state.reload();
                    }, function(error) {
                        console.debug(error);
                    });
            }

            data.quantity = null;
            data.component = null;

        }
    }
})();
