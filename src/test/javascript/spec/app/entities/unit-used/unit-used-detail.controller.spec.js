'use strict';

describe('Controller Tests', function() {

    describe('Unit_used Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUnit_used, MockModule_nature, MockComponent_nature;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUnit_used = jasmine.createSpy('MockUnit_used');
            MockModule_nature = jasmine.createSpy('MockModule_nature');
            MockComponent_nature = jasmine.createSpy('MockComponent_nature');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Unit_used': MockUnit_used,
                'Module_nature': MockModule_nature,
                'Component_nature': MockComponent_nature
            };
            createController = function() {
                $injector.get('$controller')("Unit_usedDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:unit_usedUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
