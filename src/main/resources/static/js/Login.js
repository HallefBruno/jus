$(function () {

    const pathname = $("meta[name='local']").attr("content");
    var host = $(location).attr("host");
    var settings = {};

    $("#logar").click(function () {
        if (host.includes("127") || host.includes("localhost")) {
            settings = {
                "url": "http://localhost:8082" + pathname + "auth",
                "method": "POST",
                "contentType": "application/json; charset=utf-8",
                "dataType": "json",
                "data": JSON.stringify({"email": $("#username").val(), "senha": $("#password").val()})
            };
        } else {
            settings = {
                "url": "http://jus-app.herokuapp.com" + pathname + "auth",
                "method": "POST",
                "contentType": "application/json; charset=utf-8",
                "dataType": "json",
                "data": JSON.stringify({"email": $("#username").val(), "senha": $("#password").val()})
            };
        }
        $.ajax(settings).done(function (response) {
            if (response.data.token) {
                const token = response.data.token;
                localStorage.setItem("token", token);
                document.cookie = `Authorization=${token}`;
                location.href = pathname + "dashboard";
            }
        });
    });
});