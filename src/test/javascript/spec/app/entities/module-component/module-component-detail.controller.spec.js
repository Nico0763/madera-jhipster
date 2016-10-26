'use strict';

describe('Controller Tests', function() {

    describe('Module_component Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModule_component, MockModule, MockComponent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModule_component = jasmine.createSpy('MockModule_component');
            MockModule = jasmine.createSpy('MockModule');
            MockComponent = jasmine.createSpy('MockComponent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Module_component': MockModule_component,
                'Module': MockModule,
                'Component': MockComponent
            };
            createController = function() {
                $injector.get('$controller')("Module_componentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:module_componentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
