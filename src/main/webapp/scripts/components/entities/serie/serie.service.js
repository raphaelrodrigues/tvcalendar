'use strict';

angular.module('tvcalendarApp')
    .factory('Serie', function ($resource, DateUtils) {
        return $resource('api/series/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('SeriePreview', function ($resource, DateUtils) {
    return $resource('api/series/preview/:title', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' }
    });
});
