'use strict';

describe('Controller Tests', function() {

    describe('Component_nature Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComponent_nature, MockComponent, MockUnit_used;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComponent_nature = jasmine.createSpy('MockComponent_nature');
            MockComponent = jasmine.createSpy('MockComponent');
            MockUnit_used = jasmine.createSpy('MockUnit_used');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Component_nature': MockComponent_nature,
                'Component': MockComponent,
                'Unit_used': MockUnit_used
            };
            createController = function() {
                $injector.get('$controller')("Component_natureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:component_natureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
