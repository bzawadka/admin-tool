'use strict';

describe('BrokerStatus Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockBrokerStatus;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockBrokerStatus = jasmine.createSpy('MockBrokerStatus');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'BrokerStatus': MockBrokerStatus
        };
        createController = function() {
            $injector.get('$controller')("BrokerStatusDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adminToolApp:brokerStatusUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
