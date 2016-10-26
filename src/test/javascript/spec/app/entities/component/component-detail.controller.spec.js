'use strict';

describe('Controller Tests', function() {

    describe('Component Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComponent, MockComponent_product, MockModule_component, MockProvider, MockComponent_nature, MockCommand_component;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComponent = jasmine.createSpy('MockComponent');
            MockComponent_product = jasmine.createSpy('MockComponent_product');
            MockModule_component = jasmine.createSpy('MockModule_component');
            MockProvider = jasmine.createSpy('MockProvider');
            MockComponent_nature = jasmine.createSpy('MockComponent_nature');
            MockCommand_component = jasmine.createSpy('MockCommand_component');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Component': MockComponent,
                'Component_product': MockComponent_product,
                'Module_component': MockModule_component,
                'Provider': MockProvider,
                'Component_nature': MockComponent_nature,
                'Command_component': MockCommand_component
            };
            createController = function() {
                $injector.get('$controller')("ComponentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:componentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
