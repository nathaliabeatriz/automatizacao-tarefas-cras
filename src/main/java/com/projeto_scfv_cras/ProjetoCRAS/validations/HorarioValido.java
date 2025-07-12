package com.projeto_scfv_cras.ProjetoCRAS.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HorarioValidoValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface HorarioValido {
    String message() default "O horário de término deve ser após o horário de início";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}