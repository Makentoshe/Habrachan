$('#captcha-s-field').removeClass('hidden');
//$('#recaptcha_response_field').attr('data-required', 'true');
show_global_notice('There&apos;s no user with such email and password', 'error');
reloadRecaptcha();
setTimeout(function () {
    toggleSubmitButton($('#login_form'));
}, 200);
