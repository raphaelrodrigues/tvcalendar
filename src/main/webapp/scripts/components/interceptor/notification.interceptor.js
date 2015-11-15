 'use strict';

angular.module('tvcalendarApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-tvcalendarApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-tvcalendarApp-params')});
                }
                return response;
            }
        };
    });
