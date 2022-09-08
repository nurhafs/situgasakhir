$(function () {
    var $newPassword = $(".newPass[type='password']");
    var newPassword = $("#inputValidationEx2").val();
    var $confPassword = $(".confPass[type='password']");
    var confPassword = $("#inputValidationEx3").val();
    var oldPassword = $("#old-password").val();
    var $oldPassword = $(".old-pass[type='password']");
    var $newPasswordAlert = $(".new-password-alert");
    var $newRequirements = $(".new-requirements");
    var $submitPass = $(".submit-pass");
    let leng, num, equalPass;
    var $leng = $(".leng");
    var $num = $(".num");
    var numbers = "0123456789";
    var $confPasswordAlert = $(".conf-password-alert");
    var $confRequirements = $(".conf-requirements");
    var $equalPass = $(".equal-pass");

    $newRequirements.addClass("wrong");
    $newPassword.on("focus", function(){$newPasswordAlert.show();});

    $newPassword.on("input blur", function (e) {
        var el = $(this);
        var val = el.val();
        $newPasswordAlert.show();

        if (val.length < 8) {
            leng = false;
        }
        else if (val.length > 7) {
            leng=true;
        }

        num = false;
        for(var i=0; i<val.length;i++){
            for(var j=0; j<numbers.length; j++){
                if(val[i]==numbers[j]){
                    num = true;
                }
            }
        }

        newPassword = val;

        if(leng == true && num == true){
            $(this).addClass("valid").removeClass("invalid");
            $newRequirements.removeClass("wrong").addClass("good");
            $newPasswordAlert.removeClass("alert-warning").addClass("alert-success");
        }
        else
        {
            $(this).addClass("invalid").removeClass("valid");
            $newPasswordAlert.removeClass("alert-success").addClass("alert-warning");

            if(leng==false){$leng.addClass("wrong").removeClass("good");}
            else{$leng.addClass("good").removeClass("wrong");}

            if(num==false){$num.addClass("wrong").removeClass("good");}
            else{$num.addClass("good").removeClass("wrong");}
        }

        if (oldPassword != "" && confPassword != "" && newPassword == confPassword) {
            $submitPass.removeClass("disabled");
            $submitPass.removeClass("btn-secondary");
            $submitPass.addClass("btn-primary");
        } else {
            $submitPass.addClass("disabled");
            $submitPass.addClass("btn-secondary");
        }


        if(e.type == "blur"){
            $newPasswordAlert.hide();
        }
    });

    $oldPassword.on("input blur",
        function (e) {
            var el = $(this);
            var val = el.val();
            oldPassword = val;

            if (oldPassword != "" && confPassword != "" && newPassword == confPassword) {
                $submitPass.removeClass("disabled");
                $submitPass.removeClass("btn-secondary");
                $submitPass.addClass("btn-primary");
            } else {
                $submitPass.addClass("disabled");
                $submitPass.addClass("btn-secondary");
            }

        }
    )

    $confRequirements.addClass("wrong");
    $confPassword.on("focus", function(){$confPasswordAlert.show();});

    $confPassword.on("input blur", function (e) {
        var el = $(this);
        var val = el.val();
        confPassword = val;
        $confPasswordAlert.show();

        if (newPassword != "" && confPassword != "" && newPassword == confPassword) {
            equalPass = true
        } else {
            equalPass = false;
        }

        if (oldPassword != "" && equalPass == true) {
            $submitPass.removeClass("disabled");
            $submitPass.removeClass("btn-secondary");
            $submitPass.addClass("btn-primary");
        } else {
            $submitPass.addClass("disabled");
            $submitPass.addClass("btn-secondary");
        }

        if (equalPass == true) {
            $(this).addClass("valid").removeClass("invalid");
            $confRequirements.removeClass("wrong").addClass("good");
            $confPasswordAlert.removeClass("alert-warning").addClass("alert-success");
        } else {
            $(this).addClass("invalid").removeClass("valid");
            $confPasswordAlert.removeClass("alert-success").addClass("alert-warning");

            if(equalPass==false){$equalPass.addClass("wrong").removeClass("good");}
            else{$equalPass.addClass("good").removeClass("wrong");}
        }

        if(e.type == "blur"){
            $confPasswordAlert.hide();
        }
    });

});