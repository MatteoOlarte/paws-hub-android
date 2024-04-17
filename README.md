# Testing
--------
Unit Test
  - Selected fragments 
     
     ```kotlin
     public fun validatePasswords(p1: String, p2: String) = p1 == p2
     ```

    ```kotlin
    public fun validateEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
    ```

    A los dos fragmentos anteriores se aplica la siguiente prueba unitario

     ```kotlin
     package com.software3.paws_hub_android.viewmode
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
     ```


    *Esto dio como resultado satisfactorio* la prueba para el primer fragmento ´´´kotlin EmailValidatorUnitTest()´´´ verifica si el email ingresado por un usuario en este caso (example@gmail.com) se esta validando correctamente sobre firebase


     ```kotlin
     private fun validateFields(fields: List<String?>): Boolean {
        for (f in fields) {
            if (f.isNullOrBlank()) return false
        }
        return true
     }
     ```

     ```kotlin
     import org.junit.Test
     import org.junit.jupiter.api.Assertions.*

     class EmailSignInViewModelTest{
        @Test
        fun validateFields_Check(){
        var email : String? = "email@gmail.com"
        var password : String? = "email12345"
        val fieldsOfTestCase = listOf(email,password)
        assertEquals(true,EmailSignInViewModel().validateFields(fieldsOfTestCase))
        }
     }
     ```

     
     
Integration Test
  (Firebase integration)
  - Caso de prueba:
    
     Ingresar (Email,Password) validos
     Email = example@gmail.com
     Password = example1234

  - Salida esperada

    Redireccion al punto de entrada MainActivity.kt

    
   (VieWModel - View)
   -Caso de prueba:

   Ingresar (Email,password) Invalidos
   Email = example@gmail.com
   Password = example1

   - Salida Esperada
   ERROR Datos incorrectos o no se encuentra registrado

  

     

         


         
