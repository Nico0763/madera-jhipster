'use strict';

describe('Controller Tests', function() {

    describe('Module_nature Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockModule_nature, MockModule, MockUnit_used;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockModule_nature = jasmine.createSpy('MockModule_nature');
            MockModule = jasmine.createSpy('MockModule');
            MockUnit_used = jasmine.createSpy('MockUnit_used');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Module_nature': MockModule_nature,
                'Module': MockModule,
                'Unit_used': MockUnit_used
            };
            createController = function() {
                $injector.get('$controller')("Module_natureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:module_natureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
