'use strict';

describe('MessageVersion Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMessageVersion;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMessageVersion = jasmine.createSpy('MockMessageVersion');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MessageVersion': MockMessageVersion
        };
        createController = function() {
            $injector.get('$controller')("MessageVersionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adminToolApp:messageVersionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
