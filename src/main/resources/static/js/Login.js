/* global Swal */

$(function () {

    const pathname = $("meta[name='local']").attr("content");
    var host = $(location).attr("host");
    var settings = {};
    
    $("#username").focus();
    
    $("#logar").click(function () {
        
        if ($("#username").val() && $("#password").val()) {
            
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

            $.ajax({
                url: settings.url,
                type: settings.method,
                contentType: settings.contentType,
                dataType: settings.dataType,
                data: settings.data
            }).done(function (response) {
                if (response.data.token) {
                    const token = response.data.token;
                    localStorage.setItem("token", token);
                    document.cookie = `Authorization=${token}`;
                    location.href = pathname + "dashboard";
                }
            }).fail(function (jqXHR, textStatus, response) {
                if(jqXHR.responseJSON.message) {
                    Swal.fire('Atenção',`${jqXHR.responseJSON.message}`,'warning');
                } else if(jqXHR.responseJSON.status === 401) {
                    Swal.fire('Atenção',"Acesso negado. Você deve estar autenticado no sistema para acessar a URL solicitada!",'warning');
                }
                window.console.log(jqXHR);
            });
            
        } else {
            mensagemCamposObrigatorio();
        }
    });
    
    loadGif();
});

function mensagemCamposObrigatorio() {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 5000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        }
    });

    Toast.fire({
        icon: 'warning',
        title: 'Email e senha obrigatório!'
    });
}

function loadGif() {
    $(document).ajaxSend(function (event, jqXHR, settings) {
        $("div.loading").addClass("show");
    });

    $(document).ajaxComplete(function (event, jqXHR, settings) {
        $("div.loading").removeClass("show");
    });
}