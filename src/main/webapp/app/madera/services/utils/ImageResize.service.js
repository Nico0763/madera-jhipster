(function() {
    'use strict';
    angular
        .module('maderaApp')
        .factory('ImageResize', ImageResize);

    ImageResize.$inject = [];

    function ImageResize () {


      var methods = {};
        methods.resize = function(image, width, height, callback)
        {
           var myCanvas = document.createElement("canvas");
           myCanvas.width = width;
           myCanvas.height = height;
            var ctx = myCanvas.getContext('2d');
            var img = new Image();
            img.onload = function(){
                ctx.drawImage(img, 0, 0,width,height);

                callback(dataURLtoFile(myCanvas.toDataURL('image/png'),'a.png'));
            };

            img.src = URL.createObjectURL(image);
        }

        var dataURLtoFile = function(dataurl, filename)
        {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                while(n--){
                    u8arr[n] = bstr.charCodeAt(n);
                }
                return new File([u8arr], filename, {type:mime});
        }
        return methods;
    }
})();
