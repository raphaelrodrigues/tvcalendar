'use strict';

angular.module('tvcalendarApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


