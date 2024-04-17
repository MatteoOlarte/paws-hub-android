package com.software3.paws_hub_android.viewmodel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EmailSignUpViewModelTest{
    @Test
    fun EmailValidatorUnitTest(){
        var emailTestCase : String? = "example@gmail.com"
        assertEquals(true,EmailSignUpViewModel().validateEmail(emailTestCase!!))
    }

    @Test
    fun  PasswordConfirmator(){
        var pass : String? = "123"
        var confPass : String? = "123"
        assertEquals(true,EmailSignUpViewModel().validatePasswords(pass!!,confPass!!))
    }
}