/**
 * Created by zhangzf on 16/5/25.
 * updateDate:2016-06-14
 */
'use strict';
var app = angular.module('myApp', ['ngCookies',
    'ngResource',
    'ngSanitize',
    'ui.router',
    'ui.bootstrap',
    'validation.match',
    'ngFileUpload']);
app.controller('FileCenterCtrl', function ($scope, $http) {
    var path = 'http://120.76.132.123:8080/api';
    $http.get(path + '/file/getfiles')
        .success(function (data) {
            $scope.files = data;
        });
    $scope.append = function (i, n, d) {
        $scope.result = 'http://120.76.132.123:8080/play.html?id='+ d.id;
        $scope.files.unshift(d);
    };
    $scope.remove = function (f) {
        $http.delete(path + '/file/del/' + f.id);
        _.remove($scope.files, function (x) {
            return x.id == f.id;
        })
    };
    $scope.search={
        keyword:""
    };
    $scope.submit = function () {
        $http.get(path + '/file/getsearchfile?name='+$scope.search.keyword)
            .success(function (data) {
                $scope.files = [];
                $scope.files = data;
            });
    };
    $scope.success={msg:''};

    $scope.copy = function () {
        var clip = new ZeroClipboard.Client(); //初始化对象
        ZeroClipboard.setMoviePath("ZeroClipboard.swf");//设置flash
        clip.setHandCursor( true ); //设置手型
        clip.addEventListener('mousedown', function (client) {  //创建监听
            clip.setText($scope.result);
            $scope.success.msg="复制成功！"
        });
        clip.glue('d_clip_button'); //将flash覆盖至指定ID的DOM上
        $scope.success.msg="复制成功！"
    };
    $scope.submit();
    $scope.userinfo=function(){
        alert("个人资料页暂未实现");
    }

});
app.directive('uploader', function () {
    return {
        templateUrl: 'apidoc/uploader/uploader.html',
        restrict: 'E',
        replace: true,
        controller: 'UploaderCtrl',
        scope: {
            extension: '@',
            workstate: '@',
            callback: '&',
            callerror: '&',
            path: '@',
            text: '@'
        }
    };
});
app.controller('UploaderCtrl', function ($scope, $attrs, Upload, $timeout) {

    //region ----- Extension / Workstate -----
    $scope.extensionSet = {
        common: {
            size: {max: '2GB'},
            pattern: '.jpg,.png,.pdf,.txt,.mp4'
        },
        image: {size: {max: '5MB'}, pattern: 'image/*'},
        video: {size: {max: '1GB'}, pattern: '.mp4'},
        audio: {size: {max: '1GB'}, pattern: '.mp3'}
    };
    if (!$scope.extensionSet[$scope.extension]) {
        $scope.extension = 'common';
    }
    //console.log($scope.workstate);
    if (!$scope.workstate || !/^(min|mid|max)$/i.test($scope.workstate)) {
        $scope.workstate = 'mid';
    }
    //console.log($scope.workstate);
    $scope.isMedia = function (str) {
        return '.jpg.png.mp4'.indexOf($scope.getExtension(str)) > -1;
    };
    $scope.MediaSet = [{val: 'block', str: '普通'}, {val: 'icon', str: '图标'}, {val: 'inline', str: '小图'}];
    //endregion
    $scope.isWorking = false;
    $scope.isInvalid = '';
    $scope.atSelect = function (file, invalidFiles, myForm) {
        //console.log('atSelect>');
        //console.log('atSelect>', invalidFiles);
        if ($scope.workstate === 'min') {
            //console.log('atSelect>min');
            $scope.atSubmint(myForm, invalidFiles);
        }
        if ($scope.workstate === 'max') {
            $scope.success = null;
        }
    };
    $scope.atClick = function (myForm) {
        console.log('atClick>');
        if ($scope.workstate === 'min') {
            if (!$scope.isWorking) {
                $('#uploader_file').trigger('click');
            }
        }
        else {
            //console.log('atClick>mid|max');
            $scope.atSubmint(myForm);
        }
    };
    $scope.atSubmint = function (from, invalidFiles) {
        //console.log('atSubmint>', from);
        if (from.$valid) {
            //console.log('atSubmint>valid', $scope.file);
            $scope.isInvalid = '';
            $scope.success = null;
            $scope.isWorking = true;
            $scope.errorMsg = '';
            $scope.doUpload($scope.file, invalidFiles);
        }
        else if (from.file.$error.maxSize) {
            //console.log(from.file.$error.maxSize);
            $scope.atInvalid($scope.extensionSet[$scope.extension].size.max);
        }
        else if (from.file.$error.pattern) {
            $scope.atInvalid($scope.extensionSet[$scope.extension].pattern);
        } else {
            $scope.atInvalid(from.file.$error);
        }
    };
    $scope.doUpload = function (file, errFiles) {

        $scope.f = file;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            var __path = $scope.path || 'other';
            file.upload = Upload.upload({
                url: 'http://120.76.132.123:8080/api/file/upload',
                data: {file: file, filepath: 'tas/' + __path + '/'}
            });
            file.upload.then(function (response) {
                var data = response.data;
                var icon = data.rootUrl + data.filePath;
                $timeout(function () {
                    file.result = response.data;
                    $scope.isWorking = false;
                    if ($scope.workstate === 'max') {
                        $scope.success = icon;
                    }
                    else {
                        $scope.doRegist(icon, file.name, data);
                    }
                });
            }, function (response) {
                if (response.status > 0) {
                    $scope.atError('上传失败! 建议刷新页面后重试,原因可能是:' + response.status + ': ' + response.data);
                }
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 *
                    evt.loaded / evt.total));
            });
        }
    };
    $scope.doRegist = function (result, filename, data) {
        //console.log('doRegist>', result)
        $scope.callback({o: result, fileName: filename, data: data});
        $scope.success = null;
    };
    $scope.atCancel = function () {
        $scope.success = null;
    };
    $scope.atSave = function () {
        $scope.doRegist($scope.success);
    };
    $scope.atInvalid = function (val) {
        $scope.isInvalid = val;
        if ($scope.workstate === 'min') {
            //alert('min mode show invalid : ' + val);
            $scope.callerror({o: val});
        }
        $timeout(function () {
            $scope.isInvalid = '';
        }, 5000);
    };
    $scope.atError = function (err) {
        if ($scope.workstate === 'min') {
            $scope.callerror({o: err});
        }
        $scope.errorMsg = err;
    };
    $scope.getExtension = function (filename) {
        if (filename && filename.lastIndexOf('.') > 0) {
            return filename.substring(filename.lastIndexOf('.') + 1).toLocaleLowerCase();
        }
        return '';
    };
});
app.controller('PlayCtrl', function ($scope, $http,$stateParams,$location) {
    var id = $location.absUrl().split("=")[1];
    $http.get("http://120.76.132.123:8080/api/file/getfile/"+id)
        .success(function(x){
            console.log(x);
            x.path='http://120.76.132.123:8080/file/'+ x.path;
            $scope.file=x;
        })
    $scope.close=function(){
        $window.close();
    }
});

