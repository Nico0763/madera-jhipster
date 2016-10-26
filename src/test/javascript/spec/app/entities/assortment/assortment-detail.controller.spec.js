'use strict';

describe('Controller Tests', function() {

    describe('Assortment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAssortment, MockFinition_ext, MockInsulating_type, MockFrame, MockCover_type, MockModule, MockPattern, MockQuotation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAssortment = jasmine.createSpy('MockAssortment');
            MockFinition_ext = jasmine.createSpy('MockFinition_ext');
            MockInsulating_type = jasmine.createSpy('MockInsulating_type');
            MockFrame = jasmine.createSpy('MockFrame');
            MockCover_type = jasmine.createSpy('MockCover_type');
            MockModule = jasmine.createSpy('MockModule');
            MockPattern = jasmine.createSpy('MockPattern');
            MockQuotation = jasmine.createSpy('MockQuotation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Assortment': MockAssortment,
                'Finition_ext': MockFinition_ext,
                'Insulating_type': MockInsulating_type,
                'Frame': MockFrame,
                'Cover_type': MockCover_type,
                'Module': MockModule,
                'Pattern': MockPattern,
                'Quotation': MockQuotation
            };
            createController = function() {
                $injector.get('$controller')("AssortmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'maderaApp:assortmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
