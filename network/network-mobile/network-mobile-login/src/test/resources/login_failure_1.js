form_errors_show('#login_form', {
    'email': 'Введите корректный e-mail', 'password': 'Введите пароль'
});

reloadRecaptcha();
setTimeout(function () {
    toggleSubmitButton($('#login_form'));
}, 200);