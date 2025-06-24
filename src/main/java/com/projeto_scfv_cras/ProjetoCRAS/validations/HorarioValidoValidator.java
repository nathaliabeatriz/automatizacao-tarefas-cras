package com.projeto_scfv_cras.ProjetoCRAS.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class HorarioValidoValidator implements ConstraintValidator<HorarioValido, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            var clazz = value.getClass();

            LocalTime horarioInicio = (LocalTime) clazz.getMethod("getHorarioInicio").invoke(value);
            LocalTime horarioTermino = (LocalTime) clazz.getMethod("getHorarioTermino").invoke(value);

            if (horarioInicio == null || horarioTermino == null) {
                return true;
            }

            if (!horarioTermino.isAfter(horarioInicio)) {
                context.disableDefaultConstraintViolation(); 
                context.buildConstraintViolationWithTemplate("O horário de término deve ser após o de início")
                       .addPropertyNode("horarioTermino") 
                       .addConstraintViolation();
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

