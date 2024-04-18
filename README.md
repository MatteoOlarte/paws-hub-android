# Testing
--------
Unit Test
  - Selected fragments 
     
     
    ```kotlin
    public fun validateEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
    ```

    Prueba Unitaria:

     ```kotlin
     class EmailSignUpViewModelTest{
     @Test
     fun EmailValidatorUnitTest(){
        var emailTestCase : String? = "example@gmail.com"
        assertEquals(true,EmailSignUpViewModel().validateEmail(emailTestCase!!))
         }
     ```
     Explicacion:
 
     Esta prueba valida si el email ingresado por el usuario es correcto de acuerdo a los parametros seleccionados, es decir debe contener caracteres como @, en este caso en particular se hizo uso del email 
     example@gmail.com (Valido correctamente)



     ```kotlin
     public fun validatePasswords(p1: String, p2: String) = p1 == p2
     ```

     
     Prueba Unitaria:
    
     ```kotlin
     @Test
     fun  PasswordConfirmator(){
        var pass : String? = "123"
        var confPass : String? = "123"
        assertEquals(true,EmailSignUpViewModel().validatePasswords(pass!!,confPass!!))
        }
     }
     ```

    Esta prueba valida si la contraseña que crea el usuario, en su doble confirmacion es correcta, es decir si las dos contraseñas (Password y ConfirmPassword) son iguales, en este caso en particular se observa que se valida correctamente

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


     Prueba Unitaria
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

    Esta prueba valida si los campos que se deben llenar al momento del loggin son validados, es decir aquellos campos que poseen constraints de NOT NULL deberan validarse para evitar errores
     
     
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

<<<


Resumen

|ID|TIPOPRUEBA|ENTORNOPRUEBA|CASOPRUEBA|CORRECTA/INCORRECTA|
|-|-|-|-|-|
|01|UNITARIA |INGRESO DE EMAIL SOBRE EL LOGGIN |EMAIL: example@gmail.com | CORRECTO|
|02|UNITARIA|INGRESO DE PASSWORD AL REGISTRARSE, CONFIRMACION DE CONTRASEÑA|PASSWORD:123 ; PASSWORD:123|CORRECTO|
|03|UNITARIA|INGRESO DE EMAIL Y PASSWORD SOBRE EL LOGGIN|EMAIL: example@gmail.comk ; PASSWORD: email12345|CORRECTO|
|04|INTEGRACION|INTEGRACION DE FIREBASE AUTH (LOGIN)|EMAIL:example@gmail.com ; PASSWORD: example1|CORRECTO|
|05|INTEGRACION|


     

         


         
