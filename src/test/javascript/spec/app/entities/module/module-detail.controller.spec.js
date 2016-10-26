'use strict';

describe('Controller Tests', function() {

    describe('Module Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModule, MockPrincipal_cross_section, MockModule_nature, MockProduct, MockAssortment, MockModule_component;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModule = jasmine.createSpy('MockModule');
            MockPrincipal_cross_section = jasmine.createSpy('MockPrincipal_cross_section');
            MockModule_nature = jasmine.createSpy('MockModule_nature');
            MockProduct = jasmine.createSpy('MockProduct');
            MockAssortment = jasmine.createSpy('MockAssortment');
            MockModule_component = jasmine.createSpy('MockModule_component');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Module': MockModule,
                'Principal_cross_section': MockPrincipal_cross_section,
                'Module_nature': MockModule_nature,
                'Product': MockProduct,
                'Assortment': MockAssortment,
                'Module_component': MockModule_component
            };
            createController = function() {
                $injector.get('$controller')("ModuleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:moduleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
