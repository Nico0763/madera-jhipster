'use strict';

describe('Controller Tests', function() {

    describe('Order_component Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrder_component, MockOrder, MockComponent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrder_component = jasmine.createSpy('MockOrder_component');
            MockOrder = jasmine.createSpy('MockOrder');
            MockComponent = jasmine.createSpy('MockComponent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Order_component': MockOrder_component,
                'Order': MockOrder,
                'Component': MockComponent
            };
            createController = function() {
                $injector.get('$controller')("Order_componentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:order_componentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
