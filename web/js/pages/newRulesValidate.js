jQuery.validator.setDefaults({
    ignore: 'input[type=hidden], .select2-search__field', // ignore hidden fields
    errorClass: 'validation-error-label',
    successClass: 'validation-valid-label',
    highlight: function (element, errorClass) {
        $(element).removeClass(errorClass);
    },
    unhighlight: function (element, errorClass) {
        $(element).removeClass(errorClass);
    },

    // Different components require proper error label placement
    errorPlacement: function (error, element) {

        // Styled checkboxes, radios, bootstrap switch
        if (element.parents('div').hasClass("checker") || element.parents('div').hasClass("choice") || element.parent().hasClass('bootstrap-switch-container')) {
            if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
                error.appendTo(element.parent().parent().parent().parent());
            } else {
                error.appendTo(element.parent().parent().parent().parent().parent());
            }
        }

        // Unstyled checkboxes, radios
        else if (element.parents('div').hasClass('checkbox') || element.parents('div').hasClass('radio')) {
            error.appendTo(element.parent().parent().parent());
        }

        // Input with icons and Select2
        else if (element.parents('div').hasClass('has-feedback') || element.hasClass('select2-hidden-accessible')) {
            error.appendTo(element.parent());
        }

        // Inline checkboxes, radios
        else if (element.parents('label').hasClass('checkbox-inline') || element.parents('label').hasClass('radio-inline')) {
            error.appendTo(element.parent().parent());
        }

        // Input group, styled file input
        else if (element.parent().hasClass('uploader') || element.parents().hasClass('input-group')) {
            error.appendTo(element.parent().parent());
        } else {
            error.insertAfter(element);
        }
    }
});






// valida los select (combobox)
jQuery.validator.addMethod("valueNotEquals", function (value, element, arg) {
    return arg !== value;
}, "Selecciona una opción");

// valida el ingreso solo alfanumerico
jQuery.validator.addMethod("alphanumeric", function (value, element) {
    return this.optional(element) || /^[\w.]+$/i.test(value);
}, "Por favor, debe ingresar un valor alfanumérico");

// valida el ingreso solo letras
jQuery.validator.addMethod("lettersonly", function (value, element) {
//    return this.optional(element) || /^[a-z\s]+$/i.test(value);
    return this.optional(element) || /^[a-zA-Z\u00C0-\u00ff\s]+$/.test(value.trim());
}, "Por favor, debe ingresar solo caracteres");

// valida el ingreso de una fecha válida
jQuery.validator.addMethod("dateonly", function (value, element) {
    var check = false;
    var re = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
    if (re.test(value)) {
        try {
            check = true;
        } catch (e) {
            alert(e);
            check = false;
        }
    } else
        check = false;
    return this.optional(element) || check;
}, "Por favor, debe ingresar una fecha permitida (dd/mm/yyyy)");

// valida la fecha de inicio y fecha fin
jQuery.validator.addMethod("greaterThan", function (value, element, params) {
    if (!/Invalid|NaN/.test(new Date(value))) {
        return new Date(value) >= new Date($(params).val());
    }
    return isNaN(value) && isNaN($(params).val())
            || (Number(value) >= Number($(params).val()));
}, 'Por favor, esta fecha debe ser mayor o igual a la fecha de inicio');

jQuery.extend(jQuery.validator.messages, {
    required: "Este campo es requerido.",
    remote: "Please fix this field.",
    email: "Por favor, ingrese un correo válido.",
    url: "Please enter a valid URL.",
    date: "Please enter a valid date.",
    dateISO: "Please enter a valid date (ISO).",
    number: "Please enter a valid number.",
    digits: "Please enter only digits.",
    creditcard: "Please enter a valid credit card number.",
    equalTo: "Please enter the same value again.",
    accept: "Solo se aceptan los siguientes formatos (png, jpg, jpeg).",
    maxlength: jQuery.validator.format("Por favor, ingresar {0} caracteres."),
    minlength: jQuery.validator.format("Por favor, ingresar {0} caracteres."),
    rangelength: jQuery.validator.format("Please enter a value between {0} and {1} characters long."),
    range: jQuery.validator.format("Please enter a value between {0} and {1}."),
    max: jQuery.validator.format("Please enter a value less than or equal to {0}."),
    min: jQuery.validator.format("Please enter a value greater than or equal to {0}.")
});